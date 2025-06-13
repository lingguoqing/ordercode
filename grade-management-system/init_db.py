import pymysql

from app import app, db, User


def init_db():
    # 创建数据库连接
    conn = pymysql.connect(
        host='localhost',
        user='root',
        password='WfEdcNutanix09',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )
    
    try:
        with conn.cursor() as cursor:
            # 创建数据库
            cursor.execute("CREATE DATABASE IF NOT EXISTS grade_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
            print("数据库创建成功")
    finally:
        conn.close()

    # 在应用上下文中创建表
    with app.app_context():
        db.create_all()
        
        # 检查是否已存在管理员账户
        admin = User.query.filter_by(username='admin').first()
        if not admin:
            # 创建默认管理员账户
            admin = User(
                username='admin',
                role='admin',
                name='系统管理员',
                email='admin@example.com'
            )
            admin.set_password('admin123')
            db.session.add(admin)
            db.session.commit()
            print("管理员账户创建成功")
        else:
            print("管理员账户已存在")

if __name__ == '__main__':
    init_db() 