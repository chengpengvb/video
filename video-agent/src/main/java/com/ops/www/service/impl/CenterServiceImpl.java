package com.ops.www.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.ResultModel;
import com.ops.www.common.util.StringUtils;
import com.ops.www.service.CenterService;

@Service
public class CenterServiceImpl implements CenterService {

	private Logger logger = LogManager.getLogger();

	private String center_service;

	private String centerName = "video-center";

	private String host;

	@Value(value = "${system.ssl:false}")
	private boolean ssl;

	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("restSslTemplate")
	private RestTemplate restSslTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	private synchronized void ckConfig() {
		if (!StringUtils.isBlank(host)) {
			return;
		}
		List<ServiceInstance> instances = discoveryClient.getInstances(centerName);
		if (instances.isEmpty()) {
			return;
		}
		ServiceInstance instance = instances.get(0);
		host = instance.getHost();
		int port = instance.getPort();
		if (ssl) {
			center_service = "https://" + host + ":" + port + "/";
		} else {
			center_service = "http://" + host + ":" + port + "/";
		}
	}

	@Override
	public ResultModel onClose(PlayConfig playConfig, String lines) {
		try {
			ckConfig();
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("playConfig", playConfig);
			paramMap.add("lines", lines);
			RestTemplate temp = null;
			if (ssl) {
				temp = restSslTemplate;
			} else {
				temp = restTemplate;
			}
			return temp.postForObject(center_service + "/center/onClose", paramMap, ResultModel.class);
		} catch (Exception e) {
			logger.error("onClose call error:{}.", e.getMessage());
			return new ResultModel("调用失败", false, null);
		}
	}

}
