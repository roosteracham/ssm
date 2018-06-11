/*
*  操作工程
* */
// 清除工程
$("#deletesvg").on("click", function () {
    svg.clear();
    svg = null;
    $("#svgContainer svg").remove();
});
