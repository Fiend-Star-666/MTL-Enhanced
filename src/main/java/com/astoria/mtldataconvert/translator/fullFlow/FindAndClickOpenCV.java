package com.astoria.mtldataconvert.translator.fullFlow;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static java.awt.Color.gray;

public class FindAndClickOpenCV {


    private Robot robot;

    public FindAndClickOpenCV() throws AWTException {
        this.robot = new Robot();
    }

    public void findAndClickTemplate(String templatePath) {
        // Load the image of the full screen
        Mat screenMat = getFullScreenShot();
        Mat gray = new Mat();

        Imgproc.cvtColor(screenMat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

        // Load the template
        Mat template = Imgcodecs.imread(templatePath);
        Imgproc.GaussianBlur(template, template, new Size(5, 5), 0);

        // Match the template to the screen
       // Mat result = matchTemplate(screenMat, template);

        // Find the point of the best match and click there
        //Point matchPoint = findBestMatch(result);
        //clickOnPoint(matchPoint);

        double bestMatchValue = 0;
        Point bestMatchLoc = null;
        double factor = 1.0;
        double scaleStep = 0.05;  // You can adjust this


        for (double scale = 0.8; scale <= 1.2; scale += scaleStep) {
            Mat resizedTemplate = new Mat();
            Imgproc.resize(template, resizedTemplate, new Size(), scale, scale, Imgproc.INTER_AREA);
            Mat result = new Mat();
            Imgproc.matchTemplate(gray, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            if (mmr.maxVal > bestMatchValue) {
                bestMatchValue = mmr.maxVal;
                bestMatchLoc = mmr.maxLoc;
                factor = scale;
            }
        }

        if (bestMatchValue > 0.8) {  // Set a threshold for matching confidence
            robot.mouseMove((int) (bestMatchLoc.x + template.cols() * factor / 2), (int) (bestMatchLoc.y + template.rows() * factor / 2));
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("Operation completed.");
        } else {
            System.out.println("No good match found.");
        }

    }

    private Mat getFullScreenShot() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        BufferedImage capture = robot.createScreenCapture(screenRect);

        return bufferedImageToMat(capture);
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        System.out.println("Converting BufferedImage to Mat...");
        BufferedImage convertedImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = convertedImg.createGraphics();
        g2d.drawImage(bi, 0, 0, null);
        g2d.dispose();

        Mat mat = new Mat(convertedImg.getHeight(), convertedImg.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) convertedImg.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    private Mat matchTemplate(Mat source, Mat template) {
        int resultCols = source.cols() - template.cols() + 1;
        int resultRows = source.rows() - template.rows() + 1;
        Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);

        Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED);
        return result;
    }

    private Point findBestMatch(Mat result) {
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        return mmr.maxLoc;
    }

    private void clickOnPoint(Point point) {
        robot.mouseMove((int) point.x, (int) point.y);
        robot.delay(1000);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
