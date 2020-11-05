package com.ops.www.common.util;

import java.util.UUID;

public final class IdFactory {

	private IdFactory() {
	}

	public static String buildId() {
		return UUID.randomUUID().toString().replaceAll("-", "c");
	}

}
