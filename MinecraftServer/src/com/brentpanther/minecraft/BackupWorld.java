package com.brentpanther.minecraft;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupWorld {

	public static void backup(String file) throws Exception {
		String dir = new File(".").getCanonicalPath();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy-HH-mm");
		String dest = file + "-" + sdf.format(new Date());
		String folder = dir.endsWith(File.separator) ? dir : dir + File.separator;
		File srcPath = new File(folder + file);
		File dstPath = new File(folder + dest);
		copyDirectory(srcPath, dstPath);
	}

	private static void copyDirectory(File srcPath, File dstPath) throws Exception {
		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}
			String files[] = srcPath.list();
			for (int i = 0; i < files.length; i++) {
				copyDirectory(new File(srcPath, files[i]), new File(dstPath,
						files[i]));
			}
		} else {
			copyFile(srcPath, dstPath);
		}
	}

	private static void copyFile(File sourceFile, File destFile) throws Exception {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

}
