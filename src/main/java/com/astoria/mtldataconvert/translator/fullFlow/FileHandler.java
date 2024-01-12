package com.astoria.mtldataconvert.translator.fullFlow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler {

    public List<String> executeBatchTranslation(String chineseFolderPath, String englishFolderPath, int batchSize) {

        System.out.println("Executing batch translation...");
        List<String> prompts = new ArrayList<>();
        File chineseFolder = new File(chineseFolderPath);
        File englishFolder = new File(englishFolderPath);

        File[] chineseFiles = chineseFolder.listFiles();
        File[] englishFiles = englishFolder.listFiles();

        if (chineseFiles == null || englishFiles == null || chineseFiles.length != englishFiles.length) {
            System.out.println("Mismatch in the number of files or directories not found!");
            return prompts;
        }

        Arrays.sort(chineseFiles, (f1, f2) -> {
            int n1 = extractNumber(f1.getName());
            int n2 = extractNumber(f2.getName());
            return Integer.compare(n1, n2);
        });

        Arrays.sort(englishFiles, (f1, f2) -> {
            int n1 = extractNumber(f1.getName());
            int n2 = extractNumber(f2.getName());
            return Integer.compare(n1, n2);
        });
        System.out.println("Match found in the number of files.");
        System.out.println("Number of files: " + chineseFiles.length);
        for (int i = 0; i < chineseFiles.length; i++) {
            List<String> chineseLines = readFile(chineseFiles[i].getAbsolutePath());
            List<String> englishLines = readFile(englishFiles[i].getAbsolutePath());

            System.out.println("Reading files: " + chineseFiles[i].getAbsolutePath() + " and " + englishFiles[i].getAbsolutePath());

            for (int j = 0; j < Math.min(Objects.requireNonNull(chineseLines).size(), Objects.requireNonNull(englishLines).size()); j += batchSize) {
                int end = Math.min(j + batchSize, Math.min(chineseLines.size(), englishLines.size()));
                List<String> batchChinese = chineseLines.subList(j, end);
                List<String> batchEnglish = englishLines.subList(j, end);

                String prompt = createPrompt(batchChinese, batchEnglish);
                prompts.add(prompt);
            }
        }

        System.out.println("Batch translation completed.");
        return prompts;
    }

    private List<String> readFile(String filePath) {
        System.out.println("Reading file: " + filePath);

        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int extractNumber(String fileName) {
        int num = 0;
        try {
            String numberOnly= fileName.replaceAll("[^0-9]", "");
            num = Integer.parseInt(numberOnly);
        } catch (NumberFormatException e) {
            System.out.println("Error extracting number from file name: " + fileName);
        }
        return num;
    }

    public String createPrompt(List<String> chineseLines, List<String> englishLines) {
        System.out.println("Creating prompt...");

        StringBuilder prompt = new StringBuilder();
        prompt.append("original:{");
        chineseLines.forEach(line -> {
            String sanitizedLine = line.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");  // Keep only Unicode characters
            sanitizedLine = replaceLanguageSpecificBrackets(sanitizedLine);
            prompt.append(sanitizedLine).append("\n");
        });
        prompt.append("}, translated:{");
        englishLines.forEach(line -> {
            String sanitizedLine = line.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");  // Keep only Unicode characters
            sanitizedLine = replaceLanguageSpecificBrackets(sanitizedLine);
            prompt.append(sanitizedLine).append("\n");
        });
        prompt.append("}");

        System.out.println("Prompt created.");
        return prompt.toString();
    }

    private String replaceLanguageSpecificBrackets(String line) {
        // Replace Chinese brackets
        line = line.replace("【", "(");
        line = line.replace("】", ")");

        // Replace Korean brackets
        line = line.replace("《", "(");
        line = line.replace("》", ")");

        // Replace Japanese brackets
        line = line.replace("「", "(");
        line = line.replace("」", ")");
        line = line.replace("『", "(");
        line = line.replace("』", ")");

        return line;
    }



    public void saveClipboardDataToFile(String clipboardData, int chapterCount) {
        String folderPath = "C:\\Users\\gurms\\IdeaProjects\\MTL-Enhanced\\Unlimited-Machine-wars\\"; // replace with your folder path
        System.out.println("Saving clipboard data to file...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath + "Chapter_" + chapterCount + ".txt"))) {
            writer.write(clipboardData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Clipboard data saved to file.");
    }
}
