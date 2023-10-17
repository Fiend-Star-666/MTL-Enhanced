//package com.astoria.mtldataconvert.translator.fullFlow;
//
//import com.astoria.mtldataconvert.translator.gptui.OpenAIBrowserInterface;
//import java.nio.file.*;
//import java.io.*;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//public class GPTBatchTranslationService {
//
//    private final String chineseFolderPath;
//    private final String mtlTranslationFolderPath;
//   // private final OpenAIBrowserInterface openAIBrowserInterface;
//
//    public GPTBatchTranslationService(String chineseFolderPath, String mtlTranslationFolderPath) {
//        this.chineseFolderPath = chineseFolderPath;
//        this.mtlTranslationFolderPath = mtlTranslationFolderPath;
//        //this.openAIBrowserInterface = new OpenAIBrowserInterface();
//    }
//
//    public void processFilesInBatches(int batchSize) throws IOException {
//        List<String> chineseFiles = getFilesFromDirectory(chineseFolderPath);
//        List<String> translationFiles = getFilesFromDirectory(mtlTranslationFolderPath);
//
//        for (int i = 0; i < chineseFiles.size(); i++) {
//            String chineseFile = chineseFiles.get(i);
//            String translationFile = translationFiles.get(i);
//
//            List<String> chineseLines = Files.readAllLines(Paths.get(chineseFile));
//            List<String> translatedLines = Files.readAllLines(Paths.get(translationFile));
//
//            for (int j = 0; j < chineseLines.size(); j += batchSize) {
//                List<String> chineseBatch = chineseLines.subList(j, Math.min(j + batchSize, chineseLines.size()));
//                List<String> translatedBatch = translatedLines.subList(j, Math.min(j + batchSize, translatedLines.size()));
//
//                String chineseText = String.join("\n", chineseBatch);
//                String translatedText = String.join("\n", translatedBatch);
//
//                String prompt = "original:{" + chineseText + "}, translated:{" + translatedText + "}";
//
//                // Sending to OpenAI and obtaining revised translation
//                // You can implement the logic to send the prompt to OpenAI and then
//                // store the result as needed using the `openAIBrowserInterface`.
//            }
//        }
//    }
//
//    private List<String> getFilesFromDirectory(String dirPath) throws IOException {
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
//            return StreamSupport.stream(stream.spliterator(), false)
//                    .map(Path::toString)
//                    .collect(Collectors.toList());
//        }
//    }
//}
//
//public class GPTBatchTranslationService {
//
//    private final String chineseFolderPath;
//    private final String mtlTranslationFolderPath;
//    // private final OpenAIBrowserInterface openAIBrowserInterface;
//
//    public GPTBatchTranslationService(String chineseFolderPath, String mtlTranslationFolderPath) {
//        this.chineseFolderPath = chineseFolderPath;
//        this.mtlTranslationFolderPath = mtlTranslationFolderPath;
//        //this.openAIBrowserInterface = new OpenAIBrowserInterface();
//    }
//
//    public void processFilesInBatches(int batchSize) throws IOException {
//        List<String> chineseFiles = getFilesFromDirectory(chineseFolderPath);
//        List<String> translationFiles = getFilesFromDirectory(mtlTranslationFolderPath);
//
//        for (int i = 0; i < chineseFiles.size(); i++) {
//            String chineseFile = chineseFiles.get(i);
//            String translationFile = translationFiles.get(i);
//
//            List<String> chineseLines = Files.readAllLines(Paths.get(chineseFile));
//            List<String> translatedLines = Files.readAllLines(Paths.get(translationFile));
//
//            for (int j = 0; j < chineseLines.size(); j += batchSize) {
//                List<String> chineseBatch = chineseLines.subList(j, Math.min(j + batchSize, chineseLines.size()));
//                List<String> translatedBatch = translatedLines.subList(j, Math.min(j + batchSize, translatedLines.size()));
//
//                String chineseText = String.join("\n", chineseBatch);
//                String translatedText = String.join("\n", translatedBatch);
//
//                String prompt = "original:{" + chineseText + "}, translated:{" + translatedText + "}";
//
//                // Sending to OpenAI and obtaining revised translation
//                // You can implement the logic to send the prompt to OpenAI and then
//                // store the result as needed using the `openAIBrowserInterface`.
//            }
//        }
//    }
//
//    private List<String> getFilesFromDirectory(String dirPath) throws IOException {
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
//            return StreamSupport.stream(stream.spliterator(), false)
//                    .map(Path::toString)
//                    .collect(Collectors.toList());
//        }
//    }
//}