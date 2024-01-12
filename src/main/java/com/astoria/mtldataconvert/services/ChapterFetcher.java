package com.astoria.mtldataconvert.services;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@Getter
@Setter
public class ChapterFetcher {

    @Value("${URL_BASE}")
    private String urlBase;

    @Value("${URL_NOVEL}")
    private String urlNovel;


    public void fetchChapter(int chapterNumber) {

        String url = this.urlBase + this.urlNovel + "book-14-chapter-" + chapterNumber;
        System.out.println("Fetching chapter " + chapterNumber + " from " + url);
        try {

            URL requestUrl = new URL(url);

            // Open connection and make the GET request
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

            Properties properties = PropertyLoaderHeaders.loadProperties();

            setRequestHeaders(connection, properties);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String linee;

                while ((linee = reader.readLine()) != null) {
                    response.append(linee);
                }

                reader.close();

                // Parse the HTML response
                Document document = Jsoup.parse(response.toString());

                // Find the desired <div> element
                Element divElement = document.selectFirst("div.chapter-body[v-pre]");

                Elements originalElements = document.select("sentence.original");

                Elements translatedElements = document.select("sentence.translated");

                // Extract the content of the <div> element
                assert divElement != null;

                List<String> originalList = new ArrayList<>();
                List<String> translatedList = new ArrayList<>();

                for (Element originalLine : originalElements) {
                    originalList.add(originalLine.text());
                }

                for (Element translatedLine : translatedElements) {
                    translatedList.add(translatedLine.text());
                }

                // Save the content to a file
                String directoryPath = "/novelScrapedData/";

                String originalFolderPath = directoryPath + "original/";
                String translatedFolderPath = directoryPath + "translated/";

                // Ensure the folders exist, if not create them
                new File(originalFolderPath).mkdirs();
                new File(translatedFolderPath).mkdirs();

                FileWriter originalFileWriter = null;

                // Check language and create corresponding original file in the original folder
                if (!originalList.isEmpty()) {
                    String firstLine = originalList.get(0);
                    if (isChinese(firstLine)) {
                        originalFileWriter = new FileWriter(originalFolderPath + "cn_chapter_" + chapterNumber + ".txt");
                    } else if (isKorean(firstLine)) {
                        originalFileWriter = new FileWriter(originalFolderPath + "kr_chapter_" + chapterNumber + ".txt");
                    } else if (isJapanese(firstLine)) {
                        originalFileWriter = new FileWriter(originalFolderPath + "jp_chapter_" + chapterNumber + ".txt");
                    } else { // default to English if no other language is matched
                        originalFileWriter = new FileWriter(originalFolderPath + "en_chapter_" + chapterNumber + ".txt");
                    }
                }

                // Write originalList to the corresponding file
                if (originalFileWriter != null) {
                    for (String line : originalList) {
                        String trimmedLine = line.trim();
                        if (trimmedLine.isEmpty()) continue; // Skip empty lines
                        originalFileWriter.write(trimmedLine + "\n");
                    }
                    originalFileWriter.close(); // Don't forget to close the file writer.
                }

                // Create and Write translatedList to a file in the translated folder
                FileWriter translatedFileWriter = new FileWriter(translatedFolderPath + "translated_chapter_" + chapterNumber + ".txt");
                for (String line : translatedList) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty()) continue; // Skip empty lines
                    translatedFileWriter.write(trimmedLine + "\n");
                }
                translatedFileWriter.close();
            } else {
                System.out.println("Error - Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to count the number of tokens in a string
    private int countTokens(String text) {
        // Replace this with the appropriate tokenization logic for your use case
        //TODO
        return text.split("\\s+").length;
    }

    private void setRequestHeaders(HttpURLConnection connection, Properties properties) {
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            connection.setRequestProperty(propertyName, propertyValue);
        }
    }


    private boolean isChinese(String s) {
        for (char c : s.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
                return true;
            }
        }
        return false;
    }

    private boolean isKorean(String s) {
        for (char c : s.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                return true;
            }
        }
        return false;
    }

    // Method to check if a string contains Japanese characters
    private boolean isJapanese(String s) {
        for (char c : s.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }
}
