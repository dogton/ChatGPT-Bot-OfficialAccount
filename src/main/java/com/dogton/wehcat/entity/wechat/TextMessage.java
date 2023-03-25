package com.dogton.wehcat.entity.wechat;

public class TextMessage {

    private String toUserName;
    private String fromUserName;
    private long createTime;
    private String msgType;
    private String content;
    private long msgId;
    private long msgDataId;
    private long idx;

    private String replyMsg;

    public String getToUserName() {
        return toUserName;
    }

    public TextMessage setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public TextMessage setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public TextMessage setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMsgType() {
        return msgType;
    }

    public TextMessage setMsgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TextMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public long getMsgId() {
        return msgId;
    }

    public TextMessage setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public long getMsgDataId() {
        return msgDataId;
    }

    public TextMessage setMsgDataId(long msgDataId) {
        this.msgDataId = msgDataId;
        return this;
    }

    public long getIdx() {
        return idx;
    }

    public TextMessage setIdx(long idx) {
        this.idx = idx;
        return this;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public TextMessage setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
        return this;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime=" + createTime +
                ", msgType='" + msgType + '\'' +
                ", content='" + content + '\'' +
                ", msgId=" + msgId +
                ", msgDataId=" + msgDataId +
                ", idx=" + idx +
                ", replyMsg='" + replyMsg + '\'' +
                '}';
    }
}
