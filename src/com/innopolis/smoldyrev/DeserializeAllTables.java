package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.threads.ThreadForDeserialize;

/**
 * Created by smoldyrev on 18.02.17.
 */
public class DeserializeAllTables {

    public static void go() {
        DatabaseManager.initDatabase();

        PersonList personList = new PersonList();
        LanguageList languageList = new LanguageList();
        UserList userList = new UserList();
        LangOwnerList langOwnerList = new LangOwnerList();
        MessageList messageList = new MessageList();

        Thread t1 = new Thread(new ThreadForDeserialize(personList, "temp/persones.xml"));
        Thread t2 = new Thread(new ThreadForDeserialize(languageList, "temp/languages.xml"));
        Thread t3 = new Thread(new ThreadForDeserialize(userList, "temp/users.xml"));
        Thread t4 = new Thread(new ThreadForDeserialize(langOwnerList, "temp/langOwners.xml"));
        Thread t5 = new Thread(new ThreadForDeserialize(messageList, "temp/messages.xml"));
        t3.start();
        t1.start();
        t2.start();
        t4.start();
        t5.start();
    }
}
