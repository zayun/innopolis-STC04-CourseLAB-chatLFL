package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

/**
 * Created by smoldyrev on 09.02.17.
 * десериализует файл находящийся в filePath
 * в объект obj и загружает полученные данные в таблицу на сервере
 */
public class ThreadForDeserialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;
    private static final Object lock = new Object();
    private static volatile boolean error = false;
    private static Logger logger = Logger.getLogger(ThreadForSerialize.class);

    /**
     * Конструктор - создание нового потока
     * @see ThreadForDeserialize#ThreadForDeserialize(LFLChatLoadable,String)
     * @param obj - объект в который будет загружен файл
     * @param filePath - путь к загружаемому файлу
     */
    public ThreadForDeserialize(LFLChatLoadable obj, String filePath) {

        this.obj = obj;
        this.filePath = filePath;
    }

    /**Поток выполнения
     * десериализует файл находящийся в filePath
     * в объект obj и загружает полученные данные в таблицу на сервере
     */
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
        } finally {
            DatabaseManager.closeConnection();
        }
        logger.trace("Thread successfully end/" + obj.getClass().getSimpleName());
    }

    /**
     * Проверка очередности загрузки таблиц в базу
     * если связанные таблицы не загружены к моменту
     * прохода, то поток уходит в ожидание
     * до загрузки связанных таблиц или ошибки
     * @param objClass - класс проверяемогообъекта
     * @throws InterruptedException
     */
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
