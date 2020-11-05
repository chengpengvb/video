package com.ops.www.center.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ops.www.center.service.CenterService;
import com.ops.www.center.system.SystemBoot;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 上午10:53:03
 * 
 */
@Component
public class DefaultSystemBoot implements SystemBoot {

	@Autowired
	private CenterService centerService;

	private void start() {
		centerService.start();
	}

	@Override
	public void run(String... args) throws Exception {
		start();
	}

}
