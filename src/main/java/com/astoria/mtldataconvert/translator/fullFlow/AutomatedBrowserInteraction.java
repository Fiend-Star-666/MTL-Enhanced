package com.astoria.mtldataconvert.translator.fullFlow;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AutomatedBrowserInteraction {

    private static final String CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private Robot robot;

    public AutomatedBrowserInteraction() throws AWTException {
        this.robot = new Robot();
    }

    public void initializeWebDriver() {
        String url = "https://chat.openai.com";
        int freePort = 9222;

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

    public void controlBrowser() {
        // Assuming you want to wait for Chrome to open and load content before performing actions
        robot.delay(5000);

        // Simulate typing or pasting into the text box
        pasteText("The text you want to paste");

        // Simulate pressing the "Enter" key
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    private void pasteText(String text) {
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