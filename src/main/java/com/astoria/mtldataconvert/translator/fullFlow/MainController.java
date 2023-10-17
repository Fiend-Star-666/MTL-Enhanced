//package com.astoria.mtldataconvert.translator.fullFlow;
//
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class MainController {
//
//   // private GPT4Translator gpt4Translator;
//    private FindAndClickOpenCV findAndClickOpenCV;
//
//    public MainController() throws AWTException {
//        //this.gpt4Translator = new GPT4Translator();
//        this.findAndClickOpenCV = new FindAndClickOpenCV();
//    }
//
//    public void executeBatchTranslation(String chineseFolderPath, String englishFolderPath, int batchSize) {
//        File chineseFolder = new File(chineseFolderPath);
//        File englishFolder = new File(englishFolderPath);
//
//        File[] chineseFiles = chineseFolder.listFiles();
//        File[] englishFiles = englishFolder.listFiles();
//
//        if (chineseFiles == null || englishFiles == null || chineseFiles.length != englishFiles.length) {
//            System.out.println("Mismatch in the number of files or directories not found!");
//            return;
//        }
//
//        for (int i = 0; i < chineseFiles.length; i++) {
//            List<String> chineseLines = readFile(chineseFiles[i].getAbsolutePath());
//            List<String> englishLines = readFile(englishFiles[i].getAbsolutePath());
//
//            for (int j = 0; j < Math.min(chineseLines.size(), englishLines.size()); j += batchSize) {
//                int end = Math.min(j + batchSize, Math.min(chineseLines.size(), englishLines.size()));
//                List<String> batchChinese = chineseLines.subList(j, end);
//                List<String> batchEnglish = englishLines.subList(j, end);
//
//                String prompt = createPrompt(batchChinese, batchEnglish);
//              //  gpt4Translator.sendPrompt(prompt);
//
//                try {
//                    Thread.sleep(90000);  // Wait for 1.5 minutes as specified
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                findAndClickOpenCV.findAndClickTemplate("path_to_template.png");
//            }
//        }
//    }
//
//    private List<String> readFile(String filePath) {
//        try {
//            return Files.readAllLines(Paths.get(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String createPrompt(List<String> chineseLines, List<String> englishLines) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("original:{");
//        chineseLines.forEach(line -> prompt.append(line).append("\n"));
//        prompt.append("}, translated:{");
//        englishLines.forEach(line -> prompt.append(line).append("\n"));
//        prompt.append("}");
//        return prompt.toString();
//    }
//
//    public static void main(String[] args) {
//        try {
//            MainController mainController = new MainController();
//            mainController.executeBatchTranslation("path_to_chinese_folder", "path_to_english_folder", 50);
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//package com.astoria.mtldataconvert.translator.gptui;
//
//public class MainController {
//
//    private final TranslationHelper translationHelper;
//    private final FindAndClickOpenCV findAndClick;
//
//    public MainController() throws Exception {
//        translationHelper = new TranslationHelper();
//        findAndClick = new FindAndClickOpenCV();
//    }
//
//    public void startBatchTranslation() throws InterruptedException {
//        // Simulating the process of getting lines from files.
//        // This will be replaced with actual file reading in the future.
//        String originalText = "original:{<50 lines here>}";
//        String translatedText = "translated:{<50 lines here>}";
//
//        String improvedTranslation = translationHelper.getImprovedTranslation(originalText, translatedText);
//
//        // Wait for 1.5 minutes as specified
//        Thread.sleep(90000);
//
//        findAndClick.findAndClickTemplate("path_to_copy_code_template.png");
//    }
//
//    public static void main(String[] args) {
//        try {
//            MainController controller = new MainController();
//            controller.startBatchTranslation();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
