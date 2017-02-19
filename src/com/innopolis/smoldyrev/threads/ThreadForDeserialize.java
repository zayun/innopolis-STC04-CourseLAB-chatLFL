package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;


public class ThreadForDeserialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static final Object lock = new Object();

    public ThreadForDeserialize(LFLChatLoadable obj, String filePath) {

        this.obj = obj;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        checkLinkedTables(obj.getClass());
        try {
            LFLChatLoadable loadObject = (LFLChatLoadable) FileManager.getObject(obj.getClass(), filePath);
            obj.setEntityList(loadObject.getEntityList());
            obj.uploadToDB();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void checkLinkedTables(Class objClass) {

        if (objClass.equals(UserList.class)) {
            synchronized (lock) {
                while (!Main.persones.isUploaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while (!Main.languages.isUploaded() ||
                        !Main.persones.isUploaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!Main.users.isUploaded()) {
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
