/**
 * Copyright [2016-2017] [yadong.zhang]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zyd.ztools.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * Operations on {@link java.io.File} by {@link java.util.zip.ZipFile}
 * </p>
 *
 * <ul>
 * <li><b>zip</b> - Zip normal files or directories</li>
 * <li><b>unzip</b> - Unzip the zip file</li>
 * </ul>
 * 
 * @author <a href="mailto:yadong.zhang0415@gmail.com">yadong.zhang</a>
 * @date 30/03/2017 14:09:20
 * @version V1.0
 * @since JDK ： 1.7
 */
public class ZipUtils {
	
	/**
	 * A default Buffer
	 */
	static final int DEFAULT_BUFFER = 2048;

	/**
	 * Extract the <code>zipPath</code> archive to <code>targertUnzipPath</code>
	 * 
	 * @param zipFilePath
	 *            Need to extract the zip file.
	 * @param targertUnzipPath
	 *            Unzip the file directory.
	 * @return <code>true</code> if the Unzip file was successful.
	 */
	public static boolean unzip(String zipFilePath, String targertUnzipPath) {
		boolean retCode = false;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFilePath);
			Enumeration<? extends ZipEntry> emu = zipFile.entries();

			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					FileUtil.mkdirs(targertUnzipPath + entry.getName());
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(targertUnzipPath + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件。
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				FileUtil.mkdirs(file.getParentFile());

				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, DEFAULT_BUFFER);
				int count;
				byte data[] = new byte[DEFAULT_BUFFER];
				while ((count = bis.read(data, 0, DEFAULT_BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				CloseableUtil.close(bos, bis);
			}
			retCode = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseableUtil.close(bos, bis, fos, zipFile);
		}

		return retCode;
	}

	/**
	 * ZIP the <b>srcFile</b> (file or dir) into <b> targetZipFilePath </b>
	 * 
	 * @param srcFile
	 *            The file to be written.Can be dir or file
	 * @param targetZipFilePath
	 *            Zip File Path.Must be a file and ensure that <b>the parent
	 *            directory</b> of the file exists
	 * @return <code>true</code> if the Zip file was successful.
	 */
	public static boolean zip(String srcFile, String targetZipFilePath) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(targetZipFilePath);
			return zip(srcFile, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Creates a ZIP file by {@link java.io.OutputStream OutputStream};
	 * 
	 * @param srcFile
	 *            The file to be written.Can be dir or file
	 * @param outputStream
	 *            A new ZIP output stream.
	 * @return <code>true</code> if the Zip file was successful.
	 */
	public static boolean zip(String srcPath, OutputStream outputStream) {
		File f = new File(srcPath);
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			return zip(Arrays.asList(files), outputStream);
		} else {
			List<File> files = new ArrayList<File>();
			files.add(f);
			return zip(files, outputStream);
		}
	}

	/**
	 * Creates a ZIP file for <b>srcFiles</b> by {@link java.io.OutputStream
	 * OutputStream};
	 * 
	 * @param srcFiles
	 *            List of files to be written
	 * @param outputStream
	 *            A new ZIP output stream.
	 * @return <code>true</code> if the Zip file was successful.
	 * 
	 */
	public static boolean zip(List<File> srcFiles, OutputStream outputStream) {
		boolean retCode = false;
		ZipOutputStream zout = null;
		FileInputStream fi = null;
		ZipEntry entry = null;
		try {
			zout = new ZipOutputStream(outputStream);
			byte data[] = new byte[DEFAULT_BUFFER];
			for (File f : srcFiles) {
				fi = new FileInputStream(f);
				entry = new ZipEntry(f.getName());
				zout.putNextEntry(entry);
				int count = -1;
				while ((count = fi.read(data, 0, DEFAULT_BUFFER)) != -1) {
					zout.write(data, 0, count);
				}
				CloseableUtil.close(fi);
			}
			retCode = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseableUtil.close(zout, fi, outputStream);
		}
		return retCode;
	}

	public static void main(String[] args) {
		boolean zipResult = zip("D:\\opt\\zip\\src", "D:\\opt\\zip\\zipTarget\\zip.zip");
		System.out.println(zipResult ? "压缩成功！" : "压缩失败！");
		boolean unzipResult = unzip("D:\\opt\\zip\\zipTarget\\zip.zip", "D:\\opt\\zip\\src\\unzip\\");
		System.out.println(unzipResult ? "解压缩成功！" : "解压缩失败！");
	}
}
