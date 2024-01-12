package com.astoria.mtldataconvert.translator.fullFlow;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AutomatedBrowserInteraction {

    private static final String CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private final Robot robot;

    public AutomatedBrowserInteraction() throws AWTException {
        this.robot = new Robot();
        System.out.println("Robot instance created.");
    }

    public void initializeWebBrowser() {
        System.out.println("Initializing web browser...");
        String url = "https://chat.openai.com";
        int freePort = 9227;

        launchChromeWithRemoteDebugging(freePort, url);

        System.out.println("Chrome launched and navigated to URL, now performing UI interaction...");

        waitDelay(5000);
    }

    private void launchChromeWithRemoteDebugging(int port, String url) {
        System.out.println("Launching Chrome with remote debugging...");
        String userDataDir = "E:\\testing";
        String cmd = String.format("%s --remote-debugging-port=%d --user-data-dir=%s %s", CHROME_PATH, port, userDataDir, url);
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("Chrome launched successfully.");
        } catch (IOException e) {
            System.out.println("Failed to launch Chrome with remote debugging.");
            throw new RuntimeException("Failed to launch Chrome with remote debugging", e);
        }
    }

    public void waitDelay(int delay) {
        System.out.println("Browser delay initiated. for "+delay+"ms");
        // Assuming you want to wait for Chrome to open and load content before performing actions
        robot.delay(delay);
    }

    public void pasteText(String text) {
        System.out.println("Pasting text...");
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        waitDelay(5000);
        // Simulate a Ctrl+V paste action
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        waitDelay(1211);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        waitDelay(745);
        robot.keyPress(KeyEvent.VK_ENTER);
        waitDelay(2410);
        robot.keyRelease(KeyEvent.VK_ENTER);

        // Get the current mouse position
        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();

        // Move the mouse up by about 100 pixels
        robot.mouseMove(currentMousePosition.x, currentMousePosition.y - 100);

        // Scroll down multiple times after pasting and clicking enter
        for (int i = 0; i < 500; i++) { // Change this number to scroll more or less
            scrollDown();
            waitDelay(5);
        }
    }

    public String getClipboardData() throws IOException, UnsupportedFlavorException {
        System.out.println("Fetching data from clipboard...");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return clipboard.getData(DataFlavor.stringFlavor).toString();
    }

    public void scrollUp() {
        robot.mouseWheel(-1); // Scroll up
    }

    public void scrollDown() {
        robot.mouseWheel(1); // Scroll down
    }

}
