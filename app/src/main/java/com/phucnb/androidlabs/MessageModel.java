package com.phucnb.androidlabs;

public class MessageModel {
    public String message;
    public boolean isSend;
    public long messageID;

    public MessageModel(String message, boolean isSend, long messageID ) {
        this.message = message;
        this.isSend = isSend;
        this.messageID = messageID;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }
}
