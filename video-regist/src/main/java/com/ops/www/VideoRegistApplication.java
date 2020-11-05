package com.ops.www;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.ops.www.common.util.StopWatch;

@EnableEurekaServer
@SpringBootApplication
public class VideoRegistApplication {

	private static Logger logger = LogManager.getLogger();

	private static void printMian(double timeSecond) {
		int sunSize = 100;
		StringBuffer sb = new StringBuffer(3 * sunSize);
		StringBuffer endSb = new StringBuffer(sunSize);
		sb.append("\n");
		String timeStr = "Video-Regist startUp in " + timeSecond + "s";
		if (timeStr.length() % 2 != 0) {
			timeStr += " ";
		}
		int timeSize = timeStr.length(), blankSize = (sunSize - timeSize - 2) / 2;
		StringBuffer blankSb = new StringBuffer(blankSize);
		for (int i = 0; i < sunSize; i++) {
			sb.append("*");
			endSb.append("*");
		}
		for (int i = 0; i < blankSize; i++) {
			blankSb.append(" ");
		}
		sb.append("\n*").append(blankSb).append(timeStr).append(blankSb).append("*\n").append(endSb);
		logger.info(sb);
	}

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		SpringApplication.run(VideoRegistApplication.class, args);
		stopWatch.stop();
		printMian(stopWatch.timeSecond());
	}
}
