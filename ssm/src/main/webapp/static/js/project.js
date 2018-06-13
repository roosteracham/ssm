/*
*  操作工程
* */

$(function () {

    // 模态框弹出前执行
    $('#myModal').on('shown.bs.modal', function () {

        // 清空模态框内容
        var $modal = $('#modal');
        $modal.empty();
        var child;
        // 新建工程
        if (id === 'newProject') {
            child = $("<input>", {
                type:'text',
                val:'工程名称',
                id : 'newInput',
                function:function(){
                    $(this).addClass('form-control');
                }
            });
        } else if (id === 'newSvg') { // 新建画面
            child = $("<input>", {
                type:'text',
                val:'画面名称',
                id : 'newInput',
                function:function(){
                    $(this).addClass('form-control');
                }
            });
        }
        $modal.append(child);
    });

    // 判定是否是点击的模态框的确定按钮
    var isConfirmNew = false;

    // 模态框关闭前执行
    $('#myModal').on('hide.bs.modal', function () {

        if (isConfirmNew) {

            // 获取输入框内容
            var name = $('#newInput').val(); //this.childNodes[1].childNodes[1].childNodes[3].childNodes[0].value;
            if (id === 'newProject') {  // 新建工程
                projectName = name;
                clearAllSelected();
                svg = null;
            } else if (id === 'newSvg') { // 新建画面
                    svgName = name;
                    newSVG();
            }
        }
        isConfirmNew = false;
    });

    // 新建工程和新建画面
    $('#confirmNewPro').click(function () {
        isConfirmNew = true;
        // 关闭模态框
        $('#myModal').modal('hide');
    });
});

// 工程名称
var projectName = null;

// 画面名称
var svgName = null;

// 判断时新建工程还是新建画面
var id = null;

// 新建工程
$("#newProject").on("click", function (e) {

    // id 被点击按钮的id
    id = e.target.id;

    // 显示模态框
    $('#myModal').modal('show');
});

// 新建画面
$("#newSvg").on("click", function (e) {

    // id 被点击按钮的id
    id = e.target.id;
    if (projectName === null) {
        alert('未创建工程，无法新建画面');
        return;
    } else {
        $('#myModal').modal('show');
    }
});

// 清除工程
$("#deletesvg").on("click", function () {
    svg.clear();
    svg = null;
    $("#svgContainer svg").remove();
});

// 保存工程 提交给服务器
$('#exportProject').on('click', function () {
    if (svgName === null || projectName === null) {
        return;
    }
    // 导出之前清除选中状态
    clearAllSelected();

    // 需要导出的数据都在 data 里面
    var data = {
        "projectName" : projectName,
        "svgName" : svgName,
        "svg" : svg.svg()
    };

    // 上传到服务器
    ajaxOption(urls.exportProject, 'post', data);
    //ajaxPost(urls.exportProject, data);
    //console.log(data)
});

// 导入工程， 向服务器请求
$('#importProject').on('click', function () {
    if (svg !== null) {

    } else {
        alert('工程未建立，无法导入');
    }
});

// 导出SVG
$('#exportSVG').on('click', function () {

    var eles = SVG.select('.ele');
    var data;
    for (var i = 0; i < eles.length(); i++) {
        var ele = eles.get(i);
        data = ele.svg();
        //
        console.log(data)
    }

});

// 导入SVG
$('#importSVG').on('click', function () {
    if (svg !== null) {

    } else {
        alert('工程未建立，无法导入');
    }
});

// 新建画面
function newSVG() {

        // 新建svg元素
        svg = SVG("svgContainer").size("100%", "100%");
        // 为svg添加点击事件， 点击非图形元素时取消选中效果
        svg.click(clickNonEleToClear);

        //svg 添加鼠标按下事件
        svg.mousedown(mousedownOnNonEle);

        //svg 添加鼠标弹起事件
        svg.mouseup(mouseupOnSVG);

        //svg 添加鼠标移动事件
        svg.mousemove(mouseoverOnSVG);

        // 放到localstorage 里面
        //loc
}