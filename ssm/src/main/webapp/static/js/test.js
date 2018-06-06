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

//点击元素选中，其他元素清除选中效果
function selectClicked(o) {
    clearAllSelected();
    // 选中点击的元素
    o.selectize()
        .resize();
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
                .resize('stop')
        } else if (child.children.length !== 0) {
            var c = child.children;
            for (var j = 0; j < c.length; j++) {
                if (isSvgElement(c[j].nodeName)) {
                    c[j].selectize(false)
                        .resize('stop')
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

// 图形点击可以调整大小
function myResize(o) {

    //定义图形原始位置
    o.move(100, 100) // 图形原始位置

    // 清除所有选中效果
    clearAllSelected();

    // 限制移动范围
    // 首先获取svg的范围
    var b = svg.viewbox();
    var width = b.width,
        height = b.height;
    // 移动区域
    var opt = {
            minX: 0,
            minY: 0,
            maxX: width,
            maxY: height
    };

    // 可拖放，可缩放
    o.draggable(opt) // 可拖放
        .selectize()
        .resize();   // 可缩放

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
    var polygon = svg.polygon('30,0 60,50 0,50')
        .fill('green')
        .stroke({
        width: 1
    });
    myResize(polygon);
});
