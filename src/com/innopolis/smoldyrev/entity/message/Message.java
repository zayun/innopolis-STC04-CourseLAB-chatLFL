package com.innopolis.smoldyrev.entity.message;

import com.innopolis.smoldyrev.entity.user.User;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;


/**
 * Created by smoldyrev on 17.02.17.
 */
@XmlType(propOrder = {"date", "fromUser", "toUser", "bodyText", "viewed"})
@XmlRootElement
public class Message {

    private Integer id;
    private Date date;
    private User fromUser;
    private User toUser;
    private String bodyText;
    private boolean viewed;

    public Message() {
    }

    public Message(Integer id, Date date, User fromUser, User toUser, String bodyText, boolean viewed) {
        this.id = id;
        this.date = date;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.bodyText = bodyText;
        this.viewed = viewed;
    }

    @XmlAttribute(name = "identity")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    @XmlElement
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @XmlElement
    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    @XmlElement
    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
