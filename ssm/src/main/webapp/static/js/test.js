$(".leftsidebar_box dt").css({"background-color":"#3992d0"});

$(".leftsidebar_box dt img").attr("src","../images/left/select_xl01.png");

// 页面加载完之后才被调用
$(function(){
    $(".leftsidebar_box dd").hide();

    $(".leftsidebar_box dt").click(function(){
        $(".leftsidebar_box dt").css({"background-color":"#3992d0"})
        $(this).css({"background-color": "#317eb4"});
        $(this).parent().find('dd').removeClass("menu_chioce");
        $(".leftsidebar_box dt img").attr("src","../images/left/select_xl01.png");
        $(this).parent().find('img').attr("src","../images/left/select_xl.png");
        $(".menu_chioce").slideUp();
        $(this).parent().find('dd').slideToggle();
        $(this).parent().find('dd').addClass("menu_chioce");
    });
});

// 当前svg根元素
var svg = null;

// svg图形
var allSvgs = [
    "RECT",
    "CIRCLE",
    "ELLIPSE",
    "LINE",
    "POLYGON",
    "POLYLINE",
    "PATH",
    "TEXT"
];

// 是否是图形元素
function isSvgElement(name) {
    for (var i = 0; i < allSvgs.length; i++) {
        if (allSvgs[i].toLowerCase() === name) {
            return true;
        }
    }
    return false;
}

// 新建工程
$("#newSvg").on("click", function () {
    if (svg === null) {
        // 新建svg元素
        svg = SVG("svgContainer").size("100%", "100%");
        // 为svg添加点击事件， 点击非图形元素时取消选中效果
        svg.node.addEventListener("click", clickNonEleToClear);
    }
});

// 清除工程元素
$("#clearElements").on("click", function () {
    if(svg !== null) {
        svg.clear();
    }
});

// 清除工程
$("#deletesvg").on("click", function () {
    svg.clear();
    svg = null;
    $("#svgContainer svg").remove();
});

// 选中删除元素
$("#deleteEle").on("click", function () {
    var del = SVG.select('.selected');
    for (var i = 0;i < del.length(); i++) {
        var o = del.get(i);
        if (isSvgElement(o.node.nodeName)) {
            o.selectize(false)
                .resize('stop');
            o.node.remove();
        }
    }
});

//旋转元素
function rotate(arc) {
    var del = SVG.select('.selected');
    for (var i = 0;i < del.length(); i++) {
        var o = del.get(i);
        if (isSvgElement(o.node.nodeName)) {
            o.transform({
                rotation : arc, // 旋转角度
                relative : true // 相对当前位置旋转
            });
            limiteDragArea(o);
        }
    }
}

// 左旋90度
$("#rotateLeft").on("click", function () {
    rotate(-90);
});

// 右旋90度
$("#rotateRight").on("click", function () {
    rotate(90);
});

//点击元素选中，其他元素清除选中效果
function selectClicked(o) {
    clearAllSelected();
    // 选中点击的元素
    o.selectize()
        .resize();
    o.addClass('selected');
}

//清除所有元素选中效果
function clearAllSelected() {
    // 获取svg孩子元素，孩子节点.node才是
    var c = svg.children();

    //判断孩子节点是否是图形元素，如果是去除选中
    for (var i = 0; i < c.length; i++) {
        //console.log("sad" + children[0])
        var child = c[i];
        if (isSvgElement(child.node.nodeName)) { // child 是SVG实例，child.node 是dom实例
            child.selectize(false)               // 使用child.node.instance 返回SVG实例
                .resize('stop');
            child.removeClass('selected');
        } else if (child.children.length !== 0) {
            var c = child.children;
            for (var j = 0; j < c.length; j++) {
                if (isSvgElement(c[j].nodeName)) {
                    c[j].selectize(false)
                        .resize('stop');
                    child.removeClass('selected');
                }
            }
        }
    }
}

// 点击非图片区域取消选中
function clickNonEleToClear(e) {
    if (e.target.nodeName === "svg") {
        clearAllSelected();
    }
}

//拖放区域限制
function limiteDragArea(o) {
    // 首先获取svg的范围
   /* var b = svg.viewbox();
    var width = b.width,
        height = b.height;

    // 移动区域
    var opt = {
        minX: 0,
        minY: 0,
        maxX: width,
        maxY: height
    };
*/
    // 可拖放，可缩放
    o.draggable();
    return o;
}

// 图形点击可以调整大小
function myResize(o) {

    //定义图形原始位置
    o.move(100, 100) // 图形原始位置

    // 清除所有选中效果
    clearAllSelected();

    // 限制移动范围
        limiteDragArea(o)
            .selectize()
        .resize();   // 可缩放

    // 被点击的图形添加selected属性，标记被选中
    o.addClass('selected');

    // 为元素添加点击事件
    selectClickedEle(o);
}

