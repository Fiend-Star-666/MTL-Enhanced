package com.astoria.mtldataconvert.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Properties;

@Service
public class ChapterFetcher {
    private final String urlBase;
    private final String urlNovel;
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String certificateAlias;

    public ChapterFetcher(@Value("${URL_BASE}") String urlBase,
                          @Value("${URL_NOVEL}") String urlNovel,
                          @Value("${TRUST_STORE_PATH}") String trustStorePath,
                          @Value("${TRUST_STORE_PASSWORD}") String trustStorePassword,
                          @Value("${CERTIFICATE_ALIAS}") String certificateAlias) {
        this.urlBase = urlBase;
        this.urlNovel = urlNovel;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.certificateAlias = certificateAlias;
    }

    public void fetchChapter(int chapterNumber) {

        String url = this.urlBase + urlNovel;
        System.out.println("Fetching chapter " + chapterNumber + " from " + url);
        try {
            URL requestUrl = new URL(url);

            // Load the default TrustStore
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null); // Load empty TrustStore

            // Load the imported certificate from the custom TrustStore
            String customTrustStorePath = trustStorePath;
            char[] customTrustStorePassword = trustStorePassword.toCharArray();
            String customCertificateAlias = certificateAlias;

            KeyStore customTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            customTrustStore.load(new FileInputStream(customTrustStorePath), customTrustStorePassword);
            trustStore.setCertificateEntry(customCertificateAlias, customTrustStore.getCertificate(customCertificateAlias));

            // Create TrustManagerFactory with the custom TrustStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Create SSLContext and initialize it with the TrustManagerFactory
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Use the custom SSLContext for HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            /*
            // sleep for a random amount of time between 1 and 20 seconds for AntiDetectionServices

                Random random = new Random();

                // Generate a random decimal number between 1 and 20.
                double randomSeconds = 0 + (20 - 1) * random.nextDouble();

                System.out.println("Sleeping for " + randomSeconds + " seconds...");

                try {
                    // Convert seconds to milliseconds and sleep.
                    Thread.sleep((long) (randomSeconds * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Woke up!");

            */

            // Open connection and make the GET request
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            Properties properties = PropertyLoaderHeaders.loadProperties();

            setRequestHeaders(connection, properties);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the HTML response
                Document document = Jsoup.parse(response.toString());

                // Find the desired <div> element
                Element divElement = document.selectFirst("div.chapter-body[v-pre]");

                // Extract the content of the <div> element
                assert divElement != null;
                String divContent = divElement.text();

                // Save the content to a file
                String directoryPath = "/novelScrapedData/";

                File directory = new File(directoryPath);
                directory.mkdirs();
                Date date = new Date();
                String fileName = directoryPath + "chapter_" + date.getTime()+"number_" + chapterNumber + ".txt";

                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(divContent);
                fileWriter.close();

                // Calculate the number of tokens
                int numTokens = countTokens(divContent);
                System.out.println("Number of tokens for chapter " + chapterNumber + ": " + numTokens);
            } else {
                System.out.println("Error - Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException | KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    // Function to count the number of tokens in a string
    private int countTokens(String text) {
        // Replace this with the appropriate tokenization logic for your use case
        // For example, if you are using whitespace-based tokenization, you can use:
        // String[] tokens = text.split("\\s+");
        // return tokens.length;
        return text.split("\\s+").length;
    }

    private void setRequestHeaders(HttpURLConnection connection, Properties properties) {
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            connection.setRequestProperty(propertyName, propertyValue);
        }
    }
}
