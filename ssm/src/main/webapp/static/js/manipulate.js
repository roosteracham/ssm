/*
*  操作图形
* */
// 清除工程元素
$("#clearElements").on("click", function () {
    if(svg !== null) {
        svg.clear();
    }
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

//组合元素
$('#groupEle').on('click', function () {

    var group = svg.group()
        .addClass('grouparent')
        .addClass('ele');
    var eles = SVG.select('.selected');
    for (var i = 0; i < eles.length(); i++) {

        var ele = eles.get(i);
        ele.draggable(false)
            .selectize(false)
            .resize('stop')
            .addClass('groupEle')
            .removeClass('ele');
        group.add(ele);
    }
    group.draggable()
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

// 旋转
// 左旋90度
$("#rotateLeft_90").on("click", function () {
    rotate(-90);
});

// 右旋90度
$("#rotateRight_90").on("click", function () {
    rotate(90);
});

// 左旋45度
$("#rotateLeft_45").on("click", function () {
    rotate(-45);
});

// 右旋45度
$("#rotateRight_45").on("click", function () {
    rotate(45);
});

// 左旋30度
$("#rotateLeft_30").on("click", function () {
    rotate(-30);
});

// 右旋30度
$("#rotateRight_30").on("click", function () {
    rotate(30);
});

// 图形层次变换
// 图形置最底层
$('#backLayer').on('click', function () {

    // 只有一个图形被选中才能够置底层
    var eles = SVG.select('.selected');
    if (eles.length() === 1) {

        // 清除选中状态
        clearAllSelected();
        eles.get(0).back();
    }
});

// 图形置向下一层
$('#backwardLayer').on('click', function () {

    // 只有一个图形被选中才能够置向下一层
    var eles = SVG.select('.selected');
    if (eles.length() === 1) {

        // 清除选中状态
        clearAllSelected();
        eles.get(0).backward();
    }
});

// 图形置最顶层
$('#frontLayer').on('click', function () {

    // 只有一个图形被选中才能够置顶层
    var eles = SVG.select('.selected');
    if (eles.length() === 1) {

        // 清除选中状态
        clearAllSelected();
        eles.get(0).front();
    }
});

// 图形置向上一层
$('#forwardLayer').on('click', function () {

    // 只有一个图形被选中才能够置向上一层
    var eles = SVG.select('.selected');
    if (eles.length() === 1) {

        // 清除选中状态
        clearAllSelected();
        eles.get(0).forward();
    }
});

// 改变颜色,弹出颜色选择器
$('#changeColor').on('click', function (e) {
    document.getElementById('colorPicker').jscolor.show()
});

// 改变图形的颜色， 修改fill属性
$('#colorPicker').on('change', function () {
    var c = '#' + document.getElementById('colorPicker').value;
    // 只有一个图形被选中才能够置向上一层
    var eles = SVG.select('.selected');
    if (eles.length() === 1) {
        eles.get(0).fill(c);
    }
});

// 保存
var selectedEle = null;

// 绑定测点
$('#bindPoint').on('click', function () {

    var eles = SVG.select('.selected');
    if (eles.length() === 1) {
        selectedEle = eles.get(0);
    }

    // show 模态框
    $('#myBindPointModal').modal('show');
});

// 运行
$('#point').on('click', function () {

    // 被选中的元素
    /*var eles = SVG.select('.bindPoint');

    for (var i = 0; i < eles.length(); i++) {
        var ele = eles.get(i);
        console.log(ele.data('pointName'), ele.data('type') , ele.data('value'))
    }*/
});