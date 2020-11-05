package com.ops.www.center.service;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.common.dto.ResultModel;

/**
 * @author 作者 cp
 * @version 创建时间：2020年8月26日 上午9:45:04
 * 
 */
public interface HttpService {

	PlayResult playVideo(PlayConfig playConfig);

	ResultModel close(String ip, int port, String clientId, byte protocol);

	ResultModel close(String ip, int port, String callbackId, String clientId, String theme, byte protocol);

	ResultModel closeAll(String ip, int port);
}
