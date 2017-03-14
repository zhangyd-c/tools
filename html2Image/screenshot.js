/**
 * Created by zhyd on 2017-03-05.
 */
$(document).ready(function() {
    /*生成图片*/
    $("#generate").on("click", function(event) {
        event.preventDefault();
        var $screenshotDom = $(".article");
        html2canvas($screenshotDom, {
            height: $screenshotDom.outerHeight() + 20,
            width: $screenshotDom.outerWidth() + 20
        }).then(function(canvas) {
            //保存生成的图片（base64字符串）供预览使用
            var dom = '<p><br></p><a href="' + canvas.toDataURL() + '"class="base64Image">' + '<img src="' + canvas.toDataURL() + '">' + '</a>';
            $("body").append(dom);
        });
    });

    /*下载*/
    $("#download").on("click", function() {
        var $this = $(".base64Image");
        var imageUrl = $this.attr("href");
        var title = document.title;
        var a = $("<a></a>").attr("href", imageUrl).attr("download", title + ".png").appendTo("body");
        a[0].click();
        a.remove();
    })
});