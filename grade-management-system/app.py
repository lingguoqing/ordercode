import io
import os
from datetime import datetime

import pymysql
from dotenv import load_dotenv
from flask import Flask, render_template, request, redirect, url_for, flash, jsonify, send_file, abort
from flask_login import LoginManager, UserMixin, login_user, login_required, logout_user, current_user
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy
from reportlab.lib.pagesizes import letter
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.pdfgen import canvas
from sqlalchemy.exc import IntegrityError
from werkzeug.security import generate_password_hash, check_password_hash

from config import config

# 加载环境变量
load_dotenv()

# 设置 PyMySQL 为默认的 MySQL 驱动
pymysql.install_as_MySQLdb()

app = Flask(__name__)
# 使用开发环境配置
app.config.from_object(config['development'])

db = SQLAlchemy(app)
migrate = Migrate(app, db)
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'


# 用户模型
class User(UserMixin, db.Model):
    __tablename__ = 'users'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    username = db.Column(db.String(80), unique=True, nullable=False, index=True)
    password_hash = db.Column(db.String(255), nullable=False)
    role = db.Column(db.String(20), nullable=False, index=True)  # admin, student, teacher
    name = db.Column(db.String(80), nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    courses = db.relationship('Course', backref='teacher', lazy=True)
    grades = db.relationship('Grade', backref='student', lazy=True, cascade='all, delete-orphan')
    enrollments = db.relationship('Enrollment', back_populates='student', lazy=True, cascade='all, delete-orphan')

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)


# 课程模型
class Course(db.Model):
    __tablename__ = 'courses'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(80), nullable=False, index=True)
    teacher_id = db.Column(db.Integer, db.ForeignKey('users.id', ondelete='SET NULL'))
    max_students = db.Column(db.Integer, nullable=False, default=50)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    grades = db.relationship('Grade', backref='course', lazy=True, cascade='all, delete-orphan')
    enrollments = db.relationship('Enrollment', back_populates='course', lazy=True, cascade='all, delete-orphan')


# 学生选课记录模型
class Enrollment(db.Model):
    __tablename__ = 'enrollments'

    student_id = db.Column(db.Integer, db.ForeignKey('users.id', ondelete='CASCADE'), primary_key=True)
    course_id = db.Column(db.Integer, db.ForeignKey('courses.id', ondelete='CASCADE'), primary_key=True)
    operation_count = db.Column(db.Integer, nullable=False, default=0)  # 每门课程的报名操作次数
    is_active = db.Column(db.Boolean, nullable=False, default=True)  # 是否处于活跃报名状态
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    student = db.relationship('User', back_populates='enrollments')
    course = db.relationship('Course', back_populates='enrollments')


# 成绩模型
class Grade(db.Model):
    __tablename__ = 'grades'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    student_id = db.Column(db.Integer, db.ForeignKey('users.id', ondelete='CASCADE'), index=True)
    course_id = db.Column(db.Integer, db.ForeignKey('courses.id', ondelete='CASCADE'), index=True)
    score = db.Column(db.Float, nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    __table_args__ = (
        db.UniqueConstraint('student_id', 'course_id', name='unique_student_course'),
    )


@login_manager.user_loader
def load_user(user_id):
    if user_id is None:
        return None
    return User.query.get(int(user_id))


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
        role = request.form.get('role')
        name = request.form.get('name')
        email = request.form.get('email')

        if not all([username, password, role, name, email]):
            flash('所有字段都必须填写')
            return redirect(url_for('register'))

        if User.query.filter_by(username=username).first():
            flash('用户名已存在')
            return redirect(url_for('register'))

        user = User(username=username, role=role, name=name, email=email)
        user.set_password(password)
        db.session.add(user)
        db.session.commit()
        flash('注册成功')
        return redirect(url_for('login'))

    return render_template('register.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')

        if not username or not password:
            flash('用户名和密码不能为空')
            return redirect(url_for('login'))

        user = User.query.filter_by(username=username).first()

        if user and user.check_password(password):
            login_user(user)
            if user.role == 'admin':
                return redirect(url_for('admin_dashboard'))
            elif user.role == 'teacher':
                return redirect(url_for('teacher_dashboard'))
            else:
                return redirect(url_for('student_dashboard'))

        flash('用户名或密码错误')
    return render_template('login.html')


@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('index'))


