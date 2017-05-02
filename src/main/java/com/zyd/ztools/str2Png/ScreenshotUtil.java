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
package com.zyd.ztools.str2Png;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.zyd.ztools.util.DateUtil;

import gui.ava.html.image.generator.HtmlImageGenerator;

public class ScreenshotUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static boolean cssbox(String url, String path) {
        boolean result = false;
        LOGGER.info("Start to generate image with URL is [{}]================", url);
        ImageRenderer render = new ImageRenderer();
        render.setWindowSize(new Dimension(450, 1), false);
        try {
            render.renderURLByFile(url, new File(path), ImageRenderer.Type.PNG);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        LOGGER.info("Generate image Successful！================");
        return result;
    }

    @Deprecated
    public static void url2Image(String url, String title) throws Exception {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        // Dimension ds = new Dimension(500, 4000);
        // imageGenerator.setSize(ds);
        imageGenerator.loadUrl(new URL(url));
        createFile(imageGenerator, title);
    }

    /**
     * 草了， 用loadHtml一直不行， 结果在模板中将style提到外层就OK了，比如 <html> <style>...</style>
     * <head>...</head> <body></body> </html> 妈蛋的！style放到head中是无法生成图片的
     *
     * @param content
     * @throws Exception
     * @Description
     * @author zhangyd
     * @date 2017年3月6日 下午5:50:17
     */
    @Deprecated
    public static void html2Image(String content, String path) throws Exception {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        // Dimension ds = new Dimension(1000, 4000);
        // imageGenerator.setSize(ds);
        imageGenerator.loadHtml(content);
        createFile(imageGenerator, path);
    }

    @Deprecated
    private static void createFile(HtmlImageGenerator imageGenerator, String path) {
        ImageOutputStream imOut = null;
        byte[] b = null;
        try {
            // 拿图片流
            BufferedImage bufferedImage = imageGenerator.getBufferedImage();
            bufferedImage.flush();
            // 写图片流
            ByteArrayOutputStream bs = new ByteArrayOutputStream();

            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "png", imOut);
            // 拿到图片流
            b = bs.toByteArray();
            createFile(path, b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void createFile(String path, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    /**
     * 方法详解：该方法利用Robat提供的强大桌面操作能力 硬性调用浏览器打开指定网页，并将网页信息保存到本地。 优势：简单易用，不需要任何第三方插件。
     * 缺点：不能同时处理大量数据，技术含量过低，属于应急型技巧。
     *
     * @throws URISyntaxException
     * @throws IOException
     * @throws MalformedURLException
     * @throws AWTException
     */
    @Deprecated
    public static void test3() throws MalformedURLException, IOException, URISyntaxException, AWTException {
        // 此方法仅适用于JdK1.6及以上版本
        Desktop.getDesktop().browse(new URL("http://www.flyat.cc/simpleArticle/42e388ff3fd54532afe2d6f9d527f032").toURI());
        Robot robot = new Robot();
        robot.delay(10000);
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
     * 原理就是在现在的awt或者swing上显示网页然后将内容保存为一个图片,没办法控制延迟啊。
     *
     * @throws MalformedURLException
     * @throws IOException
     * @throws InterruptedException
     * @Description
     * @author zhangyd
     * @date 2017年3月13日 上午9:40:53
     */
    @Deprecated
    public static void awt() throws MalformedURLException, IOException, InterruptedException {
        // load the webpage into the editor
        // JEditorPane ed = new JEditorPane(new URL("http://www.google.com"));
        JEditorPane ed = new JEditorPane(new URL("http://www.flyat.cc/simpleArticle/42e388ff3fd54532afe2d6f9d527f032"));
        System.out.println("10");
        Thread.sleep(10000);
        ed.setSize(1000, 1000);

        // create a new image
        BufferedImage image = new BufferedImage(ed.getWidth(), ed.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // paint the editor onto the image
        SwingUtilities.paintComponent(image.createGraphics(), ed, new JPanel(), 0, 0, image.getWidth(),
                image.getHeight());
        // save the image to file
        ImageIO.write((RenderedImage) image, "png", new File("html1.png"));
        System.out.println("ok");
    }
}
