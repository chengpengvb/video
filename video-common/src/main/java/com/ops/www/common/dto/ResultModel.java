package com.ops.www.common.dto;

import java.io.Serializable;

/**
 * 返回结果集
 */
public class ResultModel implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int CODE_SUCCESS = 200;

	public static final int CODE_ERROR = 500;

	public static final int CODE_NOT_FAND = 404;

	public static final ResultModel RESULT_ERROR = new ResultModel("操作失败", false, null);

	private String msg;// 描述信息
	private int code = CODE_SUCCESS;// 状态码 200:成功 500:错误 404:未找到
	private Object obj;// 返回对象
	private byte order = 0;// 命令方式 0：播放 1:关闭
	private String callbackId;// 方法调用唯一标识
	private String lines;

	public ResultModel(String msg, int code, Object obj) {
		this.msg = msg;
		this.code = code;
		this.obj = obj;
	}

	public ResultModel(String msg, boolean ok, Object obj) {
		this.msg = msg;
		this.obj = obj;
		if (ok) {
			code = CODE_SUCCESS;
		} else {
			code = CODE_ERROR;
		}
	}

	public boolean isOk() {
		return code == CODE_SUCCESS ? true : false;
	}

	public ResultModel() {

	}

	public String getMsg() {
		return msg;
	}

	public ResultModel setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public int getCode() {
		return code;
	}

	public ResultModel setCode(int code) {
		this.code = code;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public ResultModel setObj(Object obj) {
		this.obj = obj;
		return this;
	}

	public byte getOrder() {
		return order;
	}

	public ResultModel setOrder(byte order) {
		this.order = order;
		return this;
	}

	public String getCallbackId() {
		return callbackId;
	}

	public String getLines() {
		return lines;
	}

	public ResultModel setLines(String lines) {
		this.lines = lines;
		return this;
	}

	public ResultModel setCallbackId(String callbackId) {
		this.callbackId = callbackId;
		return this;
	}

}