# 管理员路由
@app.route('/admin/dashboard')
@login_required
def admin_dashboard():
    if current_user.role != 'admin':
        flash('无权访问')
        return redirect(url_for('index'))

    users = User.query.all()
    courses = Course.query.all()
    grades = Grade.query.all()
    teachers = User.query.filter_by(role='teacher').all()
    students = User.query.filter_by(role='student').all()

    return render_template('admin_dashboard.html',
                           users=users,
                           courses=courses,
                           grades=grades,
                           teachers=teachers,
                           students=students)


@app.route('/admin/user/add', methods=['POST'])
@login_required
def add_user():
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    data = request.form
    if not all([data.get('username'), data.get('password'), data.get('role'), data.get('name'), data.get('email')]):
        return jsonify({'error': '所有字段都必须填写'}), 400

    user = User(
        username=data['username'],
        role=data['role'],
        name=data['name'],
        email=data['email']
    )
    user.set_password(data['password'])

    db.session.add(user)
    db.session.commit()

    return jsonify({'message': '用户添加成功'})


@app.route('/admin/user/edit/<int:user_id>', methods=['POST'])
@login_required
def admin_user_edit(user_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    user = User.query.get(user_id)
    if not user:
        return jsonify({'error': '用户未找到'}), 404

    data = request.form
    username = data.get('username')
    name = data.get('name')
    email = data.get('email')
    role = data.get('role')
    new_password = data.get('new_password')

    # 验证输入
    if not all([username, name, email, role]):
        return jsonify({'error': '所有字段都必须填写'}), 400

    # 检查用户名是否重复 (排除当前用户)
    if User.query.filter(User.username == username, User.id != user_id).first():
        return jsonify({'error': '用户名已存在'}), 400

    # 检查邮箱是否重复 (排除当前用户)
    if User.query.filter(User.email == email, User.id != user_id).first():
        return jsonify({'error': '邮箱已存在'}), 400

    user.username = username
    user.name = name
    user.email = email
    user.role = role

    if new_password:
        user.set_password(new_password)

    db.session.commit()
    return jsonify({'message': '用户信息更新成功'})


@app.route('/admin/user/delete/<int:user_id>', methods=['POST'])
@login_required
def admin_user_delete(user_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    user = User.query.get(user_id)
    if not user:
        return jsonify({'error': '用户未找到'}), 404

    try:
        db.session.delete(user)
        db.session.commit()
        return jsonify({'message': '用户删除成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'删除用户失败: {str(e)}'}), 500


@app.route('/admin/course/add', methods=['POST'])
@login_required
def add_course():
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    data = request.form
    if not data.get('name'):
        return jsonify({'error': '课程名称不能为空'}), 400

    course = Course(
        name=data['name'],
        teacher_id=data.get('teacher_id') if data.get('teacher_id') else None
    )

    db.session.add(course)
    db.session.commit()

    return jsonify({'message': '课程添加成功'})


@app.route('/admin/course/edit/<int:course_id>', methods=['POST'])
@login_required
def admin_course_edit(course_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    course = Course.query.get(course_id)
    if not course:
        return jsonify({'error': '课程未找到'}), 404

    data = request.form
    name = data.get('name')
    teacher_id = data.get('teacher_id')
    max_students = data.get('max_students')

    if not name:
        return jsonify({'error': '课程名称不能为空'}), 400

    if not max_students or not max_students.isdigit() or int(max_students) < 1:
        return jsonify({'error': '最大学生数量必须是大于0的整数'}), 400

    # 检查课程名称是否重复 (排除当前课程)
    if Course.query.filter(Course.name == name, Course.id != course_id).first():
        return jsonify({'error': '课程名称已存在'}), 400

    # 检查新的最大学生数量是否小于当前学生数量
    if int(max_students) < len(course.students):
        return jsonify({'error': '最大学生数量不能小于当前报名人数'}), 400

    course.name = name
    course.teacher_id = int(teacher_id) if teacher_id else None
    course.max_students = int(max_students)

    db.session.commit()
    return jsonify({'message': '课程信息更新成功'})


@app.route('/admin/course/delete/<int:course_id>', methods=['POST'])
@login_required
def admin_course_delete(course_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    course = Course.query.get(course_id)
    if not course:
        return jsonify({'error': '课程未找到'}), 404

    try:
        db.session.delete(course)
        db.session.commit()
        return jsonify({'message': '课程删除成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'删除课程失败: {str(e)}'}), 500


@app.route('/admin/grade/add', methods=['POST'])
@login_required
def add_grade():
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    data = request.form
    if not all([data.get('student_id'), data.get('course_id'), data.get('score')]):
        return jsonify({'error': '所有字段都必须填写'}), 400

    try:
        score = float(data['score'])
        if not (0 <= score <= 100):
            return jsonify({'error': '成绩必须在0-100之间'}), 400
    except ValueError:
        return jsonify({'error': '成绩必须是有效的数字'}), 400

    grade = Grade(
        student_id=int(data['student_id']),
        course_id=int(data['course_id']),
        score=score
    )

    db.session.add(grade)
    db.session.commit()

    return jsonify({'message': '成绩添加成功'})


@app.route('/admin/grade/edit/<int:grade_id>', methods=['POST'])
@login_required
def admin_grade_edit(grade_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    grade = Grade.query.get(grade_id)
    if not grade:
        return jsonify({'error': '成绩未找到'}), 404

    data = request.form
    student_id = data.get('student_id')
    course_id = data.get('course_id')
    score = data.get('score')

    if not all([student_id, course_id, score]):
        return jsonify({'error': '所有字段都必须填写'}), 400

    try:
        score = float(score)
        if not (0 <= score <= 100):
            return jsonify({'error': '成绩必须在0-100之间'}), 400
    except ValueError:
        return jsonify({'error': '成绩必须是有效的数字'}), 400

    # 检查学生和课程是否存在
    student = User.query.get(int(student_id))
    course = Course.query.get(int(course_id))
    if not student or not course:
        return jsonify({'error': '学生或课程不存在'}), 400

    # 检查是否存在重复的成绩记录 (排除当前成绩)
    if Grade.query.filter(Grade.student_id == int(student_id), Grade.course_id == int(course_id),
                          Grade.id != grade_id).first():
        return jsonify({'error': '该学生该课程的成绩已存在'}), 400

    grade.student_id = int(student_id)
    grade.course_id = int(course_id)
    grade.score = score

    db.session.commit()
    return jsonify({'message': '成绩信息更新成功'})


@app.route('/admin/grade/delete/<int:grade_id>', methods=['POST'])
@login_required
def admin_grade_delete(grade_id):
    if current_user.role != 'admin':
        return jsonify({'error': '无权访问'}), 403

    grade = Grade.query.get(grade_id)
    if not grade:
        return jsonify({'error': '成绩未找到'}), 404

    try:
        db.session.delete(grade)
        db.session.commit()
        return jsonify({'message': '成绩删除成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'删除成绩失败: {str(e)}'}), 500


# 教师路由
@app.route('/teacher/dashboard')
@login_required
def teacher_dashboard():
    if current_user.role != 'teacher':
        flash('无权访问')
        return redirect(url_for('index'))

    courses = Course.query.filter_by(teacher_id=current_user.id).all()
    students = User.query.filter_by(role='student').all()
    grades = Grade.query.join(Course).filter(Course.teacher_id == current_user.id).all()

    return render_template('teacher_dashboard.html',
                           courses=courses,
                           students=students,
                           grades=grades)


@app.route('/teacher/profile/edit', methods=['POST'])
@login_required
def edit_teacher_profile():
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    data = request.form
    if not all([data.get('name'), data.get('email')]):
        return jsonify({'error': '姓名和邮箱不能为空'}), 400

    current_user.name = data['name']
    current_user.email = data['email']

    if data.get('new_password'):
        current_user.set_password(data['new_password'])

    db.session.commit()
    return jsonify({'message': '个人信息更新成功'})


@app.route('/teacher/course/edit/<int:course_id>', methods=['POST'])
@login_required
def teacher_course_edit(course_id):
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    course = Course.query.get(course_id)
    if not course or course.teacher_id != current_user.id:
        return jsonify({'error': '课程未找到或无权编辑'}), 404

    data = request.form
    name = data.get('name')
    max_students = data.get('max_students')

    if not name:
        return jsonify({'error': '课程名称不能为空'}), 400

    if not max_students or not max_students.isdigit() or int(max_students) < 1:
        return jsonify({'error': '最大学生数量必须是大于0的整数'}), 400

    # 检查课程名称是否重复 (排除当前课程)
    if Course.query.filter(Course.name == name, Course.id != course_id).first():
        return jsonify({'error': '课程名称已存在'}), 400

    # 检查新的最大学生数量是否小于当前学生数量
    if int(max_students) < len(course.students):
        return jsonify({'error': '最大学生数量不能小于当前报名人数'}), 400

    course.name = name
    course.max_students = int(max_students)
    db.session.commit()
    return jsonify({'message': '课程信息更新成功'})


@app.route('/teacher/course/detail/<int:course_id>')
@login_required
def teacher_course_detail(course_id):
    if current_user.role != 'teacher':
        flash('无权访问')
        return redirect(url_for('index'))

    course = Course.query.get_or_404(course_id)
    # 确保教师是该课程的授课教师
    if course.teacher_id != current_user.id:
        flash('无权查看该课程详情')
        return redirect(url_for('teacher_dashboard'))

    return render_template('teacher_course_detail.html', course=course)


@app.route('/teacher/student/grades/<int:student_id>')
@login_required
def teacher_student_grades(student_id):
    if current_user.role != 'teacher':
        flash('无权访问')
        return redirect(url_for('index'))

    student = User.query.get_or_404(student_id)
    # 获取该学生由当前教师教授的课程的成绩
    grades = Grade.query.filter_by(student_id=student_id).join(Course).filter(
        Course.teacher_id == current_user.id).all()

    return render_template('teacher_student_grades.html', student=student, grades=grades)


@app.route('/teacher/grade/add', methods=['POST'])
@login_required
def teacher_add_grade():
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    data = request.form
    if not all([data.get('student_id'), data.get('course_id'), data.get('score')]):
        return jsonify({'error': '所有字段都必须填写'}), 400

    try:
        score = float(data['score'])
        if not (0 <= score <= 100):
            return jsonify({'error': '成绩必须在0-100之间'}), 400
    except ValueError:
        return jsonify({'error': '成绩必须是有效的数字'}), 400

    course = Course.query.get(int(data['course_id']))
    if not course or course.teacher_id != current_user.id:
        return jsonify({'error': '无权操作此课程'}), 403

    grade = Grade(
        student_id=int(data['student_id']),
        course_id=int(data['course_id']),
        score=score
    )

    db.session.add(grade)
    db.session.commit()

    return jsonify({'message': '成绩添加成功'})


@app.route('/teacher/grade/edit/<int:grade_id>', methods=['POST'])
@login_required
def teacher_grade_edit(grade_id):
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    grade = Grade.query.get(grade_id)
    if not grade:
        return jsonify({'error': '成绩未找到'}), 404

    # 确保教师是该成绩所在课程的授课教师
    if grade.course.teacher_id != current_user.id:
        return jsonify({'error': '无权编辑该成绩'}), 403

    data = request.form
    # student_id 和 course_id 在编辑时通常不会改变，但在模态框中是禁用的
    # 这里主要关注成绩score的更新
    score = data.get('score')

    if not score:
        return jsonify({'error': '成绩不能为空'}), 400

    try:
        score = float(score)
        if not (0 <= score <= 100):
            return jsonify({'error': '成绩必须在0-100之间'}), 400
    except ValueError:
        return jsonify({'error': '成绩必须是有效的数字'}), 400

    grade.score = score

    try:
        db.session.commit()
        return jsonify({'message': '成绩信息更新成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'更新失败: {str(e)}'}), 500


@app.route('/teacher/grade/delete/<int:grade_id>', methods=['POST'])
@login_required
def teacher_grade_delete(grade_id):
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    grade = Grade.query.get(grade_id)
    if not grade:
        return jsonify({'error': '成绩未找到'}), 404

    # 确保教师是该成绩所在课程的授课教师
    if grade.course.teacher_id != current_user.id:
        return jsonify({'error': '无权删除该成绩'}), 403

    try:
        db.session.delete(grade)
        db.session.commit()
        return jsonify({'message': '成绩删除成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'删除失败: {str(e)}'}), 500


@app.route('/api/teacher/grades_by_course/<int:course_id>')
@login_required
def get_teacher_grades_by_course(course_id):
    if current_user.role != 'teacher':
        abort(403)

    grades_query = Grade.query.join(Course).filter(Course.teacher_id == current_user.id)

    if course_id != 0:
        grades_query = grades_query.filter(Grade.course_id == course_id)

    grades = grades_query.all()
    grades_data = []
    for grade in grades:
        grades_data.append({
            'id': grade.id,
            'student_name': grade.student.name,
            'student_id': grade.student.id,
            'course_name': grade.course.name,
            'course_id': grade.course.id,
            'score': grade.score
        })
    return jsonify(grades_data)


@app.route('/api/teacher/students_by_course/<int:course_id>')
@login_required
def get_teacher_students_by_course(course_id):
    if current_user.role != 'teacher':
        abort(403)

    students_data = []
    unique_student_ids = set()

    # 获取当前教师的所有课程
    teacher_courses = Course.query.filter_by(teacher_id=current_user.id).all()
    teacher_course_ids = [c.id for c in teacher_courses]

    if course_id != 0:
        # 如果指定了课程ID，则只考虑该课程下的学生
        if course_id not in teacher_course_ids:
            return jsonify({'error': '您无权查看该课程的学生'}), 403

        enrollments = Enrollment.query.filter_by(course_id=course_id, is_active=True).all()
        for enrollment in enrollments:
            if enrollment.student_id not in unique_student_ids:
                student = enrollment.student
                # 获取学生在该课程的成绩
                grade = Grade.query.filter_by(student_id=student.id, course_id=course_id).first()
                students_data.append({
                    'id': student.id,
                    'name': student.name,
                    'email': student.email,
                    'enrolled_courses': [enrollment.course.name],
                    'grade_for_selected_course': grade.score if grade else '暂无'
                })
                unique_student_ids.add(student.id)
    else:
        # 如果未指定课程ID，则获取当前教师所有课程下的学生
        # 遍历教师的所有课程
        for course in teacher_courses:
            # 获取该课程下的活跃报名
            enrollments = Enrollment.query.filter_by(course_id=course.id, is_active=True).all()
            for enrollment in enrollments:
                if enrollment.student_id not in unique_student_ids:
                    student = enrollment.student
                    # 获取学生在该课程的成绩
                    grade = Grade.query.filter_by(student_id=student.id, course_id=course.id).first()
                    # 获取学生在该教师所有课程下的所有活跃课程
                    student_all_active_enrollments = Enrollment.query.filter_by(student_id=student.id,
                                                                                is_active=True).all()
                    enrolled_course_names = [e.course.name for e in student_all_active_enrollments if
                                             e.course.teacher_id == current_user.id]

                    students_data.append({
                        'id': student.id,
                        'name': student.name,
                        'email': student.email,
                        'enrolled_courses': enrolled_course_names,
                        'grade_for_selected_course': grade.score if grade else '暂无'
                        # This is not perfectly accurate for 'All Courses'
                    })
                    unique_student_ids.add(student.id)

    return jsonify(students_data)


@app.route('/teacher/grade/report/<int:student_id>')
@login_required
def generate_grade_report(student_id):
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403

    student = User.query.get_or_404(student_id)
    grades = Grade.query.filter_by(student_id=student_id).join(Course).filter(
        Course.teacher_id == current_user.id).all()

    # 注册中文字体
    font_path = os.path.join(app.root_path, 'fonts', 'SimHei.ttf')
    try:
        pdfmetrics.registerFont(TTFont('SimHei', font_path))
    except Exception as e:
        # 如果字体文件不存在或注册失败，可以考虑记录日志或返回错误信息
        print(f"字体注册失败: {e}. 请确保 'SimHei.ttf' 字体文件位于项目根目录下的 'fonts' 文件夹中。")
        return jsonify({'error': '生成成绩单失败：字体文件未找到或无法加载。'}), 500

    # 创建PDF
    buffer = io.BytesIO()
    p = canvas.Canvas(buffer, pagesize=letter)

    # 添加标题
    p.setFont("SimHei", 16)
    p.drawString(100, 750, f"{student.name}的成绩单")

    # 添加成绩信息
    p.setFont("SimHei", 12)
    y = 700
    for grade in grades:
        p.drawString(100, y, f"课程：{grade.course.name}")
        p.drawString(300, y, f"成绩：{grade.score}")
        y -= 20

    p.save()
    buffer.seek(0)

    return send_file(
        buffer,
        as_attachment=True,
        download_name=f"{student.name}_成绩单.pdf",
        mimetype='application/pdf'
    )


# 学生路由
@app.route('/student/dashboard')
@login_required
def student_dashboard():
    if current_user.role != 'student':
        flash('无权访问')
        return redirect(url_for('index'))

    all_courses = Course.query.all()
    # 获取学生已报名的课程以及对应的操作次数
    student_enrollments = {e.course_id: e for e in current_user.enrollments}
    return render_template('student_dashboard.html', all_courses=all_courses, student_enrollments=student_enrollments)


@app.route('/student/profile/edit', methods=['POST'])
@login_required
def student_edit_profile():
    if current_user.role != 'student':
        abort(403)

    name = request.form.get('name')
    email = request.form.get('email')
    new_password = request.form.get('new_password')

    if not name or not email:
        return jsonify({'error': '姓名和邮箱不能为空'}), 400

    # 检查邮箱是否已被其他用户使用 (除了当前用户自己)
    existing_user = User.query.filter(User.email == email, User.id != current_user.id).first()
    if existing_user:
        return jsonify({'error': '该邮箱已被注册'}), 400

    current_user.name = name
    current_user.email = email

    if new_password:
        current_user.password = generate_password_hash(new_password)

    try:
        db.session.commit()
        return jsonify({'message': '个人信息更新成功'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'更新失败: {str(e)}'}), 500


@app.route('/student/course/enroll/<int:course_id>', methods=['POST'])
@login_required
def student_enroll_course(course_id):
    if current_user.role != 'student':
        return jsonify({'error': '无权访问'}), 403

    course = Course.query.get(course_id)
    if not course:
        return jsonify({'error': '课程未找到'}), 404

    try:
        # 尝试查找现有选课记录
        enrollment = Enrollment.query.filter_by(student_id=current_user.id, course_id=course_id).first()

        if enrollment:
            # 如果记录存在，检查操作次数限制
            if enrollment.operation_count >= 3:
                return jsonify({'error': '该课程已达操作上限，无法再次报名'}), 400

            # 如果已存在但不是活跃状态（之前取消过），则重新激活
            if not enrollment.is_active:
                enrollment.is_active = True
                enrollment.operation_count += 1
                db.session.commit()
                return jsonify({'message': '已重新报名该课程，操作次数增加'})
            else:
                # 如果已活跃报名，则不增加操作次数，直接提示已报名
                return jsonify({'error': '您已报名该课程'}), 400
        else:
            # 如果是首次报名
            # 检查课程是否已满
            if len(course.enrollments) >= course.max_students:
                return jsonify({'error': '课程名额已满'}), 400

            new_enrollment = Enrollment(
                student_id=current_user.id,
                course_id=course_id,
                operation_count=1,
                is_active=True
            )
            db.session.add(new_enrollment)
            db.session.commit()
            return jsonify({'message': '课程报名成功'})
    except IntegrityError as e:
        db.session.rollback()
        # 如果出现IntegrityError，通常表示在并发情况下记录已经存在，重新尝试查询并更新
        enrollment = Enrollment.query.filter_by(student_id=current_user.id, course_id=course_id).first()
        if enrollment:
            if enrollment.operation_count >= 3:
                return jsonify({'error': '该课程已达操作上限，无法再次报名'}), 400
            if not enrollment.is_active:
                enrollment.is_active = True
                enrollment.operation_count += 1
                db.session.commit()
                return jsonify({'message': '已重新报名该课程，操作次数增加 (通过错误恢复)'})
            else:
                return jsonify({'error': '您已报名该课程 (通过错误恢复)'}), 400
        else:
            return jsonify({'error': f'报名失败: {str(e)}'}), 500
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'报名失败: {str(e)}'}), 500


@app.route('/student/course/unenroll/<int:course_id>', methods=['POST'])
@login_required
def student_unenroll_course(course_id):
    if current_user.role != 'student':
        return jsonify({'error': '无权访问'}), 403

    enrollment = Enrollment.query.filter_by(student_id=current_user.id, course_id=course_id).first()

    if not enrollment or not enrollment.is_active:
        return jsonify({'error': '未找到该课程的有效报名记录'}), 404

    try:
        # 取消报名（设置为非活跃），并增加操作次数
        enrollment.is_active = False
        enrollment.operation_count += 1
        db.session.commit()
        return jsonify({'message': '成功退出课程报名'})
    except Exception as e:
        db.session.rollback()
        return jsonify({'error': f'退出报名失败: {str(e)}'}), 500


@app.route('/api/student/<int:student_id>/courses')
@login_required
def get_student_courses(student_id):
    student = User.query.get_or_404(student_id)
    if current_user.role not in ['admin', 'teacher'] and current_user.id != student_id:
        abort(403)

    # 获取学生已报名的活跃课程
    active_enrollments = Enrollment.query.filter_by(student_id=student.id, is_active=True).all()
    courses = []
    for enrollment in active_enrollments:
        course = Course.query.get(enrollment.course_id)
        if course:
            courses.append({'id': course.id, 'name': course.name})
    return jsonify(courses)


@app.route('/api/student/my_grades')
@login_required
def get_my_grades():
    if current_user.role != 'student':
        abort(403)

    grades_data = []
    for grade in current_user.grades:
        grades_data.append({
            'course_name': grade.course.name,
            'score': grade.score
        })
    return jsonify(grades_data)


if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(debug=True)
