from django.shortcuts import render, redirect
from departmentManage import models

# Create your views here.

def department_list(request):
    """部门列表"""
    # 从数据库获取所有部门
    queryset = models.Department.objects.all()
    return render(request, 'department_list.html', {'queryset': queryset})

def department_add(request):
    """添加部门"""
    if request.method == 'GET':
        return render(request, 'department_add.html')
    # 获取用户通过POST提交的数据
    name = request.POST.get('name')
    # 保存到数据库
    models.Department.objects.create(name=name)
    # 重定向回部门列表页面
    return redirect('/department/list/')

def department_delete(request):
    """删除部门"""
    # 获取要删除的部门ID
    nid = request.GET.get('nid')
    # 从数据库删除
    models.Department.objects.filter(id=nid).delete()
    # 重定向回部门列表页面
    return redirect('/department/list/')

def department_edit(request, nid):
    """修改部门"""
    if request.method == 'GET':
        # 根据id获取要编辑的部门对象
        row_object = models.Department.objects.filter(id=nid).first()
        return render(request, 'department_edit.html', {'row_object': row_object})

    # 获取用户提交的修改后的名称
    name = request.POST.get('name')
    # 在数据库中更新
    models.Department.objects.filter(id=nid).update(name=name)
    # 重定向回部门列表页面
    return redirect('/department/list/')
