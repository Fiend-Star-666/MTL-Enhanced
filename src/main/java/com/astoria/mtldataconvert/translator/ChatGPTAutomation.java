package com.astoria.mtldataconvert.translator;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Service
@Lazy
public class ChatGPTAutomation {

    private WebDriver driver;
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\gurms\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    private static final String CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";


    public void initializeWebDriver() {
        String url = "https://chat.openai.com";
        int freePort = findAvailablePort();
        launchChromeWithRemoteDebugging(freePort, url);
       // waitForHumanVerification();
        System.out.println("creating driver");
       // setupWebDriver(freePort);
    }



    private int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Failed to find an available port", e);
        }
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

    private void setupWebDriver(int port) {
        try {

            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
            ChromeOptions options = new ChromeOptions();
            options.setBinary(CHROME_PATH);
            String remoteDebuggingAddress = "localhost:" + port; // or use "0.0.0.0:" + port for non-local connections
            options.addArguments("--remote-debugging-address=" + remoteDebuggingAddress);
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:" + port);
            options.addArguments("--whitelisted-ips=");
            driver = new ChromeDriver(options);
            System.out.println("driver created");

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForHumanVerification() {
        System.out.println("You need to manually complete the log-in or the human verification if required.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter 'y' if you have completed the log-in or the human verification, or 'n' to check again: ");
            String userInput = scanner.nextLine().toLowerCase();
            if (userInput.equals("y")) {
                System.out.println("Continuing with the automation process...");
                break;
            } else if (userInput.equals("n")) {
                System.out.println("Waiting for you to complete the human verification...");
                try {
                    Thread.sleep(5000);  // You can adjust the waiting time as needed
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to wait", e);
                }
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    public void sendPromptToChatGPT(String prompt) {
        //WebElement inputBox = driver.findElement(By.xpath("//textarea[contains(@placeholder, 'Send a message')]"));
        WebElement inputBox = driver.findElement(By.xpath("//div[contains(@class, 'flex flex-col')]//textarea[@id='prompt-textarea']"));

        inputBox.sendKeys(prompt + Keys.RETURN);
        try {
            Thread.sleep(20000);  // Wait for 20 seconds
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to wait", e);
        }
    }

    public List<String> returnChatGPTConversation() {
        List<WebElement> elements = driver.findElements(By.cssSelector("div.text-base"));
        List<String> conversation = new ArrayList<>();
        for (WebElement element : elements) {
            conversation.add(element.getText());
        }
        return conversation;
    }

    public void saveConversation(String fileName) {
        List<String> conversation = returnChatGPTConversation();
        File directory = new File("conversations");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter file = new FileWriter(new File(directory, fileName), true)) {
            String delimiter = "|^_^|";
            for (int i = 0; i < conversation.size(); i += 2) {
                file.write(String.format("prompt: %s\nresponse: %s\n\n%s\n\n",
                        conversation.get(i), conversation.get(i + 1), delimiter));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save conversation", e);
        }
    }

    public String returnLastResponse() {
        List<WebElement> responseElements = driver.findElements(By.cssSelector("div.text-base"));
        return responseElements.get(responseElements.size() - 1).getText();
    }

    public void quit() {
        System.out.println("Closing the browser...");
        driver.quit();
    }
}
