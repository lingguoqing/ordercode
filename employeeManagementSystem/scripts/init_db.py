import os
import sys
import django
from django.utils import timezone

# Add project root to Python path
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASE_DIR)

# Set up Django environment
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'employeeManagementSystem.settings')
django.setup()

from departmentManage.models import Department
from staffManage.models import UserInfo

def main():
    """Main function to initialize data"""
    # 1. Create a default department
    dep_name = "默认部门"
    if not Department.objects.filter(name=dep_name).exists():
        Department.objects.create(name=dep_name)
        print(f"部门 '{dep_name}' 创建成功。")
    
    # 2. Get the department
    default_department = Department.objects.get(name=dep_name)

    # 3. Create a default admin user
    admin_username = "admin"
    if not UserInfo.objects.filter(username=admin_username).exists():
        UserInfo.objects.create(
            username=admin_username,
            password='123',
            age=30,
            account=0.00,
            create_time=timezone.now(),
            depart=default_department,
            gender=1
        )
        print(f"默认用户 '{admin_username}' 创建成功，密码为 '123'。")
    else:
        print(f"用户 '{admin_username}' 已存在。")

if __name__ == '__main__':
    main() 