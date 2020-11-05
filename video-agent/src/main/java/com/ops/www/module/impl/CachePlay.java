package com.ops.www.module.impl;

import java.util.Set;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;
import com.ops.www.common.util.ProcessUtil.ProcessInstance;

/**
 * @author 作者 cp
 * @version 创建时间：2020年8月16日 上午10:02:16
 * 
 */
public class CachePlay {
	final String url;
	final ProcessInstance proces;
	final PlayResult playResult;
	final PlayConfig playConfig;
	Set<String> clientIds;

	CachePlay(String url, ProcessInstance proces, PlayConfig playConfig, PlayResult playResult) {
		this.url = url;
		this.proces = proces;
		this.playConfig = playConfig;
		this.playResult = playResult;
	}

}
