import io
import os
from datetime import datetime

import pymysql
from dotenv import load_dotenv
from flask import Flask, render_template, request, redirect, url_for, flash, jsonify, send_file
from flask_login import LoginManager, UserMixin, login_user, login_required, logout_user, current_user
from flask_sqlalchemy import SQLAlchemy
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
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
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    
    students = db.relationship('User', secondary='course_student', backref='enrolled_courses')
    grades = db.relationship('Grade', backref='course', lazy=True, cascade='all, delete-orphan')

# 课程-学生关联表
course_student = db.Table('course_student',
    db.Column('course_id', db.Integer, db.ForeignKey('courses.id', ondelete='CASCADE'), primary_key=True),
    db.Column('user_id', db.Integer, db.ForeignKey('users.id', ondelete='CASCADE'), primary_key=True),
    db.Column('created_at', db.DateTime, default=datetime.utcnow)
)

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

    if not name:
        return jsonify({'error': '课程名称不能为空'}), 400

    # 检查课程名称是否重复 (排除当前课程)
    if Course.query.filter(Course.name == name, Course.id != course_id).first():
        return jsonify({'error': '课程名称已存在'}), 400

    course.name = name
    course.teacher_id = int(teacher_id) if teacher_id else None
    
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
    if Grade.query.filter(Grade.student_id == int(student_id), Grade.course_id == int(course_id), Grade.id != grade_id).first():
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

    if not name:
        return jsonify({'error': '课程名称不能为空'}), 400

    # 检查课程名称是否重复 (排除当前课程)
    if Course.query.filter(Course.name == name, Course.id != course_id).first():
        return jsonify({'error': '课程名称已存在'}), 400

    course.name = name
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
    grades = Grade.query.filter_by(student_id=student_id).join(Course).filter(Course.teacher_id == current_user.id).all()
    
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

@app.route('/teacher/grade/report/<int:student_id>')
@login_required
def generate_grade_report(student_id):
    if current_user.role != 'teacher':
        return jsonify({'error': '无权访问'}), 403
    
    student = User.query.get_or_404(student_id)
    grades = Grade.query.filter_by(student_id=student_id).join(Course).filter(Course.teacher_id == current_user.id).all()
    
    # 创建PDF
    buffer = io.BytesIO()
    p = canvas.Canvas(buffer, pagesize=letter)
    
    # 添加标题
    p.setFont("Helvetica-Bold", 16)
    p.drawString(100, 750, f"{student.name}的成绩单")
    
    # 添加成绩信息
    p.setFont("Helvetica", 12)
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
    
    courses = current_user.enrolled_courses
    grades = Grade.query.filter_by(student_id=current_user.id).all()
    
    # 计算成绩统计
    scores = [grade.score for grade in grades]
    stats = {
        'average': round(sum(scores) / len(scores) if scores else 0, 2),
        'highest': max(scores) if scores else 0,
        'lowest': min(scores) if scores else 0,
        'pass_rate': round(len([s for s in scores if s >= 60]) / len(scores) * 100 if scores else 0, 2)
    }
    
    # 计算成绩分布
    distribution = [0] * 5
    for score in scores:
        if score < 60:
            distribution[0] += 1
        elif score < 70:
            distribution[1] += 1
        elif score < 80:
            distribution[2] += 1
        elif score < 90:
            distribution[3] += 1
        else:
            distribution[4] += 1
    
    return render_template('student_dashboard.html',
                         courses=courses,
                         grades=grades,
                         stats=stats,
                         grade_distribution=distribution,
                         course_names=[grade.course.name for grade in grades],
                         course_scores=[grade.score for grade in grades])

@app.route('/student/profile/edit', methods=['POST'])
@login_required
def edit_student_profile():
    if current_user.role != 'student':
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

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(debug=True) 