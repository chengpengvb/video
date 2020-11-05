package com.ops.www.center.module.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.ops.www.center.module.VideoService;
import com.ops.www.center.service.HttpService;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;

@Service
public class VideoServiceImpl implements VideoService {

	private String agent_name = "video-agent";

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private HttpService httpService;

	@Override
	public PlayResult playVideo(PlayConfig playConfig) {
		return httpService.playVideo(playConfig);
	}

	@Override
	public void closeAll() {
		List<ServiceInstance> instances = discoveryClient.getInstances(agent_name);
		for (ServiceInstance serviceInstance : instances) {
			httpService.closeAll(serviceInstance.getHost(), serviceInstance.getPort());
		}
	}

}
