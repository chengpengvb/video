package com.ops.www.center.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ops.www.center.module.CenterCallbackService;
import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.ResultModel;

@Controller
@RequestMapping("/center")
public class CenterController {

	@Autowired
	private CenterCallbackService centerCallbackService;

	@RequestMapping(value = "/onClose")
	@ResponseBody
	public ResultModel onClose(@RequestParam Map<String, Object> paramMap) {
		PlayConfig playConfig = JSONObject.parseObject(paramMap.get("playConfig").toString(), PlayConfig.class);
		String lines = paramMap.get("lines").toString();
		centerCallbackService.onClose(playConfig, lines);
		return new ResultModel(null, true, null);
	}
}
