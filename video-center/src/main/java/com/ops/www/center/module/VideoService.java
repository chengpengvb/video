package com.ops.www.center.module;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.PlayResult;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午1:28:44
 * 
 */
public interface VideoService {

	PlayResult playVideo(PlayConfig playConfig);

	void closeAll();

}
