package com.astoria.mtldataconvert.translator.legacy;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AutomatedBrowserInteractionOld {

    private static final String CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";

    public static void main(String[] args) throws InterruptedException {
        AutomatedBrowserInteractionOld automatedBrowserInteractionOld = new AutomatedBrowserInteractionOld();
        automatedBrowserInteractionOld.initializeWebDriver();
        //automatedBrowserInteractionOld.pointerLocation();
    }

    public void initializeWebDriver() {
        String url = "https://chat.openai.com";
        int freePort = 9222; // Hard-coded for the example, consider using a method to find a free port

        launchChromeWithRemoteDebugging(freePort, url);

        System.out.println("Chrome launched and navigated to URL, now performing UI interaction...");

        controlBrowser();
    }

    private void launchChromeWithRemoteDebugging(int port, String url) {
        String userDataDir = "E:\\testing";
        String cmd = String.format("%s --remote-debugging-port=%d --user-data-dir=%s %s", CHROME_PATH, port, userDataDir, url);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Failed to launch Chrome with remote debugging", e);
        }
    }

    public void pointerLocation() throws InterruptedException {
        while (true) {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            Point point = pointerInfo.getLocation();
            int mouseX = (int) point.getX();
            int mouseY = (int) point.getY();
            System.out.println("Mouse position: X=" + mouseX + " Y=" + mouseY);

            Thread.sleep(1000); // Pause for 1 second before next read
        }
    }

    public void controlBrowser() {
        try {
            Robot robot = new Robot();

            // Wait for Chrome to open and load content
            robot.delay(5000); // Waiting for 10 seconds for demonstration purposes. This might need adjustment.

            // Coordinate positioning would depend on the screen resolution and Chrome window position
            // The example uses arbitrary values and should be adjusted for actual use
            int xCoord = 481; // These coordinates are examples, adjust as necessary
            int yCoord = 1047;

            // Move the mouse to the coordinates (this should be the text box location)
            robot.mouseMove(xCoord, yCoord);

            // Simulate a mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // Pause execution to mimic a more human-like interaction, allowing UI elements to respond
            robot.delay(2000); // Pausing for 2 seconds for demonstration

            // Simulate typing or pasting into the text box
            pasteText(robot, "The text you want to paste");

            // Simulate pressing the "Enter" key
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
    }

    private void pasteText(Robot robot, String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        // Simulate a Ctrl+V paste action
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}
