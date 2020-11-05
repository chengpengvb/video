package com.ops.www.common.util;

import java.io.File;
import java.io.IOException;

public final class PathUtil {
	private PathUtil() {
	}

	/**
	 * 项目路径
	 */
	public static String getProjectPath() {
		String osName = System.getProperties().getProperty("os.name");
		if (osName.startsWith("Windows")) {
			return System.getProperty("user.dir") + File.separator;
		} else {
			return File.separator + System.getProperty("user.dir") + File.separator;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @throws IOException
	 */

	public static void mkDirFile(String destDirName) throws IOException {
		final File parent = new File(destDirName).getParentFile();
		if (parent == null) {
			return;
		}
		forceMkdir(parent);
	}

	private static void forceMkdir(final File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				final String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				if (!directory.isDirectory()) {
					final String message = "Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("\\") + 1);
	}

	public static String getFileNameWithOutExtend(String path) {
		if (!path.contains(".")) {
			return null;
		}
		return path.substring(path.replaceAll("\\\\", "/").lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

}
