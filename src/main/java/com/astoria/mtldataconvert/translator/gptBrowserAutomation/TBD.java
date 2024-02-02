//package com.astoria.mtldataconvert.translator.gptBrowserAutomation;
//
//public class Constants {
//    public static final String TEXT_BOX_TEMPLATE = "TextBoxTemplateNEW.png";
//    public static final String COPY_CODE_TEMPLATE = "CopyCodeTemplate.png";
//    public static final String CORRECT_GPT_IDENTIFIER_TEMPLATE = "CorrectGptIdentifier.png";
//    public static final String CREATE_NEW_CHAT_TEMPLATE = "CreateNewChatTemplate.png";
//    public static final String CONTINUE_GENERATING_TEMPLATE = "ContinueGeneratingTemplate.png";
//    public static final String ERROR_GENERATING_RESPONSE_TEMPLATE = "ErrorGeneratingResponseTemplate.png";
//    public static final String NETWORK_ERROR_TEMPLATE = "NetworkErrorTemplate.png";
//    public static final String REFRESH_PAGE_TEMPLATE = "RefreshPageTemplate.png";
//    public static final String WAIT_BOX_TEMPLATE = "WaitBoxTemplate.png";
//
//    public static final int INITIAL_BROWSER_DELAY = 5000;
//    public static final int FINAL_BROWSER_DELAY = 45000;
//
//    public static final String CHINESE_FOLDER_PATH = "E:\\Novel\\Looking-Forward-In-Another-World\\original";
//    public static final String ENGLISH_FOLDER_PATH = "E:\\Novel\\Looking-Forward-In-Another-World\\translated";
//}
//
//
//package com.astoria.mtldataconvert.translator.gptBrowserAutomation;
//
//        import java.awt.*;
//
//public class MainController {
//
//    private final FindAndClickOpenCV findAndClickOpenCV;
//    private final AutomatedBrowserInteraction automatedBrowserInteraction;
//    private final FileHandler fileHandler;
//
//    public MainController() throws AWTException {
//        System.out.println("Initializing MainController...");
//        this.findAndClickOpenCV = new FindAndClickOpenCV();
//        this.automatedBrowserInteraction = new AutomatedBrowserInteraction();
//        this.fileHandler = new FileHandler();
//    }
//
//    public static void main(String[] args) {
//        System.out.println("Main method started...");
//
//        try {
//            MainController mainController = new MainController();
//            mainController.initializeBrowserAndTemplates();
//
//            List<String> prompts = mainController.executeBatchTranslation();
//
//            mainController.processPrompts(prompts);
//
//        } catch (Exception e) {
//            System.out.println("An exception occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
//        System.out.println("Main method ended.");
//    }
//
//    private void initializeBrowserAndTemplates() {
//        System.out.println("Initializing Web Browser...");
//        automatedBrowserInteraction.waitDelay(Constants.INITIAL_BROWSER_DELAY);
//        System.out.println("Finding and clicking text box template...");
//        findAndClickOpenCV.findAndClickTemplate(Constants.TEXT_BOX_TEMPLATE, automatedBrowserInteraction);
//    }
//
//    private List<String> executeBatchTranslation() {
//        System.out.println("Executing batch translation...");
//        return fileHandler.executeBatchTranslation(Constants.CHINESE_FOLDER_PATH, Constants.ENGLISH_FOLDER_PATH, 50);
//    }
//
//    private void processPrompts(List<String> prompts) {
//        int totalPrompts = prompts.size();
//        System.out.println("Total prompts: " + totalPrompts);
//        int newTranslationStartIndex = 1;
//
//        for (int i = 649; i < prompts.size(); i++) {
//            processSinglePrompt(prompts, newTranslationStartIndex, i);
//            newTranslationStartIndex += 1;
//        }
//    }
//
//    private void processSinglePrompt(List<String> prompts, int newTranslationStartIndex, int i) {
//        System.out.println("Finding and clicking text box template again");
//        findAndClickOpenCV.findAndClickTemplate(Constants.TEXT_BOX_TEMPLATE, automatedBrowserInteraction);
//
//        System.out.println("prompt: " + prompts.get(i) + " i: " + i);
//        String prompt = prompts.get(i);
//
//        if (newTranslationStartIndex % 12 == 0) {
//            createNewChat();
//        }
//
//        findAndClickOpenCV.findAndClickTemplate(Constants.TEXT_BOX_TEMPLATE, automatedBrowserInteraction);
//
//        automatedBrowserInteraction.pasteText(prompt);
//        automatedBrowserInteraction.waitDelay(Constants.INITIAL_BROWSER_DELAY * 2);
//
//        automatedBrowserInteraction.waitDelay(Constants.FINAL_BROWSER_DELAY);
//        automatedBrowserInteraction.waitDelay(Constants.FINAL_BROWSER_DELAY);
//        automatedBrowserInteraction.waitDelay(Constants.FINAL_BROWSER_DELAY);
//
//        handleErrors();
//
//        boolean copiedCorrectly = false;
//        String clipboardData = prompt;
//
//        clipboardData = copyPromptToClipboard(prompt, copiedCorrectly, clipboardData);
//
//        System.out.println("Clipboard data: " + clipboardData);
//
//        if (!clipboardData.equals(prompt)) fileHandler.saveClipboardDataToFile(clipboardData, i);
//    }
//
//    private void createNewChat() {
//        System.out.println("Finding and clicking create new chat template...");
//        findAndClickOpenCV.findAndClickTemplate(Constants.CREATE_NEW_CHAT_TEMPLATE, automatedBrowserInteraction);
//        automatedBrowserInteraction.waitDelay(15 * 1000);
//    }
//
//    private void handleErrors() {
//        for (int t = 0; t < 10; t++) {
//            if (findAndClickOpenCV.findTemplate(Constants.CONTINUE_GENERATING_TEMPLATE)) {
//                automatedBrowserInteraction.waitDelay(Constants.FINAL_BROWSER_DELAY);
//            }
//            if (findAndClickOpenCV.findTemplate(Constants.ERROR_GENERATING_RESPONSE_TEMPLATE) || findAndClickOpenCV.findTemplate(Constants.NETWORK_ERROR_TEMPLATE)) {
//                alertUser();
//            }
//            automatedBrowserInteraction.waitDelay(1000);
//        }
//        automatedBrowserInteraction.waitDelay(Constants.FINAL_BROWSER_DELAY);
//    }
//
//    private void alertUser() {
//        for (int p = 0; p < 50; p++) {
//            Toolkit.getDefaultToolkit().beep();
//            automatedBrowserInteraction.waitDelay(1000);
//        }
//    }
//
//    private String copyPromptToClipboard(String prompt, boolean copiedCorrectly, String clipboardData) {
//        long startTime = System.currentTimeMillis();
//        long timeout = 1000000;
//
//        while (!copiedCorrectly) {
//            long elapsedTime = System.currentTimeMillis() - startTime;
//
//            if (elapsedTime >= timeout) {
//                System.out.println("Timed out waiting for correct copy.");
//                break;
//            }
//
//            findAndClickOpenCV.findAndClickTemplate(Constants.COPY_CODE_TEMPLATE, automatedBrowserInteraction);
//
//            clipboardData = automatedBrowserInteraction.getClipboardData();
//
//            if (!clipboardData.equals(prompt)) {
//                copiedCorrectly = true;
//            }
//        }
//        return clipboardData;
//    }
//}