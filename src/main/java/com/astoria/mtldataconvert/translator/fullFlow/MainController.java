package com.astoria.mtldataconvert.translator.fullFlow;


import java.awt.*;
import java.util.List;

public class MainController {

    private static final String TEXT_BOX_TEMPLATE = "TextBoxTemplate.png";
    private static final String COPY_CODE_TEMPLATE = "CopyCodeTemplate.png";
    private static final String CORRECT_GPT_IDENTIFIER_TEMPLATE = "CorrectGptIdentifier.png";
    private static final String CREATE_NEW_CHAT_TEMPLATE = "CreateNewChatTemplate.png";
    private static final String REFRESH_PAGE_TEMPLATE = "RefreshPageTemplate.png";
    private static final String WAIT_BOX_TEMPLATE = "WaitBoxTemplate.png";

    private static final int INITIAL_BROWSER_DELAY = 5000; // 5 seconds
    private static final int FINAL_BROWSER_DELAY = 45000; // 45 seconds
    private static final String CHINESE_FOLDER_PATH = "E:\\Novel\\my-post-apocalyptic-shelter-levels-up-infinitely-chapter\\original";
    private static final String ENGLISH_FOLDER_PATH = "E:\\Novel\\my-post-apocalyptic-shelter-levels-up-infinitely-chapter\\translated";

    private final FindAndClickOpenCV findAndClickOpenCV;
    private final AutomatedBrowserInteraction automatedBrowserInteraction;
    private final FileHandler fileHandler;

    /*
    public void checkAndWaitBox(String prompt, boolean isPromptPasted) throws InterruptedException {

        for (int i = 0; i < 5; i++) {
            if (findAndClickOpenCV.findTemplate(WAIT_BOX_TEMPLATE)) {
                System.out.println("Wait box found. Waiting for 30 minutes...");
                Thread.sleep(30 * 60 * 1000); // Wait for 30 minutes

                findAndClickOpenCV.findAndClickTemplate(REFRESH_PAGE_TEMPLATE, automatedBrowserInteraction);
                Thread.sleep(15000); // Wait for 15 second before retrying
                System.out.println("Retrying to find wait box for 5 minutes...");
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 5 * 60 * 1000) { // Retry for 5 minutes
                    if (!findAndClickOpenCV.findTemplate(WAIT_BOX_TEMPLATE)) {
                        return; // If wait box is not found, return
                    }
                    Thread.sleep(1000); // Wait for 1 second before retrying
                }

                if (!isPromptPasted) {
                    automatedBrowserInteraction.pasteText(prompt);
                }
            }
        }

        // If the WaitBoxTemplate is not found after 5 checks
        System.out.println("Wait box not found after 5 checks. Finding text box template...");
        findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE, automatedBrowserInteraction);
        System.out.println("Pasting the prompt again...");
        if (!isPromptPasted) {
            //automatedBrowserInteraction.pasteText(prompt);
        }
        System.out.println("Waiting for 15 seconds...");
        automatedBrowserInteraction.waitDelay(15 * 1000); // Wait for 15 seconds
        checkAndWaitBox(prompt, true); // Check for the WaitBoxTemplate again

    }
    */


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
            mainController.automatedBrowserInteraction.waitDelay(INITIAL_BROWSER_DELAY);
            System.out.println("Finding and clicking text box template...");
//            mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE);
            mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE, mainController.automatedBrowserInteraction);

            List<String> prompts;
            System.out.println("Executing batch translation...");
            prompts = mainController.fileHandler.executeBatchTranslation(CHINESE_FOLDER_PATH, ENGLISH_FOLDER_PATH, 50);


            // i=3 done start from 4 and try to incorporate the scroller and an absolute pixel clicker for the text box. and change the custom instructions, so it does not give any of the
            //other things in the code blocks.
            int totalPrompts = prompts.size();
            System.out.println("Total prompts: " + totalPrompts);
            int newTranslationStartIndex = 1;
            for (int i = 186; i < prompts.size(); i++) {

//                if (!mainController.findAndClickOpenCV.findTemplate(CORRECT_GPT_IDENTIFIER_TEMPLATE)) {
//                    System.out.println("Correct GPT Identifier not found. Skipping iteration...");
//                    continue; // Skip the current iteration if the template is not found
//                }

                System.out.println("Finding and clicking text box template again");

                mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE, mainController.automatedBrowserInteraction);

                System.out.println("prompt: " + prompts.get(i) + " i: " + i);
                String prompt = prompts.get(i);


                if (newTranslationStartIndex % 5 == 0) { // After every 5 iterations
                    System.out.println("Finding and clicking create new chat template...");
                    mainController.findAndClickOpenCV.findAndClickTemplate(CREATE_NEW_CHAT_TEMPLATE, mainController.automatedBrowserInteraction);
                    mainController.automatedBrowserInteraction.waitDelay(15 * 1000); // Wait for 15 seconds
                }

                mainController.findAndClickOpenCV.findAndClickTemplate(TEXT_BOX_TEMPLATE, mainController.automatedBrowserInteraction);

                mainController.automatedBrowserInteraction.pasteText(prompt);
                mainController.automatedBrowserInteraction.waitDelay(INITIAL_BROWSER_DELAY * 2);

                //mainController.checkAndWaitBox(prompt, false);

                mainController.automatedBrowserInteraction.waitDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.waitDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.waitDelay(FINAL_BROWSER_DELAY);
                mainController.automatedBrowserInteraction.waitDelay(FINAL_BROWSER_DELAY);

                boolean copiedCorrectly = false;
                String clipboardData = prompt;

                long startTime = System.currentTimeMillis(); // Fetch starting time
                long timeout = 1000000; // 10 seconds in milliseconds

                while (!copiedCorrectly) {
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    if (elapsedTime >= timeout) {
                        System.out.println("Timed out waiting for correct copy.");
                        break; // Break the loop if timed out
                    }

                    //mainController.findAndClickOpenCV.findAndClickTemplate(COPY_CODE_TEMPLATE);
                    mainController.findAndClickOpenCV.findAndClickTemplate(COPY_CODE_TEMPLATE, mainController.automatedBrowserInteraction);

                    clipboardData = mainController.automatedBrowserInteraction.getClipboardData();

                    if (!clipboardData.equals(prompt)) {
                        copiedCorrectly = true;
                    }
                }
                System.out.println("Clipboard data: " + clipboardData);

                //Save clipboard data to file
                if (!clipboardData.equals(prompt)) mainController.fileHandler.saveClipboardDataToFile(clipboardData, i);

                newTranslationStartIndex+=1;
            }

        } catch (Exception e) {
            System.out.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Main method ended.");
    }
}
