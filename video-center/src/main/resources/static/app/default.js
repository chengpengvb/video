var topObj = {
	// 当前页面应用唯一标识，自定义
	clientId : UUID()
};

// 初始化一个连接对象(于后台交互)，单列
var wssConn = wsConnect(window.location.host, topObj.clientId);

function initEvent() {
	$('#videoPlayModal').on('hidden.bs.modal', function() {
		// 关闭视频流
		topObj.videoObj.close();
	})
}


function openAfter(isOk) {
	if (isOk) {
		$("#videoPlayModal").modal("show");
	}
}

function videoPlay() {
	var rtsp = $("#rtsp").val();
	var userName = $("#userName").val();
	var passWord = $("#passWord").val();
	if(!new RegExp("^RTSP://.*$").test(rtsp.toLocaleUpperCase())){
		alert("非法的URL请重新输入")
		return;
	}
	// 初始化一个视频流播放插件
	var video = $("#videoDiv").video(wssConn);
	var option = {
		url : rtsp,// rtsp流
		userName : userName,// rtsp流用户名
		passWord : passWord,// rtsp流密码
		width : 800,// 视频宽度
		height : 450,// 视频高度
		// 解码类型 0:cpu,1:qsv,2:cuda，查看agent端是否支持该解码方式，可能需要相关硬件
		type : 0
	};
	// 开始播放
	video.play(option, openAfter);
	topObj.videoObj = video;
}

initEvent();