package com.ops.www.center.module;

import com.ops.www.common.dto.OnCloseCallBack;
import com.ops.www.common.dto.PlayConfig;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月31日 上午10:34:10
 * 
 */
public interface CenterCallbackService {

	void onClose(PlayConfig playConfig, String lines);

	void registCallBack(String key, OnCloseCallBack callBack);

	void deleteCallBack(String key);
}
