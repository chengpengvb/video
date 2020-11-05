package com.ops.www.common.dto;

import java.io.Serializable;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午1:29:31
 * 
 */
public class PlayConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * cpu方式解码
	 */
	public static final byte TYPE_CPU = 0;

	/**
	 * 使用英特尔qsv方式解码
	 */
	public static final byte TYPE_QSV = 1;

	/**
	 * 使用英伟达cuda方式解码
	 */
	public static final byte TYPE_CUDA = 2;

	public static final byte PROTOCOL_TRSP = 0;

	public static final byte PROTOCOL_TRMP = 1;

	private String clientId;

	private String url;

	private String userName;

	private String passWord;

	private int width;

	private int height;

	private byte type = TYPE_CPU;

	private byte protocol = PROTOCOL_TRSP;

	public String getClientId() {
		return clientId;
	}

	public PlayConfig setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public PlayConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public PlayConfig setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getPassWord() {
		return passWord;
	}

	public PlayConfig setPassWord(String passWord) {
		this.passWord = passWord;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public PlayConfig setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public PlayConfig setHeight(int height) {
		this.height = height;
		return this;
	}

	public byte getType() {
		return type;
	}

	public PlayConfig setType(byte type) {
		this.type = type;
		return this;
	}

	public byte getProtocol() {
		return protocol;
	}

	public PlayConfig setProtocol(byte protocol) {
		this.protocol = protocol;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + protocol;
		result = prime * result + type;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayConfig other = (PlayConfig) obj;
		if (height != other.height)
			return false;
		if (protocol != other.protocol)
			return false;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public String getKey() {
		return url + "_" + width + "_" + height + "_" + userName + "_" + passWord + "_" + type + "_" + protocol;
	}

}
