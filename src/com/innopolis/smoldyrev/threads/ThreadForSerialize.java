package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.user.UserList;

import java.sql.SQLException;

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

    private void checkLinkedTables(Class objClass) {

        if (objClass.equals(UserList.class)) {
            synchronized (lock) {
                while (!Main.persones.isDownloaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while (!Main.languages.isDownloaded()||
                        !Main.persones.isDownloaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!Main.users.isDownloaded()) {
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
