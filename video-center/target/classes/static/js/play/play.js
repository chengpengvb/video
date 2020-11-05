function UUID() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}
function isIE() {
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
		return true;
	}
	return false;
}
(function(jQuery) {
	function fullScreen(state) {
		var e, func, doc;

		if (!this.length)
			return this;

		e = (this[0]);

		if (e.ownerDocument) {
			doc = e.ownerDocument;
		} else {
			doc = e;
			e = doc.documentElement;
		}

		if (state == null) {
			if (!((doc["exitFullscreen"]) || (doc["webkitExitFullscreen"])
					|| (doc["webkitCancelFullScreen"])
					|| (doc["msExitFullscreen"]) || (doc["mozCancelFullScreen"]))) {
				return null;
			}

			state = fullScreenState(doc);
			if (!state)
				return state;

			return (doc["fullscreenElement"])
					|| (doc["webkitFullscreenElement"])
					|| (doc["webkitCurrentFullScreenElement"])
					|| (doc["msFullscreenElement"])
					|| (doc["mozFullScreenElement"]) || state;
		}

		if (state) {
			func = (e["requestFullscreen"]) || (e["webkitRequestFullscreen"])
					|| (e["webkitRequestFullScreen"])
					|| (e["msRequestFullscreen"])
					|| (e["mozRequestFullScreen"]);
			if (func) {
				func.call(e);
			}
			return this;
		} else {
			func = (doc["exitFullscreen"]) || (doc["webkitExitFullscreen"])
					|| (doc["webkitCancelFullScreen"])
					|| (doc["msExitFullscreen"])
					|| (doc["mozCancelFullScreen"]);
			if (func && fullScreenState(doc))
				func.call(doc);
			return this;
		}
	}

	function fullScreenState(doc) {
		return !!(doc["fullscreenElement"] || doc["msFullscreenElement"]
				|| doc["webkitIsFullScreen"] || doc["mozFullScreen"]);
	}

	function toggleFullScreen() {
		return (fullScreen.call(this, !fullScreen.call(this)));
	}

	function fullScreenChangeHandler(event) {
		jQuery(document).trigger(new jQuery.Event("fullscreenchange"));
	}

	function fullScreenErrorHandler(event) {
		jQuery(document).trigger(new jQuery.Event("fullscreenerror"));
	}

	function installFullScreenHandlers() {
		var fullscreenchange = 'webkitfullscreenchange mozfullscreenchange fullscreenchange MSFullscreenChange';
		$(document).on(fullscreenchange, function(event) {
		});
		var fullscreenerror = 'webkitfullscreenerror mozfullscreenerror fullscreenerror MSFullscreenError';
		$(document).on(fullscreenerror, function(event) {
		});
	}

	jQuery.fn["fullScreen"] = fullScreen;
	jQuery.fn["toggleFullScreen"] = toggleFullScreen;
	installFullScreenHandlers();

})(jQuery);

function wsConnect(host, clientId, times) {
	if (!times) {
		times = 3;
	}
	var topObj = {
		videos : {},
		clientId : clientId,
		connectTimes : 0
	};

	function closeByUrl(data) {
		for ( var key in topObj.videos) {
			topObj.videos[key]._closeByUrl(data);
		}
	}

	function protocolOrder() {
		var iesup = isIE();
		var protocol = 0;
		if (iesup) {
			protocol = 1;
		}
		var order = {
			order : 2,
			protocol : protocol
		}
		return order;
	}

	var wsObj = {};
	wsObj.init = function() {
		var wss = null;
		var ishttps = 'https:' == document.location.protocol ? true : false;
		if (ishttps) {
			wss = new WebSocket("wss://" + host + "/video/" + clientId);
		} else {
			wss = new WebSocket("ws://" + host + "/video/" + clientId);
		}
		wsObj.wss = wss;
		wss.onopen = function(evt) {
			console.log("WS Connection Success ...");
			connectTimes = 0;
			wss.send(JSON.stringify(protocolOrder()));
		};
		wss.onmessage = function(event) {
			var data = JSON.parse(event.data);
			if (data.order == 2 && data.obj.clientId == clientId) {
				closeByUrl(data);
				return;
			}
			var video = topObj.videos[data.callbackId];
			if (video) {
				if (data.order == 0) {
					video._toPlay(data);
				} else if (data.order == 1) {
					video._close(data);
				} else if (data.order == 2) {
					console.log(data.lines);
				}
			} else {
				console.log(data.callbackId + ":对象不存在!");
			}
		};

		wss.onclose = function(evt) {
			if (connectTimes >= times) {
				return;
			}
			connectTimes++;
			onClose();
		};

	}

	function onClose() {
		console.log("WS Disconnected, trying to reconnect...");
		wsObj.init();
	}

	wsObj.init();

	topObj.set = function(key, video) {
		topObj.videos[key] = video;
	}

	topObj.wsObj = wsObj;
	return topObj;
}

