from django.shortcuts import render, redirect
from staffManage import models
from departmentManage.models import Department
from staffManage.forms import LoginForm

# Create your views here.

def login(request):
    """登录"""
    if request.method == "GET":
        form = LoginForm()
        return render(request, 'login.html', {'form': form})

    form = LoginForm(data=request.POST)
    if form.is_valid():
        # 验证成功，获取用户名和密码
        user_input_object = models.UserInfo.objects.filter(**form.cleaned_data).first()
        if not user_input_object:
            form.add_error("password", "用户名或密码错误")
            return render(request, 'login.html', {'form': form})
        
        # 网站生成随机字符串，写到用户浏览器的cookie中，再写入到session中
        request.session["info"] = {'id': user_input_object.id, 'name': user_input_object.username}
        
        return redirect('/user/list/')

    return render(request, 'login.html', {'form': form})

def logout(request):
    """注销"""
    request.session.clear()
    return redirect('/login/')

def user_profile(request):
    """个人资料"""
    user_id = request.session.get("info", {}).get("id")
    user_object = models.UserInfo.objects.filter(id=user_id).first()
    return render(request, 'user_profile.html', {'user_object': user_object})

def user_list(request):
    """员工列表"""
    queryset = models.UserInfo.objects.all()
    return render(request, 'user_list.html', {'queryset': queryset})

def user_add(request):
    """添加员工"""
    if request.method == "GET":
        departments = Department.objects.all()
        return render(request, 'user_add.html', {'departments': departments})

    # 获取用户提交的数据
    username = request.POST.get("username")
    password = request.POST.get("password")
    age = request.POST.get("age")
    account = request.POST.get("account")
    create_time = request.POST.get("create_time")
    depart_id = request.POST.get("depart_id")
    gender = request.POST.get("gender")

    # 添加到数据库
    models.UserInfo.objects.create(
        username=username,
        password=password,
        age=age,
        account=account,
        create_time=create_time,
        depart_id=depart_id,
        gender=gender
    )
    return redirect("/user/list/")

def user_delete(request, nid):
    """删除员工"""
    models.UserInfo.objects.filter(id=nid).delete()
    return redirect("/user/list/")

def user_edit(request, nid):
    """编辑员工"""
    if request.method == "GET":
        user_object = models.UserInfo.objects.filter(id=nid).first()
        departments = Department.objects.all()
        return render(request, 'user_edit.html', {"user_object": user_object, "departments": departments})

    # 获取用户提交的数据
    username = request.POST.get("username")
    age = request.POST.get("age")
    account = request.POST.get("account")
    create_time = request.POST.get("create_time")
    depart_id = request.POST.get("depart_id")
    gender = request.POST.get("gender")

    # 在数据库中更新
    models.UserInfo.objects.filter(id=nid).update(
        username=username,
        age=age,
        account=account,
        create_time=create_time,
        depart_id=depart_id,
        gender=gender
    )
    return redirect("/user/list/")
