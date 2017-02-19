package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;


public class ThreadForDeserialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static final Object lock = new Object();
    private static volatile boolean error = false;
    private static Logger logger = Logger.getLogger(ThreadForSerialize.class);

    public ThreadForDeserialize(LFLChatLoadable obj, String filePath) {

        this.obj = obj;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            checkLinkedTables(obj.getClass());
            LFLChatLoadable loadObject = (LFLChatLoadable) FileManager.getObject(obj.getClass(), filePath);
            obj.setEntityList(loadObject.getEntityList());
            obj.uploadToDB();

            synchronized (lock) {
                lock.notifyAll();
            }
        } catch (JAXBException e) {
            loggingError(e);
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            loggingError(e);
            System.out.println("Ошибка ожидания потока");
        } catch (SQLException e) {
            loggingError(e);
            System.out.println("Ошибка БД! выполнение потока " + obj.getClass().getSimpleName() + " остановлено");
        } catch (NoDataException e) {
            loggingError(e);
        }
        logger.trace("Thread successfully end/" + obj.getClass().getSimpleName());
    }

    public void checkLinkedTables(Class objClass) throws InterruptedException {

        if (objClass.equals(UserList.class)) {
            synchronized (lock) {
                while (!Main.persones.isUploaded() && !error) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while ((!Main.languages.isUploaded() &&
                        !Main.persones.isUploaded() && !error)) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!Main.users.isUploaded() && !error) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        }
    }

    private void loggingError(Exception e) {
        error = true;
        logger.error(e);
        System.out.println(e.getMessage());
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
