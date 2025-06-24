"""
URL configuration for employeeManagementSystem project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from departmentManage import views as department_views
from staffManage import views as staff_views

urlpatterns = [
    # path('admin/', admin.site.urls),
    # 部门管理
    path('department/list/', department_views.department_list),
    path('department/add/', department_views.department_add),
    path('department/delete/', department_views.department_delete),
    path('department/<int:nid>/edit/', department_views.department_edit),

    # 员工管理
    path('user/list/', staff_views.user_list),
    path('user/add/', staff_views.user_add),
    path('user/<int:nid>/edit/', staff_views.user_edit),
    path('user/<int:nid>/delete/', staff_views.user_delete),

    # 登录与注销
    path('login/', staff_views.login),
    path('logout/', staff_views.logout),

    # 个人资料
    path('user/profile/', staff_views.user_profile),
]
