package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

/**
 * Created by smoldyrev on 18.02.17.
 */
public class ThreadForDeserialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static boolean pack = false;
    private static final Object lock = new Object();

    public ThreadForDeserialize(LFLChatLoadable obj, String filePath) {
        this.obj = obj;
        this.filePath = filePath;
    }

    public static boolean isPack() {
        return pack;
    }

    public static void setPack(boolean pack) {
        ThreadForDeserialize.pack = pack;
    }

    @Override
    public void run() {
        if (pack) checkLinkedTables(obj.getClass());
        try {
            obj = (LFLChatLoadable) FileManager.getObject(obj.getClass(),filePath);
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
                while (!PersonList.isUploaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while (!LanguageList.isUploaded() ||
                        !PersonList.isUploaded()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!UserList.isUploaded()) {
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
