package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.exception.NoDataException;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by smoldyrev on 19.02.17.
 * Абстрактный класс
 * хранение и работа со списком сущностей
 */

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractEntityList<T> implements LFLChatLoadable {

    protected static Logger logger = Logger.getLogger(AbstractEntityList.class);

    private List<T> listEntities = new ArrayList<>();

    private Set<T> pulEntities = new HashSet<>();

    private volatile boolean downloaded = false;

    private volatile boolean uploaded = false;

    /**
     * Поиск сущности из списка
     *
     * @param id - идентификатор сущности
     * @see AbstractEntityList#listEntities
     * по
     */
    public abstract T getEntityOnID(String id);

    /**
     * Поиск сущности из списка
     *
     * @param id - идентификатор сущности
     * @see AbstractEntityList#listEntities
     * по
     */
    public abstract T getEntityOnID(int id);

    /**
     * Добавление сущности в список
     *
     * @see AbstractEntityList#listEntities
     */
    public void add(Object entity) {
        listEntities.add((T) entity);
    }

    /**
     * Установить новую ссылку на список сущностей
     *
     * @param entities - новая ссылка на список
     * @see AbstractEntityList#listEntities
     */
    public void setEntityList(List entities) {
        listEntities = entities;
    }

    /**
     * Получить ссылку на список сущностей
     *
     * @return entities - ссылка на список
     * @see AbstractEntityList#listEntities
     */
    public List getEntityList() {
        return listEntities;
    }

    /**
     * Флаг указывающий на то, что данные загружены в
     *
     * @return downloaded
     * @see AbstractEntityList#listEntities
     * true-загружены, false-не загружены
     */
    public boolean isDownloaded() {
        return downloaded;
    }

    /**
     * Флаг указывающийна то, что данные загружены на сервер
     * true-загружены, false-не загружены
     *
     * @return uploaded
     */
    public boolean isUploaded() {
        return uploaded;
    }

    /**
     * Установить флаг загрузки с сервера
     * true-загружены, false-не загружены
     *
     * @return uploaded
     */
    public void setDownloaded(boolean down) {
        downloaded = down;
    }

    /**
     * Установить флаг загрузки на сервер
     * true-загружены, false-не загружены
     *
     * @return uploaded
     */
    public void setUploaded(boolean upl) {
        uploaded = upl;
    }

    /**
     * Загружает данные таблицы сервера в
     *
     * @throws SQLException - проблемы с выполнением запросов к серверу
     * @see AbstractEntityList#listEntities
     */
    public synchronized void loadFromDB() throws SQLException {
        if (isDownloaded()) listEntities = null;
//        DatabaseManager dbm = new DatabaseManager();

        Statement stmt = DatabaseManager.getStatement();
        ResultSet rs = stmt.executeQuery(getSqlText("select"));
        while (rs.next()) {
            T entity = getEntity(rs);
            listEntities.add(entity);
        }
        rs.close();
        stmt.close();
        setDownloaded(true);

        logger.trace("data was serialized from table: " + getTableName());
    }

    /**
     * Загружает данные из
     *
     * @throws SQLException    - проблемы с выполнением запросов к серверу
     * @throws NoDataException - нет данных к загрузке в listEntities
     * @see AbstractEntityList#listEntities в таблицу на сервере
     */
    public synchronized void uploadToDB() throws SQLException, NoDataException {
        if (listEntities != null && listEntities.size() != 0) {
//            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = DatabaseManager.getPrepearedStatement(getSqlText("insert"));
            try {

                for (T entity :
                        listEntities) {
                    executePrepearedInsert(pstmt, entity);
                }
                pstmt.executeBatch();
                setUploaded(true);
                logger.trace("data was uploaded in table: " + getTableName());
            } finally {
                pstmt.close();
            }
        } else {
            logger.error("no data to upload in: " + getTableName());
            throw new NoDataException("Нет данных для загрузки");
        }
    }

    /**
     * Возвращает текст запроса к таблице
     *
     * @param type - типа запроса (select/insert)
     * @return sqlTest - sql запрос к таблице
     */
    protected abstract String getSqlText(String type);

    /**
     * Возвращает сущность полученную из таблицы
     *
     * @param rs - ResultSet с данными сущности из таблицы
     * @return entity - сущность
     */
    protected abstract T getEntity(ResultSet rs) throws SQLException;

    /**
     * Выполнение prepearedStatement на insert втаблицу
     *
     * @param pstmt  - подготовленный statement
     * @param entity - вставляемая сущность
     */
    protected abstract void executePrepearedInsert(PreparedStatement pstmt, T entity) throws SQLException;

    /**
     * Возращает имя таблицы хранения сущностей
     */
    protected abstract String getTableName();
}
