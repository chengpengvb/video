package com.ops.www.module;

import java.util.List;

import com.ops.www.common.dto.Config2Result;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;

public interface PlayManager {

	void start();

	PlayResult playVideo(PlayConfig playConfig);

	boolean close(String clientId);

	boolean close(String clientId, String theme);

	List<Config2Result> selectConfig2Result();
}
