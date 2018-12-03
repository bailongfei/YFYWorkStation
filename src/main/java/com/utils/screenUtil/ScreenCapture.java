package com.utils.screenUtil;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 一个简单的屏幕抓图
 */
public class ScreenCapture {

    private static final Logger logger = Logger.getLogger(ScreenCapture.class.getName());

    private int x1, y1, x2, y2;
    private int recX, recY, recH, recW; // 截取的图像
    private boolean haveDragged = false;
    private final BackgroundImage labFullScreenImage = new BackgroundImage();
    private Robot robot;
    private BufferedImage fullScreenImage;
    private BufferedImage pickedImage;

    public ScreenCapture() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.log(Level.SEVERE, "无法初始化Robot!", e);
        }
    }

    /**
     * 捕捉全屏慕
     *
     */
    public void captureFullScreen() {
        fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        pickedImage = fullScreenImage;
    }

    /**
     * 捕捉屏幕的一个矫形区域
     */
    public void captureRectangle() {
        labFullScreenImage.reset();
        fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIcon icon = new ImageIcon(fullScreenImage);
        labFullScreenImage.setIcon(icon);
        showDialog();
    }

    /**
     * 使用指定的矩形区域来截图
     */
    public void captureRectangle(Rectangle r) {
        labFullScreenImage.reset();
        fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        recX = (int) r.getX();
        recY = (int) r.getY();
        recW = (int) r.getWidth();
        recH = (int) r.getHeight();
        pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
    }

    public void setOldRectangle(Rectangle r) {
        labFullScreenImage.setOldRectangle(r);
    }

    private void showDialog() {
        final JDialog dialog = new JDialog();
        JPanel cp = (JPanel) dialog.getContentPane();
        cp.setLayout(new BorderLayout());
        labFullScreenImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (haveDragged) {
                    pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
                    dialog.setVisible(false);
                    dialog.dispose();
                }
                haveDragged = false;
            }
        });
        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                haveDragged = true;
                x2 = e.getX();
                y2 = e.getY();
                int maxX = Math.max(x1, x2);
                int maxY = Math.max(y1, y2);
                int minX = Math.min(x1, x2);
                int minY = Math.min(y1, y2);
                recX = minX;
                recY = minY;
                recW = maxX - minX;
                recH = maxY - minY;
                labFullScreenImage.drawRectangle(recX, recY, recW, recH);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                labFullScreenImage.drawCross(e.getX(), e.getY());
            }
        });
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pickedImage = null;
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        });
        cp.add(BorderLayout.CENTER, labFullScreenImage);

        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        dialog.setAlwaysOnTop(true);
        dialog.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialog.setUndecorated(true);
        dialog.setSize(dialog.getMaximumSize());
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    /**
     * 得到捕捉后的BufferedImage
     */
    public BufferedImage getPickedImage() {
        return pickedImage;
    }

    /**
     * 得到捕捉后的Icon
     */
    public ImageIcon getPickedIcon() {
        return new ImageIcon(getPickedImage());
    }

    public Rectangle getRectangle() {
        return new Rectangle(recX, recY, recW, recH);
    }

}
