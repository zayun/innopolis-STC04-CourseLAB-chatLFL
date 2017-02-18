package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;

import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;

import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;

import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;
import com.innopolis.smoldyrev.threads.ThreadForSerialize;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args){

        PersonList pl = new PersonList();
        LFLChatLoadable obj = pl;
        File file = new File("temp/persones.xml");

        DatabaseManager.initDatabase();

        try {
            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
            try {
                obj.uploadToDB();
            } catch (NoDataException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
        }

        UserList ul = new UserList();
        obj = ul;
        file = new File("temp/users.xml");

        try {
            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
            try {
                obj.uploadToDB();
            } catch (NoDataException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (JAXBException e) {
            System.out.println(e.getMessage());
        }
        DatabaseManager.closeConnection();

    }
}
