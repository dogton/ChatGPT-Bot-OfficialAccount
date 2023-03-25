package com.dogton.wehcat.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChatGPTService {

    private static final Logger log = LoggerFactory.getLogger(ChatGPTService.class);

//    @Value("${openApiKey:}")
    private String openApiKey = System.getenv("API_KEY");

    @Value("${model}")
    private String model;

    @Value("${errorTips}")
    private String errorTips;

    private Multimap<String, ChatMessage> messageMultimap = ArrayListMultimap.create();

    public String reply(String user, String prompt) {
        try {
            messageMultimap.put(user, new ChatMessage("user", prompt));
            OpenAiService service = new OpenAiService(openApiKey);
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(new ArrayList<>(messageMultimap.get(user)))
                    .build();
            return service.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Throwable e) {
            log.error("Get ChatGPT Message Error! user:{} prompt: {}", user, prompt, e);
            return errorTips;
        }
    }
}
