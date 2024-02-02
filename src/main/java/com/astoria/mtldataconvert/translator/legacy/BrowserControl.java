package com.astoria.mtldataconvert.translator.legacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Component
public class BrowserControl {

    private ChatGPTAutomation chatGPTAutomation;
    @Autowired
    public void ChatGPTClient(ChatGPTAutomation chatGPTAutomation) {
        this.chatGPTAutomation = chatGPTAutomation;
    }

    public BrowserControl(ChatGPTAutomation chatGPTAutomation) {
        this.chatGPTAutomation = chatGPTAutomation;
    }


    public void controlBrowser() {
        try {
            // Assuming you have called launchChromeWithRemoteDebugging prior to this

            Robot robot = new Robot();

            // Wait for the browser to load
            robot.delay(5000);  // This delay is just for demonstration

            // These x and y might correspond to the address bar's position, it depends on screen resolution
            int xCoord = 500;
            int yCoord = 100;

            // Move the mouse to the coordinates (this should be the text box, but it might change)
            robot.mouseMove(xCoord, yCoord);

            // "Click" the mouse
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // Wait a bit to simulate a real user's pace
            robot.delay(2000);  // This delay is just for demonstration

            // Maybe you want to "paste" something here
            pasteText(robot, "The text you want to paste");

            // Press "Enter"
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            e.printStackTrace();
            // Handle exception; perhaps print an error message and gracefully shut down
        }
    }

    private void pasteText(Robot robot, String text) {
        // This string would be pasted into the active text box
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, (ClipboardOwner) null);

        // This is a "Ctrl+V" key combination to paste
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    // Here you would have your methods like launchChromeWithRemoteDebugging and others
}
