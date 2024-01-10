package com.astoria.mtldataconvert.translator.fullFlow;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private static final String TEXT_BOX_TEMPLATE = "TextBoxTemplate.png";
    private static final String COPY_CODE_TEMPLATE = "CopyCodeTemplate.png";
    private static final int INITIAL_BROWSER_DELAY = 5000; // 5 seconds
    private static final int FINAL_BROWSER_DELAY = 45000; // 45 seconds
    private static final String CHINESE_FOLDER_PATH = "E:\\Novel\\my-post-apocalyptic-shelter-levels-up-infinitely-chapter\\original";
    private static final String ENGLISH_FOLDER_PATH = "E:\\Novel\\my-post-apocalyptic-shelter-levels-up-infinitely-chapter\\translated";

    private FindAndClickOpenCV findAndClickOpenCV;
    private AutomatedBrowserInteraction automatedBrowserInteraction;
    private FileHandler fileHandler;

    public MainController() throws AWTException {
        System.out.println("Initializing MainController...");
        this.findAndClickOpenCV = new FindAndClickOpenCV();
        this.automatedBrowserInteraction = new AutomatedBrowserInteraction();
        this.fileHandler = new FileHandler();
    }

    public static void main(String[] args) {
        System.out.println("Main method started...");

        try {
            MainController mainController = new MainController();
            System.out.println("Initializing Web Browser...");
            //mainController.automatedBrowserInteraction.initializeWebBrowser();
            System.out.println("Applying initial browser delay...");
            mainController.automatedBrowserInteraction.browerDelay(INITIAL_BROWSER_DELAY);
            System.out.println("Finding and clicking text box template...");
            mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE);


            List<String> prompts = new ArrayList<>();
            System.out.println("Executing batch translation...");
            prompts = mainController.fileHandler.executeBatchTranslation(CHINESE_FOLDER_PATH, ENGLISH_FOLDER_PATH, 50);


           // i=3 done start from 4 and try to incorporate the scroller and a absolute pixel clicker for the text box. and change the custom instructions so it does not give any of the
            //other things in the code blocks.
            for (int i = 100; i < prompts.size(); i++) {

                System.out.println("Finding and clicking text box template again");
                mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE);
                mainController.automatedBrowserInteraction.browerDelay(INITIAL_BROWSER_DELAY);

                mainController.automatedBrowserInteraction.browerDelay(INITIAL_BROWSER_DELAY);

                System.out.println("prompt: " + prompts.get(i) + " i: " + i);
                String prompt = prompts.get(i);
                mainController.automatedBrowserInteraction.browerDelay(INITIAL_BROWSER_DELAY+INITIAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.pasteText(prompt);
                mainController.automatedBrowserInteraction.browerDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.browerDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.browerDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.browerDelay(FINAL_BROWSER_DELAY);

                boolean copiedCorrectly = false;
                String clipboardData = prompt;

                long startTime = System.currentTimeMillis(); // Fetch starting time
                long timeout = 100000; // 10 seconds in milliseconds

                while (!copiedCorrectly) {
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    if (elapsedTime >= timeout) {
                        System.out.println("Timed out waiting for correct copy.");
                        break; // Break the loop if timed out
                    }

                    mainController.findAndClickOpenCV.findAndClickTemplate(COPY_CODE_TEMPLATE);
                    clipboardData = mainController.automatedBrowserInteraction.getClipboardData();

                    if (!clipboardData.equals(prompt)) {
                        copiedCorrectly = true;
                    }
                }
                System.out.println("Clipboard data: " + clipboardData);

                //Save clipboard data to file
                if(!clipboardData.equals(prompt))
                    mainController.fileHandler.saveClipboardDataToFile(clipboardData, i);
            }

        } catch (Exception e) {
            System.out.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Main method ended.");
    }
}
