package com.ops.www.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ops.www.module.PlayManager;
import com.ops.www.system.SystemBoot;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 上午10:53:03
 * 
 */
@Component
public class DefaultSystemBoot implements SystemBoot {

	@Autowired
	@Qualifier("rtspPlayManager")
	private PlayManager rtspPlayManager;

	private void start() {
		rtspPlayManager.start();
	}

	@Override
	public void run(String... args) throws Exception {
		start();
	}

}
