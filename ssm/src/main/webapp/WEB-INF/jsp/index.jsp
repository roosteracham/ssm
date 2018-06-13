<%--
  Created by IntelliJ IDEA.
  User: rooster
  Date: 2018/6/12
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>左侧导航</title>
</head>

<body id="bg">
<div id="menu">
    <button id="newProject" >新建工程</button>
    <button id="newSvg" >新建画面</button>
    <button id="exportProject" >保存工程</button>
    <button id="importProject" >导入工程</button>
    <button id="clearElements" >清空元素</button>
    <button id="deletesvg" >删除工程</button>
    <button id="deleteEle" >删除元素</button>
    <button id="groupEle" >组合元素</button>
    <button id="rotateLeft_90" >左旋90°</button>
    <button id="rotateRight_90" >右旋90°</button>
    <button id="rotateLeft_45" >左旋45°</button>
    <button id="rotateRight_45" >右旋45°</button>
    <button id="rotateLeft_30" >左旋30°</button>
    <button id="rotateRight_30" >右旋30°</button>
    <button id="backLayer" >置底层</button>
    <button id="frontLayer" >置顶层</button>
    <button id="backwardLayer" >向下一层</button>
    <button id="forwardLayer" >向上一层</button>
    <button id="importSVG" >导入图形</button>
    <button id="exportSVG" >导出图形</button>
    <button id="changeColor" >
        <input id="colorPicker" class="jscolor" value="ab2567" hidden="hidden" />
        填充颜色
    </button>
    <button id="bindPoint" >绑定测点</button>
    <button id="point" >绑定测点</button>
</div>
<div class="mycontainer">

    <div class="leftsidebar_box">
        <div class="line"></div>
        <dl class="system_log">
            <dt >基本图形<img src="../images/left/select_xl01.png"></dt>
            <dd class="st" id="line">直线</dd>
            <dd class="st" id="text">文本</dd>
            <dd class="st" id="rect">矩形</dd>
            <dd class="st" id="circle">圆形</dd>
            <dd class="st" id="ellipse">椭圆</dd>
            <dd class="st" id="tangle">三角形</dd>
            <dd class="st" id="roundRect">圆角矩形</dd>
            <dd class="st" id="pentagon">五边形</dd>
            <dd class="st" id="hexagon">六边形</dd>
            <dd class="st" id="octagon">八边形</dd>
            <dd class="st" id="pentagram">五角星</dd><!--
            <dd class="st" id="star">贝塞尔曲线</dd>-->
        </dl>
        <dl class="system_log">
            <dt >工控图形<img src="../images/left/select_xl01.png"></dt>
            <dd class="st" id="tank1">原料罐1</dd>
            <dd class="st" id="tank2">原料罐2</dd>
            <dd class="st" id="tank3">原料罐3</dd>
            <dd class="st" id="light">灯</dd>
            <dd class="st" id="flabellum">扇叶片</dd>
            <dd class="st" id="pipe">管道</dd>
            <dd class="st" id="uPipe">U形管道</dd>
            <dd class="st" id="rightAngleConnector">直角接头</dd>
            <dd class="st" id="tConnector">T型接头</dd>
            <dd class="st" id="crossConnector">十字型接头</dd>
            <dd class="st" id="pump">泵</dd>
            <dd class="st" id="valve">阀门</dd>
        </dl>
    </div>

    <div class="svg-container" id="svgContainer" style="position: relative; left: 15%;
    top:1%; width : 80%; height: 90%; background-color: lightgray; ">
    </div>
</div>

<link rel="stylesheet" type="text/css" href="../../static/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="../../static/css/svg.select.css"/>
<link rel="stylesheet" type="text/css" href="../../static/css/test.css"/>
<script type="text/javascript" src="../../static/js/jquery.min.js"></script>
<script type="text/javascript" src="../../static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../static/js/svg.js"></script>
<script type="text/javascript" src="../../static/js/svg.draggable.js"></script>
<script type="text/javascript" src="../../static/js/svg.select.js"></script>
<script type="text/javascript" src="../../static/js/svg.resize.js"></script>
<script type="text/javascript" src="../../static/js/svg.path.js"></script>
<script type="text/javascript" src="../../static/js/jscolor.js"></script>
<script type="text/javascript" src="../../static/js/test.js"></script>
<script type="text/javascript" src="../../static/js/ajaxOption.js"></script>
<script type="text/javascript" src="../../static/js/project.js"></script>
<script type="text/javascript" src="../../static/js/manipulate.js"></script>
<script type="text/javascript" src="../../static/js/addindustrialEle.js"></script>

<!-- 模态框（Modal） 新建工程时弹出 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    模态框（Modal）标题
                </h4>
            </div>
            <div id="modal" class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消
                </button>
                <button id="confirmNewPro" type="button" class="btn btn-primary" >
                    确认
                </button>
                <!--<input type="button" value="首页" id="confirmNewPro" class="btn btn-default" />-->
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!-- 模态框（Modal） 绑定测点时弹出 -->
<div class="modal fade" id="myBindPointModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myBindPointModalLabel">
                    模态框（Modal）标题
                </h4>
            </div>
            <div id="modalBindPoint" class="modal-body">

                // 下拉菜单
                <select id="pointType">
                    <option value="number" selected="selected">数值</option>
                    <option value="liquidLevel">液位</option>
                    <option value="switch" >开关</option>
                </select>
                <input type="text" id="pointName" />
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消
                </button>
                <button id="confirmBindPoint" type="button" class="btn btn-primary" >
                    确认
                </button>
                <!--<input type="button" value="首页" id="confirmNewPro" class="btn btn-default" />-->
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>