// 点击选中
function selectClickedEle(o) {
    o.click(function () {
        selectClicked(o);
    });
}

// 新增直线
$("#line").on("click", function () {
    if(svg === null) {
        return;
    }
    // 创建线条对象
    var line = svg.line(100, 100, 200, 100)
        .stroke({
            width : 1
        });
    // 可拖放,可缩放
    myResize(line);
});

//新增文本
$("#text").on("click", function () {
    if(svg === null) {
        return;
    }
    var text = svg.text('text');
    text.font({
        fill : "black",
        family: 'Inconsolata',
        size : 30
    });
    myResize(text);
});

//新增矩形
$("#rect").on("click", function () {
    if(svg === null) {
        return;
    }
    var rect = svg.rect(100, 200)
       .attr("fill", "red");
    myResize(rect);
});

//新增圆角矩形
$("#roundRect").on("click", function () {
    if(svg === null) {
        return;
    }
    var roundRect = svg.rect(100, 200)
        .radius(10)
        .attr("fill", "red");
    myResize(roundRect);
});

//新增五边形
$("#pentagon").on("click", function () {
    if(svg === null) {
        return;
    }
    var path = '100,10 190,80 150,180 50,180 10,80',
        fill = 'red';
    var pentagon = polygonGraph(path, fill);
    myResize(pentagon);
});

//新增五角星
$('#pentagram').on('click', function () {
    if(svg === null) {
        return;
    }
    var path = '147.6,265.5 177,281 171.4,248.2 195.1,225.1 162.3,220.3 ' +
        '147.6,190.5 132.9,220.3 100,225.1 123.8,248.2 118.2,281',
        fill = 'yellow';
    var pentagram = polygonGraph(path, fill);
    myResize(pentagram);
});

//新增六边形
$("#hexagon").on("click", function () {
    if(svg === null) {
        return;
    }
    var path = '37.5,129 0,65 37.5,0 112.3,0 150,65 112.3,129',
        fill = 'red';
    var hexagon = polygonGraph(path, fill);
    myResize(hexagon);
});

//新增八边形
$("#octagon").on("click", function () {
    if(svg === null) {
        return;
    }
    var path = '50,0 120,0 170,50 170,120 120,170 50,170 0,120 0,50',
    fill = 'red';
    var octagon = polygonGraph(path, fill);
    myResize(octagon);
});

//新增圆形
$("#circle").on("click", function () {
    if(svg === null) {
        return;
    }
    var circle = svg.circle(100, 200)
        .attr("fill", "red");
    myResize(circle);
});

// 新增椭圆
$("#ellipse").on("click", function () {
    if(svg === null) {
        return;
    }
    var ellipse = svg.ellipse(200, 100)
        .attr("fill", "red");
    myResize(ellipse);
});

//新增三角形
$("#tangle").on("click", function () {
    if(svg === null) {
        return;
    }
    var path = '30,0 60,50 0,50',
        fill = 'green';
    var polygon = polygonGraph(path, fill);
    myResize(polygon);
});

// svg polygon绘制图形
function polygonGraph(path, fill) {
    return svg.polygon(path)
        .fill(fill)
        .stroke({
            width : 1
        });
}

// 新增工业控制图形
// 新增管道
$('#pipe').on('click', function () {
    if(svg === null) {
        return;
    }
    var rect = svg.rect(80, 100);
    var gradient = svg.gradient('linear', function(stop) {
        stop.at(0, 'gray')
        stop.at(0.8, 'white')
        stop.at(1, 'gray')
    });
    // 渐变起止区域
//gradient.from(0,'100%').to('50%', '100%')
    rect.attr({ fill: gradient });
    myResize(rect);
});

// 新增灯
$('#light').on('click', function () {
    if(svg === null) {
        return;
    }
    var light = svg.path('M79.4,67.8c0-18.4-16.9-33.2-37.7-33.2S4,49.4,4,67.8' +
        'c0,7.3,2.7,14,7.1,19.4c0.5,0.7,15.5,21.9,16.7,30.8c1.3,9.1,' +
        '1.3,11.5,1.3,11.5h25.2c0,0,0-2.4,1.3-11.5c1.3-8.9,16.2-30,16.7-30.8'+
        'C76.8,81.8,79.4,75.1,79.4,67.')
        .fill('green')
        .stroke({
            color: 'white',
            width: 4,
            linecap: 'round',
            linejoin: 'round'
        });
    myResize(light);
});