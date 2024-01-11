package com.astoria.mtldataconvert.translator.fullFlow;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FindAndClickOpenCV {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV native library.");
    }

    private Robot robot;

    public FindAndClickOpenCV() throws AWTException {
        this.robot = new Robot();
        System.out.println("Robot object created.");
    }

    public void findAndClickTemplate(String templatePath, AutomatedBrowserInteraction automatedBrowserInteraction) {

        System.out.println("Starting findAndClickTemplate method.");

        // Load the image of the full screen
        Mat screenMat = getFullScreenShot();


        Mat gray = new Mat();
        Imgproc.cvtColor(screenMat, gray, Imgproc.COLOR_BGR2GRAY);
        gray.convertTo(gray, CvType.CV_8U);

        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

        // Load the template
        Mat template = Imgcodecs.imread(templatePath);
        System.out.println("Template loaded.");

        Mat templateGray = new Mat();
        Imgproc.cvtColor(template, templateGray, Imgproc.COLOR_BGR2GRAY);
        templateGray.convertTo(templateGray, CvType.CV_8U);
        Imgproc.GaussianBlur(templateGray, templateGray, new Size(5, 5), 0);

        double bestMatchValue = 0;
        Point bestMatchLoc = null;
        double factor = 1.0;
        double scaleStep = 0.05;

        for (double scale = 0.8; scale <= 1.2; scale += scaleStep) {
            Mat resizedTemplate = new Mat();
            Imgproc.resize(templateGray, resizedTemplate, new Size(), scale, scale, Imgproc.INTER_AREA);
            resizedTemplate.convertTo(resizedTemplate, CvType.CV_8U);

            Mat result = new Mat();
            result.convertTo(result, CvType.CV_8U);

            Imgproc.matchTemplate(gray, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);

            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            if (mmr.maxVal > bestMatchValue) {
                bestMatchValue = mmr.maxVal;
                bestMatchLoc = mmr.maxLoc;
                factor = scale;
            }
        }

        if (bestMatchValue > 0.8) {
            robot.mouseMove((int) (bestMatchLoc.x + template.cols() * factor / 2), (int) (bestMatchLoc.y + template.rows() * factor / 2));
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("Operation completed.");
        } else {
            System.out.println("No good match found. Scrolling...");
            if (templatePath.toLowerCase().contains("copycode")) {
                automatedBrowserInteraction.scrollUp();
            } else {
                automatedBrowserInteraction.scrollDown();
            }
            findAndClickTemplate(templatePath, automatedBrowserInteraction);
        }
    }

    private Mat getFullScreenShot() {
        System.out.println("Taking a full-screen shot.");
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        BufferedImage capture = robot.createScreenCapture(screenRect);

        return bufferedImageToMat(capture);
    }

    public Mat bufferedImageToMat(BufferedImage bi) {
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

}
