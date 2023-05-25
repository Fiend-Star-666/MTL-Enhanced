package com.astoria.mtldataconvert.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class ChapterFetcherService {
    public void fetchChapters(int numberOfChaptersFrom, int numberOfChaptersTo) {
        Dotenv dotenv = Dotenv.configure().filename("novel.env").load();

        String urlBase = dotenv.get("URL_BASE");
        String urlNovel = dotenv.get("URL_NOVEL");
        String trustStorePath = dotenv.get("TRUST_STORE_PATH");
        String trustStorePassword = dotenv.get("TRUST_STORE_PASSWORD");
        String certificateAlias = dotenv.get("CERTIFICATE_ALIAS");

        for (int currentChapter = numberOfChaptersFrom; currentChapter <= numberOfChaptersTo; currentChapter++) {

            String chapter = urlNovel + currentChapter;

            ChapterFetcher chapterFetcher = new ChapterFetcher(urlBase, chapter, trustStorePath, trustStorePassword, certificateAlias);

            chapterFetcher.fetchChapter(currentChapter);

        }
    }
}
