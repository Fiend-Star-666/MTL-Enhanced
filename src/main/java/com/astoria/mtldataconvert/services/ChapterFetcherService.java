package com.astoria.mtldataconvert.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterFetcherService {

    @Autowired
    ChapterFetcher chapterFetcher;

    public void fetchChapters(int numberOfChaptersFrom, int numberOfChaptersTo) {

        //Dotenv.configure().filename("novel.env").load();

        for (int currentChapter = numberOfChaptersFrom; currentChapter <= numberOfChaptersTo; currentChapter++) {

            chapterFetcher.fetchChapter(currentChapter);

        }
    }

    /*
    public static final String certificateAlias = Dotenv.configure().filename("novel.env").load().get("CERTIFICATE_ALIAS");
    public static final String trustStorePassword = Dotenv.configure().filename("novel.env").load().get("TRUST_STORE_PASSWORD");
    public static final String trustStorePath = Dotenv.configure().filename("novel.env").load().get("TRUST_STORE_PATH");

    private static void loadCert() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, KeyManagementException {

        assert trustStorePassword != null;
        char[] customTrustStorePassword = trustStorePassword.toCharArray();
        String customCertificateAlias = certificateAlias;


        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null); // Load empty TrustStore

        KeyStore customTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        assert trustStorePath != null;
        customTrustStore.load(new FileInputStream(trustStorePath), customTrustStorePassword);
        trustStore.setCertificateEntry(customCertificateAlias, customTrustStore.getCertificate(customCertificateAlias));

        // Create TrustManagerFactory with the custom TrustStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Create SSLContext and initialize it with the TrustManagerFactory
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        // Use the custom SSLContext for HttpsURLConnection
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Load the Base64 encoded ASCII certificate from the .crt file
        try {
            try (FileInputStream fis = new FileInputStream(ChapterFetcherService.trustStorePassword);
                 BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {

                StringBuilder certBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("-----BEGIN CERTIFICATE-----") && !line.startsWith("-----END CERTIFICATE-----"))
                        certBuilder.append(line);
                }
                String base64Cert = certBuilder.toString();
                byte[] decodedCert = Base64.getDecoder().decode(base64Cert);
                ByteArrayInputStream bis = new ByteArrayInputStream(decodedCert);

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate cert = cf.generateCertificate(bis);

                trustStore.setCertificateEntry(ChapterFetcherService.certificateAlias, cert);
                //return trustStore;
            }
        } catch (IOException e) {
            System.err.println("Error while reading .crt file: " + Arrays.toString(e.getStackTrace()));
        }
       // return trustStore;
    }
    */

}