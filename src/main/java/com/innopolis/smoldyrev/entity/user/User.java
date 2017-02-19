package com.innopolis.smoldyrev.entity.user;

import com.innopolis.smoldyrev.entity.person.Person;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by smoldyrev on 16.02.17.
 */
@XmlType(propOrder={"userType","login","passwd","person","blocked"})
@XmlRootElement
public class User {

    private Integer userID;
    private String userType;
    private String login;
    private String passwd;
    private Person person;
    private boolean blocked;

    public User() {
    }

    public User(Integer userID, String userType,String login, String passwd, Person person, boolean blocked) {
        this.userID = userID;
        this.userType = userType;
        this.login = login;
        this.passwd = passwd;
        this.person = person;
        this.blocked = blocked;
    }


    @XmlAttribute(name = "id")
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }


    @XmlElement
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @XmlElement
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @XmlElement
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @XmlElement
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @XmlElement
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
