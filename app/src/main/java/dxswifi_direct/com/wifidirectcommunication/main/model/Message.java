package dxswifi_direct.com.wifidirectcommunication.main.model;

import java.util.Date;

/**
 * Created by Deepak Sharma on 10/6/16.
 */

public class Message {
    private String message;
    private String sender;
    private Date messageDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
