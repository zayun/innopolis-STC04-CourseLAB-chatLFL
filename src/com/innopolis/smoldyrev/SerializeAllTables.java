package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.threads.ThreadForSerialize;

/**
 * Created by smoldyrev on 18.02.17.
 */
public class SerializeAllTables {

    public static void go(){
        DatabaseManager.initDatabase();

        PersonList personList = new PersonList();
        LanguageList languageList = new LanguageList();

        UserList userList = new UserList();
        LangOwnerList langOwnerList = new LangOwnerList();

        MessageList messageList = new MessageList();

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
        DatabaseManager.closeConnection();
    }
}
