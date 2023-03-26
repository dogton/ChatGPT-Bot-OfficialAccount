package com.dogton.wehcat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;

import static com.theokanning.openai.service.OpenAiService.*;

@Service
public class ChatGPTService {

    private static final Logger log = LoggerFactory.getLogger(ChatGPTService.class);

    @Value("${openApiKey:}")
    private String openApiKey;

    @Value("${model:}")
    private String model;

    @Value("${errorTips}")
    private String errorTips;

    @Value("${proxyTarget:}")
    private String proxyTarget;

    @Value("${timeout:1000}")
    private long timeout;

    private OpenAiService openAiService;

    private Multimap<String, ChatMessage> messageMultimap = ArrayListMultimap.create();

    public String reply(String user, String prompt) {
        try {
            messageMultimap.put(user, new ChatMessage("user", prompt));
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(new ArrayList<>(messageMultimap.get(user)))
                    .build();
            return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Throwable e) {
            log.error("Get ChatGPT Message Error! user:{} prompt: {}", user, prompt, e);
            return errorTips;
        }
    }

    @Autowired
    public void setOpenAiService() {
        if (StringUtils.hasText(proxyTarget)) {
            String[] hostPort = proxyTarget.split(":");
            if (hostPort.length != 2) {
                throw new RuntimeException("代理地址配置异常, 请调整配置!");
            }
            ObjectMapper mapper = defaultObjectMapper();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1])));
            OkHttpClient client = defaultClient(openApiKey, Duration.ofMillis(timeout))
                    .newBuilder()
                    .proxy(proxy)
                    .build();
            Retrofit retrofit = defaultRetrofit(client, mapper);
            OpenAiApi api = retrofit.create(OpenAiApi.class);
            this.openAiService = new OpenAiService(api);
        } else {
            this.openAiService = new OpenAiService(openApiKey);
        }
    }
}
