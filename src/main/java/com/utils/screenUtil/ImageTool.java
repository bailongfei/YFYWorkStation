package com.utils.screenUtil;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片保存
 */
public class ImageTool {

    public static void saveAsPNG(BufferedImage image, File file) throws IOException {
        ImageIO.write(image, "PNG", file);
    }

    public static void saveAsJPEG(BufferedImage image, File file) throws IOException {
        ImageIO.write(image, "JPEG", file);
    }

    public static void saveAs(BufferedImage image, String format, OutputStream out) throws IOException {
        ImageIO.write(image, format, out);
    }

    public static WritableImage convert(BufferedImage bf) {
        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bf.getWidth(); x++) {
                for (int y = 0; y < bf.getHeight(); y++) {
                    pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
        }
        return wr;
    }
}
