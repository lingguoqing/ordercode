"""fix course_student duplicates

Revision ID: fix_course_student_duplicates
Revises: 924886b209da
Create Date: 2025-06-13 11:15:00.000000

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'fix_course_student_duplicates'
down_revision = '924886b209da'
branch_labels = None
depends_on = None


def upgrade():
    # 1. 删除重复记录，保留每个 course_id 和 user_id 组合的第一条记录
    op.execute("""
        DELETE t1 FROM course_student t1
        INNER JOIN course_student t2
        WHERE t1.course_id = t2.course_id 
        AND t1.user_id = t2.user_id
        AND t1.created_at > t2.created_at
    """)
    
    # 2. 删除现有的主键约束
    op.execute("ALTER TABLE course_student DROP PRIMARY KEY")
    
    # 3. 重新添加主键约束
    op.execute("ALTER TABLE course_student ADD PRIMARY KEY (course_id, user_id)")
    
    # 4. 添加唯一索引
    op.create_index('uq_course_student', 'course_student', ['course_id', 'user_id'], unique=True)


def downgrade():
    # 1. 删除唯一索引
    op.drop_index('uq_course_student', table_name='course_student')
    
    # 2. 删除主键约束
    op.execute("ALTER TABLE course_student DROP PRIMARY KEY")
    
    # 3. 重新添加主键约束
    op.execute("ALTER TABLE course_student ADD PRIMARY KEY (course_id, user_id)") 