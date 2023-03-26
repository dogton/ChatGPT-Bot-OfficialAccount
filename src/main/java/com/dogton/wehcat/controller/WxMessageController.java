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
@RequestMapping("/wechat/${wxToken}/${openApiKey}")
public class WxMessageController {

    private final static Logger log = LoggerFactory.getLogger(WxMessageController.class);

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping
    public String validate(@PathVariable() String wxToken,
                           @PathVariable() String openApiKey,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        if (WeChatMessageHelper.validate(wxToken, signature, timestamp, nonce)) {
            return echostr;
        }
        return "error";
    }

    @PostMapping
    public String reply(@PathVariable() String wxToken,
                        @PathVariable() String openApiKey,
                        HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        if (WeChatMessageHelper.validate(wxToken, signature, timestamp, nonce)) {
            Map<String, String> xml = WeChatMessageHelper.parseXml(request);
            TextMessage message = MessageFormatUtil.parseMessage(xml);
            String replyMsg = chatGPTService.reply(openApiKey, message.getFromUserName(), message.getContent());
            log.info("Receive TextMessage: {}, Reply: {}", message, replyMsg);
            return MessageFormatUtil.replay(message.setReplyMsg(replyMsg));
        } else {
            return "error";
        }
    }

}
