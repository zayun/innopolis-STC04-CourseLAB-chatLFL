package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.language.LangOwner;

import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.Language;

import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.Person;

import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.threads.ThreadControl;
import com.innopolis.smoldyrev.threads.ThreadForSerialize;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws SQLException {

        /*Сначала грузим основные справочники Person и Language*/
        /*Потом User и LangOwner*/
        /*Messages только после User*/
        DatabaseManager.initDatabase();

        PersonList personList = new PersonList();
        LanguageList languageList = new LanguageList();

        UserList userList = new UserList();
        LangOwnerList langOwnerList = new LangOwnerList();

        MessageList messageList = new MessageList();

        Thread tc = new Thread(new ThreadControl());
        tc.start();
        Thread t1 = new Thread(new ThreadForSerialize(personList, "temp/persones.xml"));
        Thread t2 = new Thread(new ThreadForSerialize(languageList, "temp/languages.xml"));
        Thread t3 = new Thread(new ThreadForSerialize(userList, "temp/users.xml"));
        Thread t4 = new Thread(new ThreadForSerialize(langOwnerList, "temp/langOwners.xml"));
        Thread t5 = new Thread(new ThreadForSerialize(messageList, "temp/messages.xml"));
        t3.start();
        t1.start();
        t2.start();
        t4.start();
        t5.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DatabaseManager.closeStatement();
        DatabaseManager.closeConnection();

//
//        PersonList persons = new PersonList();
//        persons.loadFromDB();
//        LanguageList languages = new LanguageList();
//        languages.loadFromDB();
//
//        for (Language lang:
//             languages.getLanguages()) {
//            System.out.println(lang.getShortName()+"/");
//        }
        //FileManager.saveToFile(languages, "temp/languages.xml");

//        LangOwnerGroup langOwners = new LangOwnerGroup();
//        langOwners.loadFromDB();
//        FileManager.saveToFile(langOwners, "temp/langOwner.xml");

//
//        MessageList ml = new MessageList();
//        ml.loadFromDB();
//        FileManager.saveToFile(ml, "temp/messages.xml");
//
//        DatabaseManager.closeStatement();
//        DatabaseManager.closeConnection();

//        DatabaseManager.initDatabase();
//
//        PersonGroup persons = new PersonGroup();
//        persons.loadFromDB();
//        UserGroup users = new UserGroup();
//        users.loadFromDB();
//        LanguageGroup languages = new LanguageGroup();
//        languages.loadFromDB();

//        FileManager.saveToFile(users, "temp/users.xml");
//        FileManager.saveToFile(languages, "temp/languages.xml");

//        Language lang = new Language();
//        lang.setShortName("RUS");
//        lang.setFullName("Russian");
//        lang.setDialekt("General");
//
//        Language lang2 = new Language();
//        lang2.setShortName("ENG");
//        lang2.setFullName("English");
//        lang2.setDialekt("Britain");
//
//        LanguageGroup lg = new LanguageGroup();
//        lg.add(lang);
//        lg.add(lang2);
//        FileManager.saveToFile(lg, "temp/languages.xml");
//
//        Person p1 = new Person();
//        p1.setId(1);
//        p1.setFirstName("efff");
//        p1.setLastName("sdasdas");
//        p1.setEmail("asdasd@asdas.ru");
//        p1.setPhoneNumber("911");
//        p1.setBirthDay(new Date(2016, 9, 12));
//
//        Person p2 = new Person();
//        p2.setId(2);
//        p2.setFirstName("ddddd");
//        p2.setLastName("seeeeee");
//        p2.setEmail("asdasd@asdas.ru");
//        p2.setPhoneNumber("343434");
//        p2.setBirthDay(new Date(2016, 9, 12));
//        User user = new User();
//        user.setUserID(1);
//        user.setLogin("test");
//        user.setPasswd("123456");
//        user.setPerson(p1);
//
//        User user2 = new User();
//        user2.setUserID(2);
//        user2.setLogin("test2");
//        user2.setPasswd("123456");
//        user2.setPerson(p2);
//
//        UserGroup ug = new UserGroup();
//        ug.add(user);
//        ug.add(user2);
//        FileManager.saveToFile(ug, "temp/users.xml");

//        Person p1 = new Person();
//        p1.setId(1);
//        p1.setFirstName("efff");
//        p1.setLastName("sdasdas");
//        p1.setEmail("asdasd@asdas.ru");
//        p1.setPhoneNumber("911");
//        p1.setBirthDay(new Date(2016, 9, 12));
//
//        Person p2 = new Person();
//        p2.setId(2);
//        p2.setFirstName("ddddd");
//        p2.setLastName("seeeeee");
//        p2.setEmail("asdasd@asdas.ru");
//        p2.setPhoneNumber("343434");
//        p2.setBirthDay(new Date(2016, 9, 12));
//        PersonGroup pg = new PersonGroup();
//        pg.add(p1);
//        pg.add(p2);
//        FileManager.saveToFile(pg, "temp/persons.xml");


//        File file = new File("temp/file.xml");
//        try {
//            UserGroup ug2 = (UserGroup) FileManager.getObject(file, UserGroup.class);
//
//            for (User user:
//                    ug2.getUsers()) {
//                System.out.println(user.getUserID()+"/"+user.getLogin());
//                    System.out.println(user.getPerson().getFirstName()+" " + user.getPerson().getLastName());
//            }
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } finally {
//            DatabaseManager.closeConnection();
//        }


//
//        User user = new User();
//        user.setUserID(1);
//        user.setLogin("test");
//        user.setPasswd("123456");
//        user.setPerson(p1);
//
//        User user2 = new User();
//        user2.setUserID(2);
//        user2.setLogin("test2");
//        user2.setPasswd("123456");
//        user2.setPerson(p2);
//
//        UserGroup ug = new UserGroup();
//        ug.add(user);
//        ug.add(user2);
//        ArrayList<Person> aalp = new ArrayList<>();
//        aalp.add(p1);
//        aalp.add(p2);
//        PersonGroup pg = new PersonGroup();
//        pg.setPersons(aalp);

//        PersonGroup pg = new PersonGroup();
//        try {
//            pg.loadPersonsFromDB("","");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


//        File file = new File("temp/file.xml");
//        try {
//            UserGroup ug2 = (UserGroup) FileManager.getObject(file,UserGroup.class);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }


    }
}
