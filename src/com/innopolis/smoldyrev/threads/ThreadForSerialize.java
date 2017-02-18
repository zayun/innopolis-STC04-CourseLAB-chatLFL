package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;

import java.sql.SQLException;

/**
 * Created by smoldyrev on 17.02.17.
 */
public class ThreadForSerialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static final Object lock = new Object();


    public ThreadForSerialize(LFLChatLoadable obj, String filePath) {

        this.obj = obj;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        checkLinkedTables(obj.getClass());

        try {
            obj.loadFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FileManager.saveToFile(obj, filePath);

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void checkLinkedTables(Class objClass) {

        if (objClass.equals(UserList.class)) {
            synchronized (lock) {
                while (!PersonList.isDownloaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while (!LanguageList.isDownloaded() ||
                        !PersonList.isDownloaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!UserList.isDownloaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
