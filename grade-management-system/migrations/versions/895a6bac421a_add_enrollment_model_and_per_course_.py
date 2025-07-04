"""Add Enrollment model and per-course operation count

Revision ID: 895a6bac421a
Revises: c124f7c0f8c8
Create Date: 2025-06-13 12:17:55.865296

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import mysql

# revision identifiers, used by Alembic.
revision = '895a6bac421a'
down_revision = 'c124f7c0f8c8'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('enrollments',
    sa.Column('student_id', sa.Integer(), nullable=False),
    sa.Column('course_id', sa.Integer(), nullable=False),
    sa.Column('operation_count', sa.Integer(), nullable=False),
    sa.Column('created_at', sa.DateTime(), nullable=True),
    sa.Column('updated_at', sa.DateTime(), nullable=True),
    sa.ForeignKeyConstraint(['course_id'], ['courses.id'], ondelete='CASCADE'),
    sa.ForeignKeyConstraint(['student_id'], ['users.id'], ondelete='CASCADE'),
    sa.PrimaryKeyConstraint('student_id', 'course_id')
    )
    op.drop_table('course_student')
    with op.batch_alter_table('users', schema=None) as batch_op:
        batch_op.drop_column('operation_count')

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('users', schema=None) as batch_op:
        batch_op.add_column(sa.Column('operation_count', mysql.INTEGER(), server_default=sa.text("'0'"), autoincrement=False, nullable=False))

    op.create_table('course_student',
    sa.Column('course_id', mysql.INTEGER(), autoincrement=False, nullable=False),
    sa.Column('user_id', mysql.INTEGER(), autoincrement=False, nullable=False),
    sa.Column('created_at', mysql.DATETIME(), nullable=True),
    sa.ForeignKeyConstraint(['course_id'], ['courses.id'], name=op.f('course_student_ibfk_1'), ondelete='CASCADE'),
    sa.ForeignKeyConstraint(['user_id'], ['users.id'], name=op.f('course_student_ibfk_2'), ondelete='CASCADE'),
    sa.PrimaryKeyConstraint('course_id', 'user_id'),
    mysql_collate='utf8mb4_unicode_ci',
    mysql_default_charset='utf8mb4',
    mysql_engine='InnoDB'
    )
    op.drop_table('enrollments')
    # ### end Alembic commands ###
