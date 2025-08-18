import os
from datetime import datetime, date, timedelta
from flask import Flask, render_template, request, redirect, url_for, session, flash, jsonify
from werkzeug.security import generate_password_hash, check_password_hash

from db import get_db_connection


def create_app() -> Flask:
    app = Flask(__name__)
    app.secret_key = os.environ.get("SECRET_KEY", "dev-secret-key")

    # 预约限制配置（环境变量可覆盖）
    book_start_str = os.environ.get("BOOK_START", "08:00")
    book_end_str = os.environ.get("BOOK_END", "22:00")
    min_days_ahead = int(os.environ.get("BOOK_MIN_DAYS_AHEAD", "0"))
    max_days_ahead = int(os.environ.get("BOOK_MAX_DAYS_AHEAD", "30"))

    def _parse_time_str(value: str):
        return datetime.strptime(value, "%H:%M").time()

    allowed_time_start = _parse_time_str(book_start_str)
    allowed_time_end = _parse_time_str(book_end_str)

    @app.context_processor
    def inject_globals():
        return {
            "current_user": get_current_user(),
            "year_now": datetime.now().year,
        }

    def get_current_user():
        if "user_id" not in session:
            return None
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT id, full_name, email, role FROM users WHERE id=%s", (session["user_id"],))
        user = cur.fetchone()
        cur.close()
        cnx.close()
        return user

    def login_required(view_func):
        def wrapper(*args, **kwargs):
            if "user_id" not in session:
                return redirect(url_for("login", next=request.path))
            return view_func(*args, **kwargs)
        wrapper.__name__ = view_func.__name__
        return wrapper

    def admin_required(view_func):
        def wrapper(*args, **kwargs):
            user = get_current_user()
            if not user or user["role"] != "admin":
                flash("需要管理员权限。", "warning")
                return redirect(url_for("index"))
            return view_func(*args, **kwargs)
        wrapper.__name__ = view_func.__name__
        return wrapper

    @app.route("/")
    def index():
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT id, name, building, capacity, description FROM classrooms WHERE is_featured=1 ORDER BY id LIMIT 4")
        featured = cur.fetchall()
        cur.execute("SELECT title FROM events ORDER BY sort_order, id LIMIT 10")
        events = [row["title"] for row in cur.fetchall()]
        cur.close()
        cnx.close()
        return render_template("index.html", featured=featured, events=events)

    @app.route("/classrooms")
    def classrooms():
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT id, name, building, capacity FROM classrooms ORDER BY building, name")
        rooms = cur.fetchall()
        cur.close()
        cnx.close()
        return render_template("classrooms.html", rooms=rooms)

    @app.route("/classrooms/<int:room_id>")
    def classroom_detail(room_id: int):
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT * FROM classrooms WHERE id=%s", (room_id,))
        room = cur.fetchone()
        cur.close()
        cnx.close()
        if not room:
            flash("教室不存在。", "warning")
            return redirect(url_for("classrooms"))
        return render_template("classroom_detail.html", room=room)

    @app.route("/book/<int:room_id>", methods=["GET", "POST"])
    @login_required
    def book(room_id: int):
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT id, name, building, capacity FROM classrooms WHERE id=%s", (room_id,))
        room = cur.fetchone()
        if not room:
            cur.close()
            cnx.close()
            flash("教室不存在。", "warning")
            return redirect(url_for("classrooms"))

        # 计算可预约日期范围（含起止）
        today = date.today()
        min_date = today + timedelta(days=min_days_ahead)
        max_date = today + timedelta(days=max_days_ahead)

        if request.method == "POST":
            full_name = request.form.get("full_name", "").strip()
            email = request.form.get("email", "").strip()
            phone = request.form.get("phone", "").strip()
            activity = request.form.get("activity", "").strip()
            reservation_date = request.form.get("date")
            start_time = request.form.get("start_time")
            end_time = request.form.get("end_time")

            if not (full_name and email and activity and reservation_date and start_time and end_time):
                flash("请完整填写表单。", "danger")
                return render_template(
                    "book.html",
                    room=room,
                    allowed_time_start=book_start_str,
                    allowed_time_end=book_end_str,
                    min_date=min_date.isoformat(),
                    max_date=max_date.isoformat(),
                )

            # 预约时段限制与基本合法性检查
            try:
                date_obj = datetime.strptime(reservation_date, "%Y-%m-%d").date()
                start_t = datetime.strptime(start_time, "%H:%M").time()
                end_t = datetime.strptime(end_time, "%H:%M").time()
            except ValueError:
                flash("日期或时间格式不正确。", "danger")
                return render_template(
                    "book.html",
                    room=room,
                    allowed_time_start=book_start_str,
                    allowed_time_end=book_end_str,
                    min_date=min_date.isoformat(),
                    max_date=max_date.isoformat(),
                )

            if not (min_date <= date_obj <= max_date):
                flash(f"仅可预约 {min_date} 至 {max_date} 之间的日期。", "danger")
                return render_template(
                    "book.html",
                    room=room,
                    allowed_time_start=book_start_str,
                    allowed_time_end=book_end_str,
                    min_date=min_date.isoformat(),
                    max_date=max_date.isoformat(),
                )

            if not (allowed_time_start <= start_t < end_t <= allowed_time_end):
                flash(f"预约时间需在 {book_start_str} - {book_end_str} 之间，且结束时间需晚于开始时间。", "danger")
                return render_template(
                    "book.html",
                    room=room,
                    allowed_time_start=book_start_str,
                    allowed_time_end=book_end_str,
                    min_date=min_date.isoformat(),
                    max_date=max_date.isoformat(),
                )

            # Conflict check
            cur.execute(
                """
                SELECT COUNT(*) AS cnt FROM reservations
                WHERE classroom_id=%s AND reservation_date=%s
                  AND status='booked'
                  AND NOT (end_time<=%s OR start_time>=%s)
                """,
                (room_id, reservation_date, start_time, end_time),
            )
            if cur.fetchone()["cnt"] > 0:
                flash("该时间段已被预订。", "danger")
                return render_template(
                    "book.html",
                    room=room,
                    allowed_time_start=book_start_str,
                    allowed_time_end=book_end_str,
                    min_date=min_date.isoformat(),
                    max_date=max_date.isoformat(),
                )

            cur.execute(
                """
                INSERT INTO reservations (user_id, classroom_id, activity, reservation_date, start_time, end_time)
                VALUES (%s, %s, %s, %s, %s, %s)
                """,
                (
                    session["user_id"],
                    room_id,
                    activity,
                    reservation_date,
                    start_time,
                    end_time,
                ),
            )
            cnx.commit()
            cur.close()
            cnx.close()
            flash("预订成功！", "success")
            return redirect(url_for("dashboard"))

        cur.close()
        cnx.close()
        return render_template(
            "book.html",
            room=room,
            allowed_time_start=book_start_str,
            allowed_time_end=book_end_str,
            min_date=min_date.isoformat(),
            max_date=max_date.isoformat(),
        )

    @app.route("/dashboard")
    @login_required
    def dashboard():
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute(
            """
            SELECT r.id,
                   c.name AS classroom_name,
                   r.reservation_date,
                   r.start_time,
                   r.end_time,
                   r.status,
                   u.full_name AS user_name,
                   u.email AS user_email
            FROM reservations r
            JOIN classrooms c ON c.id=r.classroom_id
            JOIN users u ON u.id=r.user_id
            WHERE r.user_id=%s AND r.status='booked'
            ORDER BY r.reservation_date, r.start_time
            """,
            (session["user_id"],),
        )
        bookings = cur.fetchall()
        cur.close()
        cnx.close()
        return render_template("dashboard.html", bookings=bookings)

    @app.post("/api/reservations/<int:reservation_id>/cancel")
    @login_required
    def cancel_reservation(reservation_id: int):
        cnx = get_db_connection()
        cur = cnx.cursor()
        cur.execute(
            "UPDATE reservations SET status='cancelled' WHERE id=%s AND user_id=%s",
            (reservation_id, session["user_id"]),
        )
        cnx.commit()
        affected = cur.rowcount
        cur.close()
        cnx.close()
        return jsonify({"ok": affected == 1})

    @app.route("/admin")
    @admin_required
    def admin_panel():
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT id, name, building, capacity FROM classrooms ORDER BY building, name")
        rooms = cur.fetchall()
        cur.execute(
            """
            SELECT r.id, u.full_name AS user_name, c.name AS classroom_name, r.reservation_date, r.start_time, r.end_time, r.status
            FROM reservations r
            JOIN users u ON u.id=r.user_id
            JOIN classrooms c ON c.id=r.classroom_id
            ORDER BY r.reservation_date DESC, r.start_time DESC
            LIMIT 100
            """
        )
        reservations = cur.fetchall()
        cur.close()
        cnx.close()
        return render_template("admin_dashboard.html", rooms=rooms, reservations=reservations)

    @app.route("/admin/classrooms/add", methods=["GET", "POST"])
    @admin_required
    def add_classroom():
        if request.method == "POST":
            name = request.form.get("name", "").strip()
            building = request.form.get("building", "").strip()
            capacity = request.form.get("capacity", "0").strip()
            description = request.form.get("description", "").strip()
            is_featured = 1 if request.form.get("is_featured") == "on" else 0
            cnx = get_db_connection()
            cur = cnx.cursor()
            cur.execute(
                "INSERT INTO classrooms (name, building, capacity, description, is_featured) VALUES (%s, %s, %s, %s, %s)",
                (name, building, int(capacity or 0), description, is_featured),
            )
            cnx.commit()
            cur.close()
            cnx.close()
            flash("教室已添加。", "success")
            return redirect(url_for("admin_panel"))
        return render_template("classroom_form.html", action="add", room=None)

    @app.route("/admin/classrooms/<int:room_id>/edit", methods=["GET", "POST"])
    @admin_required
    def edit_classroom(room_id: int):
        cnx = get_db_connection()
        cur = cnx.cursor(dictionary=True)
        cur.execute("SELECT * FROM classrooms WHERE id=%s", (room_id,))
        room = cur.fetchone()
        if not room:
            cur.close()
            cnx.close()
            flash("教室不存在。", "warning")
            return redirect(url_for("admin_panel"))
        if request.method == "POST":
            name = request.form.get("name", "").strip()
            building = request.form.get("building", "").strip()
            capacity = int(request.form.get("capacity", "0").strip() or 0)
            description = request.form.get("description", "").strip()
            is_featured = 1 if request.form.get("is_featured") == "on" else 0
            cur2 = cnx.cursor()
            cur2.execute(
                "UPDATE classrooms SET name=%s, building=%s, capacity=%s, description=%s, is_featured=%s WHERE id=%s",
                (name, building, capacity, description, is_featured, room_id),
            )
            cnx.commit()
            cur2.close()
            cur.close()
            cnx.close()
            flash("教室已更新。", "success")
            return redirect(url_for("admin_panel"))
        cur.close()
        cnx.close()
        return render_template("classroom_form.html", action="edit", room=room)

    @app.post("/admin/classrooms/<int:room_id>/delete")
    @admin_required
    def delete_classroom(room_id: int):
        cnx = get_db_connection()
        cur = cnx.cursor()
        cur.execute("DELETE FROM classrooms WHERE id=%s", (room_id,))
        cnx.commit()
        cur.close()
        cnx.close()
        flash("教室已删除。", "info")
        return redirect(url_for("admin_panel"))

    @app.route("/login", methods=["GET", "POST"])
    def login():
        if request.method == "POST":
            email = request.form.get("email", "").strip().lower()
            password = request.form.get("password", "")
            cnx = get_db_connection()
            cur = cnx.cursor(dictionary=True)
            cur.execute("SELECT id, password_hash, role FROM users WHERE email=%s", (email,))
            row = cur.fetchone()
            cur.close()
            cnx.close()
            if row and check_password_hash(row["password_hash"], password):
                session["user_id"] = row["id"]
                session["role"] = row["role"]
                flash("登录成功。", "success")
                next_url = request.args.get("next") or url_for("index")
                return redirect(next_url)
            flash("邮箱或密码错误。", "danger")
        return render_template("login.html")

    @app.route("/register", methods=["GET", "POST"])
    def register():
        if request.method == "POST":
            full_name = request.form.get("full_name", "")
            email = request.form.get("email", "").strip().lower()
            password = request.form.get("password", "")
            confirm = request.form.get("confirm", "")
            if not full_name or not email or not password or password != confirm:
                flash("请检查输入，密码需一致。", "danger")
                return render_template("register.html")
            cnx = get_db_connection()
            cur = cnx.cursor()
            try:
                cur.execute(
                    "INSERT INTO users (full_name, email, password_hash, role) VALUES (%s, %s, %s, 'user')",
                    (full_name, email, generate_password_hash(password)),
                )
                cnx.commit()
            except Exception:
                cnx.rollback()
                flash("注册失败，邮箱可能已被使用。", "danger")
                cur.close()
                cnx.close()
                return render_template("register.html")
            cur.close()
            cnx.close()
            flash("注册成功，请登录。", "success")
            return redirect(url_for("login"))
        return render_template("register.html")

    @app.route("/logout")
    def logout():
        session.clear()
        flash("您已退出。", "info")
        return redirect(url_for("index"))

    return app


app = create_app()

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(host="0.0.0.0", port=port, debug=True)


