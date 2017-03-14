package com.tools.str2Png;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import com.tools.util.FileUtil;

import gui.ava.html.image.generator.HtmlImageGenerator;

public class Util {

	public static void main(String[] args) throws Exception {
		String path = "D:\\links_mail.html";
		File file = new File(path);
		String content = FileUtil.getFileContent(file);
		System.out.println(content);
		html2Image(content, "html2Image");
	}

	/**
	 * 草了， 用loadHtml一直不行， 结果在模板中将style提到外层就OK了，比如 <html> <style>...</style>
	 * <head>...</head> <body></body> </html> 妈蛋的！style放到head中是无法生成图片的
	 * 
	 * @Description
	 * @author zhangyd
	 * @date 2017年3月6日 下午5:50:17
	 * @param content
	 * @throws Exception
	 */
	public static void html2Image(String content, String title) throws Exception {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		// 设置图片的宽高，不是截屏内容的宽高
		// Dimension ds = new Dimension(1000, 4000);
		// imageGenerator.setSize(ds);
		imageGenerator.loadHtml(content);
		createFile(imageGenerator, title);
	}

	private static void createFile(HtmlImageGenerator imageGenerator, String title) {
		ImageOutputStream imOut = null;
		FileOutputStream fileOut = null;
		ByteArrayOutputStream byteOut = null;
		try {
			// 拿图片流
			BufferedImage bufferedImage = imageGenerator.getBufferedImage();
			bufferedImage.flush();
			// 写图片流
			byteOut = new ByteArrayOutputStream();
			imOut = ImageIO.createImageOutputStream(byteOut);
			ImageIO.write(bufferedImage, "png", imOut);
			// 拿到图片流
			byte[] b = byteOut.toByteArray();
			String path = "D:" + File.separator + title + ".png";

			fileOut = new FileOutputStream(path);
			fileOut.write(b);
			fileOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(imOut, fileOut, byteOut);
		}
	}

	public static void close(Closeable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						System.out.println("啊偶,年轻人搞事情啊...");
					}
				}
			}
		}
	}

	/**
	 * 方法详解：该方法利用Robot提供的强大桌面操作能力 硬性调用浏览器打开指定网页，并将网页信息保存到本地。 优势：简单易用，不需要任何第三方插件。
	 * 缺点：不能同时处理大量数据，技术含量过低，属于应急型技巧。
	 *
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws AWTException
	 *
	 */
	public static void robot() throws MalformedURLException, IOException, URISyntaxException, AWTException {
		// 此方法仅适用于JdK1.6及以上版本
		Desktop.getDesktop().browse(new URL("http://www.flyat.cc/simpleArticle/295744a0af6942fbbf3e4dffa898bd48").toURI());
		Robot robot = new Robot();
		Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		int width = (int) d.getWidth();
		int height = (int) d.getHeight();
		// 最大化浏览器
		robot.keyRelease(KeyEvent.VK_F11);
		robot.delay(2000);
		Image image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		// 保存图片
		ImageIO.write(bi, "jpg", new File("D:" + File.separator + "html.png"));
	}

	/**
	 * @Description cssbox插件生成图片
	 * @author zhangyd
	 * @date 2017年3月14日 上午11:50:20
	 */
	public static void cssbox() {
		ImageRenderer render = new ImageRenderer();
		render.setWindowSize(new Dimension(1400, 600), false);
		String url = "http://www.flyat.cc/simpleArticle/295744a0af6942fbbf3e4dffa898bd48";
		try {
			render.renderURLByFile(url, new File("D:" + File.separator + "html.png"), ImageRenderer.Type.PNG);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 原理就是在现在的awt或者swing上显示网页然后将内容保存为一个图片,没办法控制延迟啊。
	 * 
	 * @Description
	 * @author zhangyd
	 * @date 2017年3月13日 上午9:40:53
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void awt() throws MalformedURLException, IOException, InterruptedException {
		JEditorPane ed = new JEditorPane(new URL("http://www.flyat.cc/simpleArticle/295744a0af6942fbbf3e4dffa898bd48"));
		System.out.println("10");
		Thread.sleep(10000);
		ed.setSize(1000, 1000);

		// create a new image
		BufferedImage image = new BufferedImage(ed.getWidth(), ed.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// paint the editor onto the image
		SwingUtilities.paintComponent(image.createGraphics(), ed, new JPanel(), 0, 0, image.getWidth(),
				image.getHeight());
		// save the image to file
		ImageIO.write((RenderedImage) image, "png", new File("D:" + File.separator + "html.png"));
		System.out.println("ok");
	}

}
