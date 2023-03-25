package com.dogton.wehcat.controller;

import com.dogton.wehcat.entity.wechat.TextMessage;
import com.dogton.wehcat.service.ChatGPTService;
import com.dogton.wehcat.util.WeChatMessageHelper;
import com.dogton.wehcat.util.MessageFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WxMessageController {

    private final static Logger log = LoggerFactory.getLogger(WxMessageController.class);

    @Value("${wxToken}")
    private String wxToken;

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping
    public String validate(@RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        if (WeChatMessageHelper.validate(wxToken, signature, timestamp, nonce)) {
            return echostr;
        }
        return "error";
    }

    @PostMapping
    public String reply(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        if (WeChatMessageHelper.validate(wxToken, signature, timestamp, nonce)) {
            Map<String, String> xml = WeChatMessageHelper.parseXml(request);
            TextMessage message = MessageFormatUtil.parseMessage(xml);
            String replyMsg = chatGPTService.reply(message.getFromUserName(), message.getContent());
            log.info("Receive TextMessage: {}, Reply: {}", message, replyMsg);
            return MessageFormatUtil.replay(message.setReplyMsg(replyMsg));
        } else {
            return "error";
        }
    }

    @GetMapping("test")
    public String test(String user, String prompt) {
        return chatGPTService.reply(user, prompt);
    }

}
