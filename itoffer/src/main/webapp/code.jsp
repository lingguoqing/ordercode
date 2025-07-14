<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-2.1.3.min.js"></script>
<!-- <script src="//unpkg.com/layui@2.6.8/dist/layui.js"/> -->

<body>
<script type="text/javascript">
    var index = parent.layer.getFrameIndex(window.name);
    $(function () {
        $(document).on('click', "#image", function (event) {
            var obj = this;
            var x = event.offsetX;//获取点击时鼠标相对图片坐标
            var y = event.offsetY;
            $.ajax({
                url: "UserServlet?type=Code", //ajax提交
                type: "post",
                data: {'x': x, "y": y},
                success: function (data) {
                    console.log(data);
                    obj.src = obj.src + "?date=" + new Date();
                    if (data == 'success') {
                        parent.layer.close(index);
                        // layer.closeAll();
                    } else {
                        location.reload();
                    }
                }
            })
        });
    })
</script>
<img id="image" src="${pageContext.request.contextPath}/ValidateCodeServlet" style="cursor: pointer;" disabled;>
<h3 align='center' style="color: red;">${sessionScope.errMsg}</h3>
</body>
</html>