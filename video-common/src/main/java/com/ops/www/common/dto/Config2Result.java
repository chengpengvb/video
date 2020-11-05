package com.ops.www.common.dto;

import java.io.Serializable;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月15日 上午11:25:16
 * 
 */
public class Config2Result implements Serializable {
	private static final long serialVersionUID = 1L;
	private PlayResult playResult;
	private PlayConfig playConfig;

	public Config2Result() {
	}

	public Config2Result(PlayConfig playConfig, PlayResult playResult) {
		this.playResult = playResult;
		this.playConfig = playConfig;
	}

	public PlayResult getPlayResult() {
		return playResult;
	}

	public Config2Result setPlayResult(PlayResult playResult) {
		this.playResult = playResult;
		return this;
	}

	public PlayConfig getPlayConfig() {
		return playConfig;
	}

	public Config2Result setPlayConfig(PlayConfig playConfig) {
		this.playConfig = playConfig;
		return this;
	}

}
