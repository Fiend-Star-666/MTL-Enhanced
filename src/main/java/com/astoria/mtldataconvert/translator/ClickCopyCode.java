package com.astoria.mtldataconvert.translator;


import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class ClickCopyCode {
    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();

        // Simulate pressing Ctrl + F
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(500); // Wait for search box to appear

        // Type "Copy code"
        String text = "Copy code";
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }

        // Get the current mouse position
        java.awt.Point currentPoint = java.awt.MouseInfo.getPointerInfo().getLocation();

        // Scan downwards from current position to find the button (look for a significant color change as an indicator)
        int startX = (int) currentPoint.getX();
        int startY = (int) currentPoint.getY();

        BufferedImage screenShot = robot.createScreenCapture(new java.awt.Rectangle(startX, startY, 1, 200));
        Color prevColor = robot.getPixelColor(startX, startY);
        for (int y = 0; y < 200; y++) {
            Color currentColor = new Color(screenShot.getRGB(0, y));
            if (!colorsAreClose(prevColor, currentColor, 10)) {
                startY += y;
                break;
            }
            prevColor = currentColor;
        }

        robot.mouseMove(startX, startY);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(500); // Wait

        // Simulate pressing Ctrl + C to copy
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static boolean colorsAreClose(Color c1, Color c2, int threshold) {
        int diffRed = Math.abs(c1.getRed() - c2.getRed());
        int diffGreen = Math.abs(c1.getGreen() - c2.getGreen());
        int diffBlue = Math.abs(c1.getBlue() - c2.getBlue());
        return (diffRed < threshold) && (diffGreen < threshold) && (diffBlue < threshold);
    }
}

