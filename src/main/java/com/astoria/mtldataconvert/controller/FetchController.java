package com.astoria.mtldataconvert.controller;

import com.astoria.mtldataconvert.services.ChapterFetcherService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class FetchController {

    private final ChapterFetcherService chapterFetcherService;

    @Autowired
    public FetchController(ChapterFetcherService chapterFetcherService) {
        this.chapterFetcherService = chapterFetcherService;
    }

    @GetMapping("/fetchChapters")
    public String fetchChaptersForm(@ModelAttribute ChapterRange chapterRange, Model model) {
        chapterFetcherService.fetchChapters(chapterRange.getFrom(), chapterRange.getTo());
        model.addAttribute("chapterRange", new ChapterRange());
        model.addAttribute("successMessage", "Chapters fetched successfully");
        return "fetchChapters";
    }


    public class ChapterRange {
        @Getter
        @Setter
        private int from;

        @Getter
        @Setter
        private int to;
    }
}
