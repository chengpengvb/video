package com.ops.www.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ops.www.common.dto.Config2Result;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.module.PlayManager;

/**
 * @author 作者 cp
 * @version 创建时间：2020年8月26日 上午9:27:34
 * 
 */
@Controller
@RequestMapping("/agent")
public class ApiCenter {

	@Value(value = "${host}")
	private String localHost;

	@Value(value = "${server.port}")
	private int localPort;

	@Autowired
	@Qualifier("rtspPlayManager")
	private PlayManager rtspPlayManager;

	@Autowired
	@Qualifier("rtmpPlayManager")
	private PlayManager rtmpPlayManager;

	@RequestMapping(value = "/close")
	@ResponseBody
	public boolean close(@RequestParam Map<String, Object> paramMap) {
		String protocolStr = paramMap.get("protocol").toString();
		String clientId = paramMap.get("clientId").toString();
		Byte protocol = Byte.parseByte(protocolStr);
		switch (protocol) {
		case PlayConfig.PROTOCOL_TRSP:
			return rtspPlayManager.close(clientId);
		case PlayConfig.PROTOCOL_TRMP:
			return rtmpPlayManager.close(clientId);
		default:
			return true;
		}
	}

	@RequestMapping(value = "/closeByTheme")
	@ResponseBody
	public boolean closeByTheme(@RequestParam Map<String, Object> paramMap) {
		String protocolStr = paramMap.get("protocol").toString();
		String theme = paramMap.get("theme").toString();
		String clientId = paramMap.get("clientId").toString();
		Byte protocol = Byte.parseByte(protocolStr);
		return _closeByTheme(clientId, theme, protocol);
	}

	private boolean _closeByTheme(String clientId, String theme, byte protocol) {
		switch (protocol) {
		case PlayConfig.PROTOCOL_TRSP:
			return rtspPlayManager.close(clientId, theme);
		case PlayConfig.PROTOCOL_TRMP:
			return rtmpPlayManager.close(clientId, theme);
		default:
			return true;
		}
	}

	@RequestMapping(value = "/closeAll")
	@ResponseBody
	public boolean closeAll() {
		List<Config2Result> selectConfig2Result = rtspPlayManager.selectConfig2Result();
		for (Config2Result config2Result : selectConfig2Result) {
			PlayResult playResult = config2Result.getPlayResult();
			PlayConfig playConfig = config2Result.getPlayConfig();
			String clientId = playConfig.getClientId();
			String theme = playResult.getTheme();
			byte protocol = playConfig.getProtocol();
			_closeByTheme(clientId, theme, protocol);
		}
		return true;
	}

	@RequestMapping(value = "/playVideo")
	@ResponseBody
	public PlayResult playVideo(@RequestParam Map<String, Object> paramMap) {
		PlayConfig playConfig = JSONObject.parseObject(paramMap.get("playConfig").toString(), PlayConfig.class);
		switch (playConfig.getProtocol()) {
		case PlayConfig.PROTOCOL_TRSP:
			return rtspPlayManager.playVideo(playConfig);
		case PlayConfig.PROTOCOL_TRMP:
			return rtmpPlayManager.playVideo(playConfig);
		default:
			return new PlayResult(null, 0, null, localHost, localPort);
		}
	}

}
