var topObj = {
	// 当前页面应用唯一标识，自定义
	clientId : UUID(),
	// 注意这个map在IE浏览器里面失效，可使用Object代替
	videoMap : new Map()
};

function openModal() {
	$("#videoConfigModal").modal("show");
}

function appendAndPlay() {
	var rtspUrl = $("#rtspUrl").val();
	if (!rtspUrl) {
		alert("请输入URL！");
		return;
	}
	if(!new RegExp("^RTSP://.*$").test(rtspUrl.toLocaleUpperCase())){
		alert("非法的URL请重新输入")
		return;
	}
	var domId = "div_" + UUID();
	var childId=findRowId()
	if(childId!=null){
		$("#"+childId).append(buildHtmls(domId));
	}else{
		var rowId = "div_" + UUID();
		$("#multiPlayDiv").append(buildRowHtml(rowId));
		$("#"+rowId+"_1").append(buildHtmls(domId));
	}
	playVideo(domId);
	$("#videoConfigModal").modal("hide");
}
function findRowId(){
	var childId=null
	
	$("#multiPlayDiv .sp").each(function(){
	     var id=$(this).attr("id");
	     var html=$("#"+id).html();
	     if(html==null||html.length==0){
	    	 childId=id;
	    	 return false;
	     }
	})
	return childId;
}
function buildRowHtml(rowId){
	var html=[];
	html.push('<div class="row" id='+rowId+'><div class="col-md-4 sp" id='+rowId+'_1></div>')
	html.push('<div class="col-md-4 sp" id='+rowId+'_2></div>')
	html.push('<div class="col-md-4 sp" id='+rowId+'_3></div></div>')
    return 	html.join("")
}

function playVideo(domId) {
	var rtspUrl = $("#rtspUrl").val();
	var rtspName = $("#rtspName").val();
	var rtspPassword = $("#rtspPassword").val();
	var decodType = $("#decodType").val();
	var video = $("#" + domId).video(topObj.wssConn);
	var option = {
		url : rtspUrl,
		userName : rtspName,
		passWord : rtspPassword,
		width : 800,
		height : 450,
		type : decodType
	};
	video.play(option);
	topObj.videoMap.set(domId, video);
}

function deletePlay(domId) {
	var video = topObj.videoMap.get(domId);
	if (video) {
		video.close();
		topObj.videoMap.delete(domId)
	}
	var id = "tol_" + domId;
	$("#" + id).remove();
}

function buildHtmls(domId) {
	var htmls = [];
	htmls.push("<div id='tol_" + domId + "'>");
	htmls.push("<div style='height: 20px'><label style='cursor: pointer;' onclick='deletePlay(\"" + domId
			+ "\")'>删除</label></div>");
	htmls.push("<div id='" + domId + "' ");
	htmls.push("style='width: 472px; height: 265px;margin: auto;'></div>");
	htmls.push("</div>");
	return htmls.join("");
}

function initMain() {
	topObj.wssConn = wsConnect(window.location.host, topObj.clientId);
}

initMain();

