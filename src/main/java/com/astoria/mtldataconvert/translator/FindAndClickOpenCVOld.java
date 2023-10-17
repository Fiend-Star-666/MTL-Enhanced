package com.astoria.mtldataconvert.translator;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FindAndClickOpenCVOld {

    // if maven reloads add the opencv.jar again via project module dependencies
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV native library.");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing Robot...");
        Robot robot = new Robot();

        // Capture the entire screen
        System.out.println("Capturing screenshot...");
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = new Robot().createScreenCapture(screenRect);

        System.out.println("Converting screenshot to Mat format...");
        Mat screenshot = bufferedImageToMat(bufferedImage);

        // Convert screenshot to grayscale for better text detection
        Mat gray = new Mat();
        System.out.println("Converting screenshot to grayscale...");
        Imgproc.cvtColor(screenshot, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);


        // Load the "" template
        System.out.println("Loading '' template...");
        Mat template = Imgcodecs.imread("template.png", Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.GaussianBlur(template, template, new Size(5, 5), 0);

        // Multi-scale template matching
        double bestMatchValue = 0;
        org.opencv.core.Point bestMatchLoc = null;
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
}
