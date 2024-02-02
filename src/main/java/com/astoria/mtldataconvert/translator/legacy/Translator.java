package com.astoria.mtldataconvert.translator.legacy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
public class Translator {

    private ChatGPTAutomation chatGPTAutomation;

    @Autowired
    public void ChatGPTClient(ChatGPTAutomation chatGPTAutomation) {
        this.chatGPTAutomation = chatGPTAutomation;
    }

    public Translator(ChatGPTAutomation chatGPTAutomation) {
        this.chatGPTAutomation = chatGPTAutomation;
    }

    public void run() {

        chatGPTAutomation.initializeWebDriver();

        // Define a prompt and send it to ChatGPT
//        String prompt = "What are the benefits of exercise?";
//        chatGPTAutomation.sendPromptToChatGPT(prompt);
//
//        // Retrieve the last response from ChatGPT
//        String response = chatGPTAutomation.returnLastResponse();
//        System.out.println(response);
//
//        // Save the conversation to a text file
//        String fileName = "conversation.txt";
//        chatGPTAutomation.saveConversation(fileName);
//
//        // Close the browser and terminate the WebDriver session
//        chatGPTAutomation.quit();
    }
}

