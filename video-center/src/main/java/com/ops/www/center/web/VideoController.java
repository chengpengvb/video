package com.ops.www.center.web;

import static com.ops.www.center.util.OrderConstants.ORDER_CLOSE;
import static com.ops.www.center.util.OrderConstants.ORDER_PLAY;
import static com.ops.www.center.util.OrderConstants.ORDER_CLOSE_CALL;
import static com.ops.www.center.util.OrderConstants.ORDER_UNDEFINED;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ops.www.center.service.CenterService;
import com.ops.www.center.web.websocket.DefaultWebsocketHandle;
import com.ops.www.common.dto.OnCloseCallBack;
import com.ops.www.common.dto.Order;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.common.dto.ResultModel;
import com.ops.www.common.util.StringUtils;

/**
 * webSocket接口部分
 * 
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午3:01:25
 * 
 */

@ServerEndpoint("/video/{uuid}")
@Component
public class VideoController extends DefaultWebsocketHandle implements OnCloseCallBack {

	private static CenterService centerService;

	private byte protocol = PlayConfig.PROTOCOL_TRSP;

	@Override
	public void closeHandle(Session session) {
		String clientId = getUUID(session);
		if (clientId != null) {
			centerService.close(clientId, protocol);
			centerService.deleteCallBack(clientId);
			logger.debug("Close {}.", clientId);
		}
	}

	@Override
	public void openHandle(Session session, String uuid) {
		centerService.registCallBack(uuid, this);
		logger.debug("New ClientId [{}].", uuid);
	}

	@Override
	public void doOnClose(PlayConfig playConfig, String lines) {
		String clientId = playConfig.getClientId();
		Session session = getSession(clientId);
		if (session == null) {
			return;
		}
		ResultModel resultModel = new ResultModel("视频流关闭", ResultModel.CODE_SUCCESS, playConfig)
				.setOrder(ORDER_CLOSE_CALL);
		resultModel.setLines(lines);
		sendMsg(resultModel, session);
	}

	private void doPlay(JSONObject parse, Session session) {
		String callbackId = parse.getString("callbackId");
		JSONObject object = (JSONObject) parse.get("payload");
		PlayConfig playConfig = object.toJavaObject(PlayConfig.class);
		playConfig.setProtocol(protocol);
		String url = playConfig.getUrl();
		if (StringUtils.isBlank(url) || !url.contains("rtsp:")) {
			ResultModel resultModel = new ResultModel("URL非法", ResultModel.CODE_ERROR, null).setOrder(ORDER_PLAY)
					.setCallbackId(callbackId);
			sendMsg(resultModel, session);
			return;
		}
		PlayResult result = centerService.playVideo(callbackId, playConfig);
		if (result == null) {
			ResultModel resultModel = new ResultModel("调用失败", ResultModel.CODE_ERROR, null).setOrder(ORDER_PLAY)
					.setCallbackId(callbackId);
			sendMsg(resultModel, session);
			return;
		}
		ResultModel resultModel = new ResultModel("调用成功", ResultModel.CODE_SUCCESS, result).setOrder(ORDER_PLAY)
				.setCallbackId(callbackId);
		sendMsg(resultModel, session);
	}

	private void doClose(JSONObject parse, Session session) {
		String callbackId = parse.getString("callbackId");
		JSONObject jsonObject = (JSONObject) parse.get("payload");
		String clientId = jsonObject.getString("clientId");
		String theme = jsonObject.getString("theme");
		boolean falg = centerService.close(callbackId, clientId, theme, protocol);
		int code = falg ? ResultModel.CODE_SUCCESS : ResultModel.CODE_ERROR;
		ResultModel resultModel = new ResultModel("调用成功", code, theme).setOrder(ORDER_CLOSE).setCallbackId(callbackId);
		sendMsg(resultModel, session);
	}

	private void doOther(JSONObject parse, Session session) {
		String callbackId = parse.getString("callbackId");
		ResultModel resultModel = new ResultModel("未定义的方法", false, null).setOrder(ORDER_UNDEFINED)
				.setCallbackId(callbackId);
		sendMsg(resultModel, session);
	}

	@Override
	public void onMessageHandle(String message, Session session) {
		JSONObject parse = (JSONObject) JSONObject.parse(message);
		int order = parse.getIntValue("order");
		switch (order) {
		case Order.ORDER_PLAY:
			doPlay(parse, session);
			return;
		case Order.ORDER_CLOSE:
			doClose(parse, session);
			return;
		case Order.ORDER_SET_PROTOCOL:
			setProtocol(parse, session);
			return;
		default:
			doOther(parse, session);
			return;
		}
	}

	private void setProtocol(JSONObject parse, Session session) {
		String protocol = parse.getString("protocol");
		if (protocol == null) {
			return;
		}
		this.protocol = Byte.parseByte(protocol);
	}

	@Autowired
	public void setCenterService(CenterService centerService) {
		VideoController.centerService = centerService;
	}

	private void sendMsg(ResultModel resultModel, Session session) {
		sendTextMsg(session, JSON.toJSONString(resultModel, SerializerFeature.DisableCircularReferenceDetect));
	}
}
