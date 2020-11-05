package com.ops.www.module.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ops.www.common.dto.Config2Result;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.common.dto.ResultModel;
import com.ops.www.common.util.CallBack;
import com.ops.www.common.util.IdFactory;
import com.ops.www.common.util.PathUtil;
import com.ops.www.common.util.ProcessUtil;
import com.ops.www.common.util.ProcessUtil.ProcessInstance;
import com.ops.www.common.util.StringUtils;
import com.ops.www.module.PlayManager;
import com.ops.www.service.CenterService;
import com.ops.www.util.PidUtil;
import com.ops.www.util.cmd.PlayCmdRtsp;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午1:53:28
 * 
 */
@Component("rtspPlayManager")
public class RtspPlayManager implements PlayManager {

	private Logger logger = LogManager.getLogger();

	@Value(value = "${system.play.service.port:8081}")
	private int servicePort;

	@Value(value = "${system.play.ws.ip}")
	private String wsIp;

	@Value(value = "${system.play.ws.port:8082}")
	private int wsPort;

	@Value(value = "${system.play.ws.secret:supersecret}")
	private String supersecret;

	@Value(value = "${system.ffmpeg.timeOut:3}")
	private int timeOut;

	@Autowired
	private CenterService centerService;

	@Value(value = "${host}")
	private String localHost;

	@Value(value = "${server.port}")
	private int localPort;

	private Map<String, CachePlay> caches = new ConcurrentHashMap<>();

	@Override
	public List<Config2Result> selectConfig2Result() {
		Collection<CachePlay> values = caches.values();
		List<Config2Result> ret = new ArrayList<>();
		for (CachePlay cachePlay : values) {
			ret.add(new Config2Result(cachePlay.playConfig, cachePlay.playResult));
		}
		return ret;
	}

	@Override
	public PlayResult playVideo(PlayConfig playConfig) {
		String url = playConfig.getUrl();
		if (StringUtils.isBlank(url)) {
			return new PlayResult(wsIp, wsPort, null, localHost, localPort);
		}
		String key = playConfig.getKey();
		CachePlay cachePlay = caches.get(key);
		String clientId = playConfig.getClientId();
		if (cachePlay != null) {
			Set<String> clientIds = cachePlay.clientIds;
			if (clientIds == null) {
				clientIds = new HashSet<>();
				cachePlay.clientIds = clientIds;
			}
			clientIds.add(clientId);
			return cachePlay.playResult;
		}
		cachePlay = buildCache(playConfig);
		cachePlay.clientIds = new HashSet<>();
		cachePlay.clientIds.add(clientId);
		caches.put(key, cachePlay);
		logger.info("Open New Url:{}.", url);
		return cachePlay.playResult;
	}

	private CachePlay buildCache(PlayConfig playConfig) {
		int width = playConfig.getWidth();
		int height = playConfig.getHeight();
		String theme = "play_" + IdFactory.buildId();
		CallBack onClose = new CallBack() {
			@Override
			public void doCallBack(Object args, Object result) {
				close(playConfig.getClientId(), theme);
				if (StringUtils.isBlank(result)) {
					return;
				}
				ResultModel model = centerService.onClose(playConfig, result.toString());
				logger.info("onClose call ret:{}.", model.isOk());
			}
		};
		String cmd = PlayCmdRtsp.playCmd(playConfig.getType(), playConfig.getUrl(), playConfig.getUserName(),
				playConfig.getPassWord(), width + "x" + height, wsIp, servicePort, supersecret, theme, timeOut);
		ProcessInstance proces = ProcessUtil.doCmd(theme, cmd, new CallBack() {
			@Override
			public void doCallBack(Object args, Object result) {
				logger.info(result);// 改成info查看ffmpeg回显
			}
		}, onClose, 0);
		String url = playConfig.getUrl();
		PlayResult playResult = new PlayResult(wsIp, wsPort, theme, localHost, localPort);
		return new CachePlay(url, proces, playConfig, playResult);
	}

	private void closeProcess(CachePlay cache) {
		ProcessInstance proces = cache.proces;
		if (proces != null) {
			long pid = PidUtil.getPid(proces.getProcess());
			PidUtil.killPid(pid);
			proces.close();
		}
	}

	@Override
	public boolean close(String clientId, String theme) {
		Set<String> keySet = caches.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CachePlay cache = caches.get(key);
			String themeTemp = cache.playResult.getTheme();
			if (!theme.equals(themeTemp)) {
				continue;
			}
			Set<String> clientIds = cache.clientIds;
			if (clientIds == null) {
				continue;
			}
			clientIds.remove(clientId);
			if (!clientIds.isEmpty()) {
				continue;
			}
			closeProcess(cache);
			caches.remove(key);
			logger.info("Close :{}.", cache.url);
		}
		return true;
	}

	@Override
	public boolean close(String clientId) {
		Set<String> keySet = caches.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CachePlay cache = caches.get(key);
			Set<String> clientIds = cache.clientIds;
			if (clientIds == null) {
				continue;
			}
			if (clientIds.contains(clientId)) {
				close(clientId, cache.playResult.getTheme());
			}
		}
		return true;
	}

	@Override
	public void start() {
		ProcessUtil.doCmd("kill node", PidUtil.killProcesCmd("node"), null, null, 0).waitClose();
		ProcessUtil.doCmd("kill ffmpeg", PidUtil.killProcesCmd("ffmpeg"), null, null, 0).waitClose();
		String path = PathUtil.getProjectPath() + "src/main/resources/play/websocket.js";
		String cmd = "node " + path + " " + supersecret + " " + servicePort + " " + wsPort;
		ProcessUtil.doCmd("playService", cmd, new CallBack() {
			@Override
			public void doCallBack(Object args, Object result) {
				logger.info(result);
			}
		}, null, 0);
	}

}
