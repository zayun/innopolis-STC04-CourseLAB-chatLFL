package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.user.UserList;
import org.apache.log4j.Logger;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class ThreadForSerialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static final Object lock = new Object();
    private static volatile boolean error = false;
    private static Logger logger = Logger.getLogger(ThreadForSerialize.class);

    public ThreadForSerialize(LFLChatLoadable obj, String filePath) {

        this.obj = obj;
        this.filePath = filePath;
    }

    /**Поток выполнения
     * Сначала загружает данные из БД в объекты программы
     * затем сериализует их в файл filePath
     */
    @Override
    public void run() {
        try {
            checkLinkedTables(obj.getClass());
            obj.loadFromDB();
            FileManager.saveToFile(obj, filePath);

            synchronized (lock) {
                lock.notifyAll();
            }

        } catch (InterruptedException e) {
            loggingError(e);
        } catch (SQLException e) {
            System.out.println("Ошибка БД! выполнение потока " + obj.getClass().getSimpleName() + " остановлено");
            loggingError(e);
        } finally {
            DatabaseManager.closeConnection();
        }

        logger.trace("Thread successfully end/" + obj.getClass().getSimpleName());
    }

    /**
     * Проверка очередности загрузки таблиц в порограмму
     * если связанные таблицы не загружены к моменту
     * прохода, то поток уходит в ожидание
     * до загрузки связанных таблиц или ошибки
     * @param objClass - класс проверяемогообъекта
     * @throws InterruptedException
     */
    private void checkLinkedTables(Class objClass) throws InterruptedException {

        if (objClass.equals(UserList.class)) {
            synchronized (lock) {
                while (!Main.persones.isDownloaded() && !error) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        } else if (objClass.equals(LangOwnerList.class)) {
            synchronized (lock) {
                while ((!Main.languages.isDownloaded() ||
                        !Main.persones.isDownloaded()) && !error) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        } else if (objClass.equals(MessageList.class)) {
            synchronized (lock) {
                while (!Main.users.isDownloaded() && !error) {
                    logger.trace(obj.getClass().getSimpleName() + " is waiting");
                    lock.wait();
                }
            }
        }
    }

    /**
     * логирует ошибку
     * уведомляет пользователя в консоль об ошибке
     * устанавливает флаг
     * @see ThreadForDeserialize#error в true для остановки потоков
     * уведомляет ожидающие потоки об ошибке
     * @param e - логируемый Exception
     */
    private void loggingError(Exception e) {
        error = true;
        logger.error(e);
        System.out.println(e.getMessage());
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
