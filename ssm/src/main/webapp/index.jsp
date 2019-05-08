<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>1</title>

    <script src="assets/sbadmin/jquery/dist/jquery.js" ></script>
</head>
<body>
<h2 id="myDiv">Hello World!</h2>

<script>
    $('#myDiv').on('click',function () {
        location = 'http://localhost:8888/zutai/dev/c1/a?id=16';
    })
</script>

</body>
</html>
