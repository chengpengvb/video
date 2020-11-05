package com.ops.www.util;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Kernel32 extends Library {

	public static Kernel32 INSTANCE = (Kernel32) Native.load("kernel32", Kernel32.class);

	public long GetProcessId(Long hProcess);

}