$.fn
		.extend({

			video : function(wssConn) {
				var dom = this;
				var topObj = {
					key : UUID(),
					width : $(dom).width(),
					height : $(dom).height(),
					playerStatus : 1,
					voice : 0,
					theme : null,
					closeStatus : false,
					mute : false,
					firstVideo : false,
					isIEState : false,
					_doClose : false
				};
				topObj.isIEState = isIE();
				topObj.cid = "c_" + topObj.key;

				wssConn.set(topObj.key, topObj);

				topObj._closeByUrl = function(data) {
					var url = topObj.url;
					if (!url) {
						return;
					}
					if (topObj.url == data.obj.url && !topObj._doClose) {
						delete wssConn.videos[topObj.key];
						alert("打开视频[" + topObj.url + "]流失败，请查看控制台");
						console.log(data.lines);
					}
					if (url == data.obj.url && !topObj.closeStatus
							&& !topObj.firstVideo) {
						topObj.closeStatus = true;
						topObj.close();
					}
				}

				function ckClose() {
					topObj.firstVideo = true;
					// 加载完成后回到
					if (topObj.closeStatus) {
						topObj.close();
						if (!topObj.isIEState) {
							topObj.jsmpeg.destroy();
						} else {
							topObj.player.videoClear();
						}
					}
				}

				topObj._toPlay = function(data) {
					if (topObj.openCallback) {
						topObj.openCallback(data.ok)
					}
					if (!data.ok) {
						alert(data.msg);
						return;
					}
					var obj = data.obj;
					var canvas = document.getElementById(topObj.cid);

					var ishttps = 'https:' == document.location.protocol ? true
							: false;
					var url = null;
					if (ishttps) {
						url = "wss://" + obj.wsIp + ":" + obj.wsPort + "/"
								+ obj.theme;
					} else {
						url = "ws://" + obj.wsIp + ":" + obj.wsPort + "/"
								+ obj.theme;
					}
					topObj.theme = obj.theme;
					topObj.browser = getExplorerInfo();
					if (topObj.isIEState) {
						url = "rtmp://" + obj.wsIp + ":" + obj.wsPort + "/"
								+ obj.theme;
						var videoObject = {
							width : topObj.width,
							height : topObj.height + 150,
							playerID : topObj.cid,
							container : "#" + topObj.cid,
							variable : "player",
							autoplay : true,
							live : true,
							volume : 0,
							loaded : 'loadedHandler',
							video : url,
						};
						topObj.player = new ckplayer(videoObject);
						if (!topObj.firstVideo) {
							ckClose();
						}

					} else {
						var jsmpeg = new JSMpeg.Player(url, {
							canvas : canvas,
							onSourceEstablished : ckClose
						});
						topObj.jsmpeg = jsmpeg;
						topObj.jsmpeg.volume = 0;
					}
				}
				topObj._close = function(data) {
					topObj._doClose = true;
					if (topObj.firstVideo) {
						delete wssConn.videos[topObj.key];
						if (topObj.isIEState && topObj.videoPlay) {
							topObj.videoPlay.dispose()
						}
						if (!data.ok) {
							console.log(data.msg)
							return;
						}
						console.log("关闭视频流[" + data.obj + "]成功");
					}
				}

				topObj.close = function() {
					if (topObj.firstVideo) {
						var order = {
							callbackId : topObj.key,
							order : 1,
							payload : {
								clientId : wssConn.clientId,
								theme : topObj.theme
							}
						};
						wssConn.wsObj.wss.send(JSON.stringify(order));
					} else {
						topObj.closeStatus = true;
					}
				}

				topObj.play = function(option, openCallback) {
					var order = {
						callbackId : topObj.key,
						order : 0,
						payload : {
							clientId : wssConn.clientId,
							url : option.url,
							userName : option.userName,
							passWord : option.passWord,
							width : option.width,
							height : option.height,
							type : option.type
						}
					}
					topObj.openCallback = openCallback;
					topObj.url = option.url;
					wssConn.wsObj.wss.send(JSON.stringify(order));
				}

				function initHtml() {
					$(dom).html(null);
					var htmls = [];
					htmls.push("<canvas id='" + topObj.cid + "'");
					htmls.push("width='" + topObj.width + "' height='");
					htmls.push(topObj.height + "' style='width:" + topObj.width
							+ "px;height:" + topObj.height + "px'></canvas>");

					htmls.push("<div class='play_tools' id='tools_");
					htmls.push(topObj.cid + "'>");

					htmls.push("<i title='暂停' class='v-play fa fa-pause'");
					htmls.push(" aria-hidden='true'></i>");

					htmls.push("<div class='v-voice'>");

					htmls.push("<i title='静音' class='v-v-mute fa fa-square'");
					htmls.push(" aria-hidden='true'></i>")

					htmls.push("<i title='减小音量' class='v-v-sub fa fa-minus'");
					htmls.push(" aria-hidden='true'></i>");

					htmls.push("<label>0%</label>");

					htmls.push("<i title='增大音量' class='v-v-add fa fa-plus'");
					htmls.push(" aria-hidden='true'></i>");

					htmls.push("<i title='全屏' class='v-full fa fa-arrows-alt'");
					htmls.push(" aria-hidden='true'></i>");

					htmls.push("</div>");

					htmls.push("</div>");

					$(dom).append(htmls.join(""));
				}

				function initIEHtml() {
					$(dom).html(null)
					var htmls = [];
					htmls.push('<div class="Ckplayer" style="width:'
							+ topObj.width + 'px;height:' + topObj.height
							+ 'px;"><div id="' + topObj.cid + '" style="width:'
							+ topObj.width + 'px;height:' + topObj.height
							+ 'px;"  width="' + topObj.width + '" height="'
							+ topObj.height + '" ref="video_ck"></div>	</div>')
					$(dom).html(htmls.join(""));
				}

				function switchPlay(event) {
					var target = event.target;
					if (topObj.playerStatus == 1) {
						topObj.jsmpeg.pause();
						topObj.playerStatus = 0;
						$(target).removeClass("fa-pause");
						$(target).addClass("fa-play");
						$(target).attr("title", "播放");
					} else {
						topObj.jsmpeg.play();
						topObj.playerStatus = 1;
						$(target).removeClass("fa-play");
						$(target).addClass("fa-pause");
						$(target).attr("title", "暂停");
					}
				}
				var version = [ "81.0.4044.92", "74.0.3729.131",
						"77.0.3865.75", "61.0.3163.79" ]
				function exitFullscreen() {
					if (topObj.browser && topObj.browser.type == "Chrome"
							&& topObj.openSreen) {
						var h = window.screen.height
						$("#" + topObj.cid).height(topObj.initHeight)
						$("#" + topObj.cid).width(topObj.initWidth)
						topObj.openSreen = false;
					} else {
						if ($("#" + topObj.cid).exitFullscreen) {
							$("#" + topObj.cid).exitFullscreen();
						} else if ($("#" + topObj.cid).mozCancelFullScreen) {
							$("#" + topObj.cid).mozCancelFullScreen();
						} else if ($("#" + topObj.cid).webkitExitFullscreen) {
							$("#" + topObj.cid).webkitExitFullscreen();
						}
					}

				}

				function launchIntoFullscreen(element) {

					if (topObj.browser != null
							&& topObj.browser.type == "Chrome") {
						topObj.openSreen = true;
						topObj.initWidth = $("#" + topObj.cid).width()
						topObj.initHeight = $("#" + topObj.cid).height()
						var h = window.screen.height
						var w = window.screen.width
						$("#" + topObj.cid).width(w)
						$("#" + topObj.cid).height(h)
					}
					if (element.requestFullscreen) {
						element.requestFullscreen();
					} else if (element.mozRequestFullScreen) {
						element.mozRequestFullScreen();
					} else if (element.webkitRequestFullscreen) {
						element.webkitRequestFullscreen();
					} else if (element.msRequestFullscreen) {
						element.msRequestFullscreen();
					}
					window.onresize = function() {
						if (!checkFull()) {
							doEsc();
						}
					}
				}

				function getExplorerInfo() {
					var explorer = window.navigator.userAgent.toLowerCase();
					// ie
					if (explorer.indexOf("msie") >= 0) {
						var ver = explorer.match(/msie ([\d.]+)/)[1];
						return {
							type : "IE",
							version : ver
						};
					}
					// firefox
					else if (explorer.indexOf("firefox") >= 0) {
						var ver = explorer.match(/firefox\/([\d.]+)/)[1];
						return {
							type : "Firefox",
							version : ver
						};
					}
					// Chrome
					else if (explorer.indexOf("chrome") >= 0) {
						var ver = explorer.match(/chrome\/([\d.]+)/)[1];
						return {
							type : "Chrome",
							version : ver
						};
					}
					// Opera
					else if (explorer.indexOf("opera") >= 0) {
						var ver = explorer.match(/opera.([\d.]+)/)[1];
						return {
							type : "Opera",
							version : ver
						};
					}
					// Safari
					else if (explorer.indexOf("Safari") >= 0) {
						var ver = explorer.match(/version\/([\d.]+)/)[1];
						return {
							type : "Safari",
							version : ver
						};
					}
				}
				function fullScreen(event) {
					$("#" + topObj.cid).fullScreen(true);
				}
				function doEsc() {
					exitFullscreen();
				}
				function setVoiceHtml() {
					var v_voice = '#tools_' + topObj.cid + " .v-voice label";
					if (parseInt(topObj.voice) > 100) {
						topObj.voice = 100
					}
					$(v_voice).html(parseInt(topObj.voice) + "%");
				}

				function checkFull() {
					return document.fullscreenElement
							|| document.msFullscreenElement
							|| document.mozFullScreenElement
							|| document.webkitFullscreenElement || false;

					/*
					 * if (isFull === undefined) isFull = false; return isFull;
					 */
				}
				function initEvent() {
					var v_playId = '#tools_' + topObj.cid + " .v-play";
					$(v_playId).unbind("click").bind('click', function(event) {
						switchPlay(event);
					})

					var v_fullId = '#tools_' + topObj.cid + " .v-full";
					$(v_fullId).unbind("click").bind(
							'click',
							function(event) {
								launchIntoFullscreen(document
										.getElementById(topObj.cid))
								// fullScreen(event);
							})

					var v_addId = '#tools_' + topObj.cid + " .v-v-add";
					$(v_addId).unbind("click").bind('click', function(event) {
						if (topObj.mute) {
							return;
						}
						var oldVolume = topObj.jsmpeg.volume;
						if (topObj.voice >= 100) {
							return;
						}
						oldVolume = oldVolume + 0.13

						topObj.jsmpeg.volume = oldVolume;
						topObj.voice = topObj.voice += 10;
						if (topObj.voice > 100) {
							topObj.voice == 100
						}
						setVoiceHtml();
					})

					var v_sub = '#tools_' + topObj.cid + " .v-v-sub";
					$(v_sub).unbind("click").bind('click', function(event) {
						if (topObj.mute) {
							return;
						}
						var oldVolume = topObj.jsmpeg.volume;
						if (topObj.voice == 0) {
							topObj.jsmpeg.volume = 0
							return;
						}
						topObj.voice = topObj.voice -= 10;
						if (topObj.voice == 0) {
							topObj.jsmpeg.volume = 0
						} else {
							topObj.jsmpeg.volume = oldVolume - 0.1;
						}
						setVoiceHtml();
					});

					var v_mute = '#tools_' + topObj.cid + " .v-v-mute";
					$(v_mute).unbind("click").bind('click', function(event) {
						var target = event.target;
						if (topObj.mute == false) {
							topObj.voice = topObj.jsmpeg.volume;
							topObj.jsmpeg.volume = 0;
							topObj.mute = true;
							$(target).removeClass("fa-square");
							$(target).addClass("fa-ban");
						} else {
							topObj.jsmpeg.volume = topObj.voice;
							topObj.voice = topObj.voice *= 100;
							setVoiceHtml();
							topObj.mute = false;
							$(target).removeClass("fa-ban");
							$(target).addClass("fa-square");
							setVoiceHtml();
						}
					})
				}

				function initMain() {
					if (!topObj.isIEState) {
						initHtml();
						initEvent();
					} else {
						initIEHtml()
					}
				}
				initMain();
				return topObj;
			}
		})
