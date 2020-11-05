package com.ops.www.util.cmd;

import com.ops.www.common.dto.PlayConfig;
import com.ops.www.common.util.StringUtils;
import com.sun.jna.Platform;

/**
 * @author 作者 cp
 * @version 创建时间：2020年7月31日 上午10:04:26
 * 
 */
public class PlayCmdRtsp {

	private PlayCmdRtsp() {
	}

	private static String getRtsp(String rtsp, String userName, String passWord) {
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(passWord)) {
			return rtsp;
		}
		String[] strArray = rtsp.split("//");
		return strArray[0] + "//" + userName + ":" + passWord + "@" + strArray[1];
	}

	private static String buildRtsp(String rtsp, String userName, String passWord) {
		if (Platform.isWindows()) {
			return "\"" + getRtsp(rtsp, userName, passWord) + "\"";
		} else {
			return getRtsp(rtsp, userName, passWord);
		}
	}

	private static String cpuCmd(String rtsp, String userName, String passWord, String size, String wsIp, int port,
			String supersecret, String theme, int timeOut) {
		StringBuffer sb = new StringBuffer();
		sb.append("ffmpeg").append(" -stimeout ").append(timeOut).append("000000").append(" -i ");
		sb.append(buildRtsp(rtsp, userName, passWord));
		sb.append(" -r 30  -q 0 -ar 44100 -f mpegts -codec:v mpeg1video -s ").append(size);
		sb.append(" -codec:a mp2 -muxdelay 0.001 -y -max_muxing_queue_size 9999 http://").append(wsIp).append(":")
				.append(port).append("/").append(supersecret).append("/").append(theme);
		return sb.toString();
	}

	private static String qsvCmd(String rtsp, String userName, String passWord, String size, String wsIp, int port,
			String supersecret, String theme, int timeOut) {
		StringBuffer sb = new StringBuffer();
		sb.append("ffmpeg -c:v h264_qsv ").append("-stimeout ").append(timeOut).append("000000").append(" -i ");
		sb.append(buildRtsp(rtsp, userName, passWord));
		sb.append("  -r 30  -q 0 -ar 44100 -vcodec h264_qsv -f mpegts -codec:v mpeg1video -s ").append(size);
		sb.append(" -codec:a mp2 -muxdelay 0.001 -y -max_muxing_queue_size 9999 http://").append(wsIp).append(":")
				.append(port).append("/").append(supersecret).append("/").append(theme);
		return sb.toString();
	}

	private static String cudaCmd(String rtsp, String userName, String passWord, String size, String wsIp, int port,
			String supersecret, String theme, int timeOut) {
		StringBuffer sb = new StringBuffer();
		sb.append("ffmpeg  -c:v h264_cuvid ").append("-stimeout ").append(timeOut).append("000000").append(" -i ");
		sb.append(buildRtsp(rtsp, userName, passWord));
		sb.append("  -r 30 -q 0 -ar 44100 -c:v h264_nvenc -f mpegts -codec:v mpeg1video -s ").append(size);
		sb.append(" -codec:a mp2 -muxdelay 0.001 -y -max_muxing_queue_size 9999 http://").append(wsIp).append(":")
				.append(port).append("/").append(supersecret).append("/").append(theme);
		return sb.toString();
	}

	public static String playCmd(byte type, String rtsp, String userName, String passWord, String size, String wsIp,
			int port, String supersecret, String theme, int timeOut) {
		switch (type) {
		case PlayConfig.TYPE_CPU:
			return cpuCmd(rtsp, userName, passWord, size, wsIp, port, supersecret, theme, timeOut);
		case PlayConfig.TYPE_CUDA:
			return cudaCmd(rtsp, userName, passWord, size, wsIp, port, supersecret, theme, timeOut);
		case PlayConfig.TYPE_QSV:
			return qsvCmd(rtsp, userName, passWord, size, wsIp, port, supersecret, theme, timeOut);
		default:
			throw new RuntimeException("Unsupported type: " + type + "!");
		}
	}
//	public static void main(String[] args) {
//		byte type = 0;
//		String rtsp = "rtsp://127.0.0.1:8554/live";
//		String userName = "admin";
//		String passWord = "yzfar123";
//		String size = "800x600";
//		String wsIp = "127.0.0.1";
//		int port = 8081;
//		String supersecret = "supersecret";
//		String theme = "live1";
//		String playCmd = playCmd(type, rtsp, userName, passWord, size, wsIp, port, supersecret, theme);
//		System.out.println(playCmd);
//	}
}
