package com.ops.www.common.dto;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月31日 上午11:15:26
 * 
 */
public interface OnCloseCallBack {

	
	
	void doOnClose(PlayConfig playConfig, String lines);
}
