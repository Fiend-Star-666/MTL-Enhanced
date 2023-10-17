package com.astoria.mtldataconvert.controller;

import com.astoria.mtldataconvert.translator.BrowserControl;
import com.astoria.mtldataconvert.translator.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translator")
public class TranslatorController {

    @Autowired
    private Translator translator;

    @Autowired
    private BrowserControl browserControl;

    @GetMapping("/run")
    public String runTranslator() {
        translator.run();
        return "Translation process completed";
    }

    @PostMapping("/send-prompt")
    public String sendPromptToChatGPT(@RequestBody String prompt) {
        translator.getChatGPTAutomation().sendPromptToChatGPT(prompt);
        return "Prompt sent";
    }

    @GetMapping("/last-response")
    public String getLastResponse() {
        return translator.getChatGPTAutomation().returnLastResponse();
    }

    @PostMapping("/save-conversation")
    public String saveConversation(@RequestParam String fileName) {
        translator.getChatGPTAutomation().saveConversation(fileName);
        return "Conversation saved";
    }

    @PostMapping("/quit")
    public String quit() {
        translator.getChatGPTAutomation().quit();
        return "Browser closed";
    }

}
