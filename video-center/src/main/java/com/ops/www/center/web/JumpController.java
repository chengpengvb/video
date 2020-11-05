package com.ops.www.center.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ops.www.center.service.CenterService;
import com.ops.www.common.dto.Config2Result;

@Controller
public class JumpController {

	@Autowired
	private CenterService centerService;

	@RequestMapping("/")
	public String defaultJump() {
		return "default";
	}

	@RequestMapping("/index")
	public String index() {
		return "multiPlay";
	}

	@RequestMapping("/selectAllCache") // 查询当前所有在线的客户信息
	@ResponseBody
	public Map<String, Config2Result> selectAllCache() {
		return centerService.selectAllCache();
	}
}
