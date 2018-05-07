<%
    String path = request.getContextPath();
    //用于定义基准路径
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<base href="<%=basePath%>">

<script src="js/jquery-3.2.1.js"></script>

