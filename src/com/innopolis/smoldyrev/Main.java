package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.entity.language.LangOwner;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;


public class Main {
    public static PersonList persones;
    public static UserList users;
    public static LanguageList languages;
    public static LangOwnerList langOwners;
    public static MessageList messageList;
;
    public static void main(String[] args){

//        SerializeAllTables.go();
//        DeserializeAllTables.go();
        DatabaseManager.initDatabase();
        persones = new PersonList();
        users = new UserList();
        languages = new LanguageList();
        langOwners = new LangOwnerList();
        messageList = new MessageList();

        try {
//            persones.loadFromDB();
//            users.loadFromDB();
//            languages.loadFromDB();
//            langOwners.loadFromDB();
//            messageList.loadFromDB();
//            FileManager.saveToFile(persones,"temp/persones.xml");
//            FileManager.saveToFile(users,"temp/users.xml");
//            FileManager.saveToFile(languages,"temp/languages.xml");
//            FileManager.saveToFile(langOwners,"temp/langOwners.xml");
//            FileManager.saveToFile(messageList,"temp/messages.xml");

            persones = (PersonList) FileManager.getObject(persones.getClass(),"temp/persones.xml");
            persones.uploadToDB();
            users = (UserList) FileManager.getObject(users.getClass(),"temp/users.xml");
            users.uploadToDB();
            languages = (LanguageList) FileManager.getObject(languages.getClass(),"temp/languages.xml");
            languages.uploadToDB();
            langOwners = (LangOwnerList) FileManager.getObject(langOwners.getClass(),"temp/langOwners.xml");
            langOwners.uploadToDB();
            messageList = (MessageList) FileManager.getObject(messageList.getClass(),"temp/messages.xml");
            messageList.uploadToDB();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeConnection();

    }
}
