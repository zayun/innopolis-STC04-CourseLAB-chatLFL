package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;

/**
 * Created by smoldyrev on 18.02.17.
 */
public class ThreadControl implements Runnable {
    @Override
    public void run() {
        boolean allTablesReady = false;
        while (!allTablesReady) {
            allTablesReady = (PersonList.isDownloaded()&&
                    LanguageList.isDownloaded()&&
                    UserList.isDownloaded()&&
                    LangOwnerList.isDownloaded()&&
                    MessageList.isDownloaded());
            synchronized (ThreadForSerialize.getLock()) {
                ThreadForSerialize.getLock().notifyAll();
            }
        }
    }
}
