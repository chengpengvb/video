package com.ops.www.service;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.dto.ResultModel;

public interface CenterService {

	ResultModel onClose(PlayConfig playConfig, String lines);

}
