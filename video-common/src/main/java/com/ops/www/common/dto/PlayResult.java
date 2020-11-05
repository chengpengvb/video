package com.ops.www.common.dto;

import java.io.Serializable;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 下午2:09:37
 * 
 */
public class PlayResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String wsIp;

	private int wsPort;

	private String theme;

	private String localHost;

	private int localPort;

	public PlayResult(String wsIp, int wsPort, String theme, String localHost, int localPort) {
		this.wsIp = wsIp;
		this.wsPort = wsPort;
		this.theme = theme;
		this.localHost = localHost;
		this.localPort = localPort;
	}

	public PlayResult() {
	}

	public String getWsIp() {
		return wsIp;
	}

	public int getWsPort() {
		return wsPort;
	}

	public String getTheme() {
		return theme;
	}

	public String getLocalHost() {
		return localHost;
	}

	public PlayResult setWsIp(String wsIp) {
		this.wsIp = wsIp;
		return this;
	}

	public PlayResult setWsPort(int wsPort) {
		this.wsPort = wsPort;
		return this;
	}

	public PlayResult setTheme(String theme) {
		this.theme = theme;
		return this;
	}

	public PlayResult setLocalHost(String localHost) {
		this.localHost = localHost;
		return this;
	}

	public int getLocalPort() {
		return localPort;
	}

	public PlayResult setLocalPort(int localPort) {
		this.localPort = localPort;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localHost == null) ? 0 : localHost.hashCode());
		result = prime * result + localPort;
		result = prime * result + ((theme == null) ? 0 : theme.hashCode());
		result = prime * result + ((wsIp == null) ? 0 : wsIp.hashCode());
		result = prime * result + wsPort;
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
		PlayResult other = (PlayResult) obj;
		if (localHost == null) {
			if (other.localHost != null)
				return false;
		} else if (!localHost.equals(other.localHost))
			return false;
		if (localPort != other.localPort)
			return false;
		if (theme == null) {
			if (other.theme != null)
				return false;
		} else if (!theme.equals(other.theme))
			return false;
		if (wsIp == null) {
			if (other.wsIp != null)
				return false;
		} else if (!wsIp.equals(other.wsIp))
			return false;
		if (wsPort != other.wsPort)
			return false;
		return true;
	}

}
