package com.ops.www.center.service;

import java.util.Map;

import com.ops.www.common.dto.Config2Result;
import com.ops.www.common.dto.OnCloseCallBack;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午4:19:51
 * 
 */
public interface CenterService {

	PlayResult playVideo(String callbackId, PlayConfig playConfig);

	void closeByPlayConfig(PlayConfig playConfig);

	boolean close(String clientId, byte protocol);

	boolean close(String callbackId, String clientId, String theme, byte protocol);

	void start();

	void registCallBack(String key, OnCloseCallBack callBack);

	void deleteCallBack(String key);

	Map<String, Config2Result> selectAllCache();
}
