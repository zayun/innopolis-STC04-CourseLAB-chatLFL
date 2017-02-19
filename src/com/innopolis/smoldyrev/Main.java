package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.language.LangOwner;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;

import java.sql.SQLException;


public class Main {
    public static PersonList persones;
    public static UserList users;
    public static LanguageList languages;
    public static LangOwnerList langOwners;
;
    public static void main(String[] args){

//        SerializeAllTables.go();
//        DeserializeAllTables.go();
        DatabaseManager.initDatabase();
        persones = new PersonList();
        users = new UserList();
        languages = new LanguageList();
        langOwners = new LangOwnerList();

        try {
            persones.loadFromDB();
            users.loadFromDB();
            languages.loadFromDB();
            langOwners.loadFromDB();
            FileManager.saveToFile(persones,"temp/persones.xml");
            FileManager.saveToFile(users,"temp/users.xml");
            FileManager.saveToFile(languages,"temp/languages.xml");
            FileManager.saveToFile(langOwners,"temp/langOwners.xml");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeConnection();

    }
}
