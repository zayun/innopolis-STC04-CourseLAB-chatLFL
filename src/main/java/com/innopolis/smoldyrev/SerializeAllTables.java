package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.threads.ThreadForSerialize;

/**
 * <p>Запуск потоков для сериализации в одном месте </p>
 * просто чтобы не засорять мэйн
 */
public class SerializeAllTables {


    public static void go() {

        DatabaseManager.initDatabase();

        PersonList personList = Main.persones;
        LanguageList languageList = Main.languages;
        UserList userList = Main.users;
        LangOwnerList langOwnerList = Main.langOwners;
        MessageList messageList = Main.messageList;

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
            System.out.println(e.getMessage());
            System.out.println("Ошибка выполнения потока");
        }finally {
            DatabaseManager.closeConnection();
        }
    }
}
