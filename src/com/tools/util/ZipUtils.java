package com.tools.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Description Zip工具类，提供压缩和解压缩功能
 * @author zhangyd
 * @date 2017年3月30日 下午2:09:20
 * @version V1.0
 * @since JDK ： 1.7
 * @modify
 * @Review
 */
public class ZipUtils {
	static final int BUFFER = 2048;

	/**
	 * @Description 解压
	 * @author zhangyd
	 * @date 2017年3月30日 下午2:09:46
	 * @param zipPath
	 *            待解压的文件路径
	 * @param descDir
	 *            待解压到的目录
	 * @return
	 */
	public static boolean unzip(String zipPath, String descDir) {
		boolean retCode = false;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			Enumeration<? extends ZipEntry> emu = zipFile.entries();

			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					new File(descDir + entry.getName()).mkdirs();
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(descDir + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件。
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, BUFFER);
				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				close(bos, bis);
			}
			zipFile.close();
			retCode = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bos, bis, fos);
		}

		return retCode;
	}

	/**
	 * @Description 压缩
	 * @author zhangyd
	 * @date 2017年3月30日 下午2:10:42
	 * @param zipPath
	 *            待压缩的文件路径
	 * @param srcPath
	 *            需要压缩的源文件地址
	 * @return
	 */
	public static boolean zip(String zipPath, String srcPath) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(zipPath);
			return zip(fos, srcPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean zip(OutputStream out, String srcPath) {
		File f = new File(srcPath);
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			return zip(out, Arrays.asList(files));
		} else {
			List<File> files = new ArrayList<File>();
			files.add(f);
			return zip(out, files);
		}
	}

	public static boolean zip(OutputStream out, List<File> srcFiles) {
		boolean retCode = false;
		ZipOutputStream zout = null;
		FileInputStream fi = null;
		ZipEntry entry = null;
		try {
			zout = new ZipOutputStream(out);
			byte data[] = new byte[BUFFER];
			for (File f : srcFiles) {
				fi = new FileInputStream(f);
				entry = new ZipEntry(f.getName());
				zout.putNextEntry(entry);
				int count = -1;
				while ((count = fi.read(data, 0, BUFFER)) != -1) {
					zout.write(data, 0, count);
				}
				close(fi);
			}
			retCode = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(zout, fi);
		}
		return retCode;
	}

	public static void close(Closeable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
						closeable = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		boolean zipResult = zip("D:\\opt\\zip\\zipTarget\\zip.zip", "D:\\opt\\zip\\src");
		System.out.println(zipResult ? "压缩成功！" : "压缩失败！");
		boolean unzipResult = unzip("D:\\opt\\zip\\zipTarget\\zip.zip", "D:\\opt\\zip\\src\\unzip\\");
		System.out.println(unzipResult ? "解压缩成功！" : "解压缩失败！");
	}
}
