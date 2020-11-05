package com.ops.www.center.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ops.www.center.service.HttpService;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.common.dto.ResultModel;
import com.ops.www.common.util.StringUtils;

/**
 * @author 作者 cp
 * @version 创建时间：2020年8月26日 上午9:47:08
 * 
 */
@Service
public class HttpServiceImpl implements HttpService {

	private Logger logger = LogManager.getLogger();

	private String agent_name = "video-agent";

	private String agent_service;

	@Autowired
	@Qualifier("loadBalanced")
	private RestTemplate loadBalanced;

	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;

	@Value(value = "${server.port.ssl.key-store:}")
	public void setProtocol(String protocol) {
		if (StringUtils.isBlank(protocol)) {
			agent_service = "http://" + agent_name + "/";
		} else {
			agent_service = "https://" + agent_name + "/";
		}
	}

	@Override
	public ResultModel close(String ip, int port, String clientId, byte protocol) {
		try {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("clientId", clientId);
			paramMap.add("protocol", protocol);
			ResultModel resultModel = restTemplate.postForObject("http://" + ip + ":" + port + "/agent/close", paramMap,
					ResultModel.class);
			return resultModel;
		} catch (Exception e) {
			return new ResultModel(e.getMessage(), false, null);
		}
	}

	@Override
	public ResultModel close(String ip, int port, String callbackId, String clientId, String theme, byte protocol) {
		try {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("clientId", clientId);
			paramMap.add("theme", theme);
			paramMap.add("protocol", protocol);
			ResultModel resultModel = restTemplate.postForObject("http://" + ip + ":" + port + "/agent/closeByTheme",
					paramMap, ResultModel.class);
			return resultModel;
		} catch (Exception e) {
			return new ResultModel(e.getMessage(), false, null);
		}

	}

	@Override
	public ResultModel closeAll(String ip, int port) {
		try {
			ResultModel resultModel = restTemplate.postForObject("http://" + ip + ":" + port + "/agent/closeAll", null,
					ResultModel.class);
			return resultModel;
		} catch (Exception e) {
			return new ResultModel(e.getMessage(), false, null);
		}
	}

	@Override
	public PlayResult playVideo(PlayConfig playConfig) {
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("playConfig", playConfig);
		try {
			PlayResult playResult = loadBalanced.postForObject(agent_service + "/agent/playVideo", paramMap,
					PlayResult.class);
			return playResult;
		} catch (Exception e) {
			logger.error("playVideo error:{}.", e.getMessage());
			return null;
		}
	}

}
