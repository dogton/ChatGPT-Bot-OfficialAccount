package com.dogton.wehcat.util;


import com.dogton.wehcat.entity.wechat.TextMessage;

import java.util.Map;

public final class MessageFormatUtil {

    public static TextMessage parseMessage(Map<String, String> map) {
        TextMessage message = new TextMessage();
        message.setToUserName(map.get("ToUserName"));
        message.setFromUserName(map.get("FromUserName"));
        message.setCreateTime(Long.parseLong(map.getOrDefault("CreateTime", "0")));
        message.setMsgType(map.get("MsgType"));
        message.setContent(map.get("Content"));
        message.setMsgId(Long.parseLong(map.getOrDefault("MsgId", "0")));
        message.setMsgDataId(Long.parseLong(map.getOrDefault("MsgDataId", "0")));
        message.setIdx(Long.parseLong(map.getOrDefault("Idx", "0")));
        return message;
    }

    public static String replay(TextMessage message) {
        return String.format("<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%d</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[%s]]></Content>" +
                        "</xml>",
                message.getFromUserName(), message.getToUserName(), System.currentTimeMillis(), message.getReplyMsg());
    }
}
