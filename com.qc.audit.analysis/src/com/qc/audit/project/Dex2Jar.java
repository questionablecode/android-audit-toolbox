package com.qc.audit.project;

import java.io.File;
import java.io.IOException;

public class Dex2Jar {

	public static void run(File dexFile, File jarFile) throws IOException {
		com.googlecode.dex2jar.v3.Main.doFile(dexFile, jarFile);
	}

}
