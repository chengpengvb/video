package com.ops.www.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author 作者 cp
 * @version 创建时间：2020年7月13日 上午11:09:31
 * 
 */
public class ProcessUtil {

	public static ProcessInstance doCmd(String name, String cmd, CallBack callBack, CallBack onClose, int delay) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			IoReader inputReader = new IoReader(name + "_I", new InputStreamReader(process.getInputStream(), "GB2312"),
					callBack, onClose, delay);
			IoReader errorReader = new IoReader(name + "_E", new InputStreamReader(process.getErrorStream(), "GB2312"),
					callBack, onClose, delay);
			inputReader.start();
			errorReader.start();
			return new ProcessInstance(name, process, inputReader, errorReader);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class ProcessInstance {
		final Process process;
		final IoReader inputReader;
		final IoReader errorReader;
		final String name;

		public ProcessInstance(String name, Process process, IoReader inputReader, IoReader errorReader) {
			this.process = process;
			this.inputReader = inputReader;
			this.errorReader = errorReader;
			this.name = name;
		}

		public void close() {
			this.process.destroy();
			this.inputReader.stopProces();
			this.errorReader.stopProces();
		}

		public Process getProcess() {
			return process;
		}

		public boolean isOpen() {
			return this.inputReader.isOpen() && this.errorReader.isOpen();
		}

		public void waitClose() {
			while (true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					break;
				}
				if (!this.inputReader.isOpen() && !this.errorReader.isOpen()) {
					break;
				}
			}
		}
	}

	private static class IoReader extends Thread {
		private final InputStreamReader is;
		private final CallBack callBack;
		private final CallBack onClose;
		private AtomicBoolean open = new AtomicBoolean(true);
		private final String theme;
		private final int delay;
		private StringBuffer sb = new StringBuffer();

		IoReader(String theme, InputStreamReader is, CallBack callBack, CallBack onClose, int delay) {
			super(theme);
			this.theme = theme;
			this.is = is;
			this.callBack = callBack;
			this.onClose = onClose;
			this.delay = delay;
		}

		@Override
		public void run() {
			if (delay > 0) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {
					return;
				}
			}
			try {
				LineNumberReader input = new LineNumberReader(is);
				String line = input.readLine();
				int num = 0;
				while (line != null) {
					if (num >= 100) {
						sb.setLength(0);
					}
					sb.append(line).append("\n");
					num++;
					Thread.sleep(1);
					if (callBack != null) {
						callBack.doCallBack(theme, line);
					}
					if (!isOpen()) {
						break;
					}
					line = input.readLine();
				}
				open.set(false);
				if (onClose != null) {
					onClose.doCallBack(null, sb);
				}
			} catch (IOException | InterruptedException e) {
			} finally {
				close();
			}
		}

		void close() {
			open.set(false);
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		void stopProces() {
			open.compareAndSet(true, false);
			this.interrupt();
		}

		boolean isOpen() {
			return open.get();
		}
	}

}
