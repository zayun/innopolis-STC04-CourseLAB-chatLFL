package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.exception.NoDataException;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 19.02.17.
 */

@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractEntityList<T> implements LFLChatLoadable {

    protected static Logger logger = Logger.getLogger(AbstractEntityList.class);

    private List<T> listEntities = new ArrayList<>();

    private volatile boolean downloaded = false;

    private volatile boolean uploaded = false;

    public abstract T getEntityOnID(String id);

    public abstract T getEntityOnID(int id);

    public void add(Object entity) {
        listEntities.add((T) entity);
    }

    public void setEntityList(List entities) {
        listEntities = entities;
    }

    public List getEntityList() {
        return listEntities;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setDownloaded(boolean down) {
        downloaded = down;
    }

    public void setUploaded(boolean upl) {
        uploaded = upl;
    }

    public synchronized void loadFromDB() throws SQLException {
        if (isDownloaded()) listEntities = null;
        DatabaseManager dbm = new DatabaseManager();

        Statement stmt = dbm.getStatement();
        ResultSet rs = stmt.executeQuery(getSqlText("select"));
        while (rs.next()) {
            listEntities.add(getEntity(rs));
        }
        rs.close();
        stmt.close();
        setDownloaded(true);
        logger.trace("data was serialized from table: "+getTableName());
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {
        if (listEntities != null && listEntities.size() != 0) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(getSqlText("insert"));
            try {

                for (T entity :
                        listEntities) {
                    executePrepearedInsert(pstmt, entity);
                }
                setUploaded(true);
                logger.trace("data was uploaded in table: "+getTableName());
            } finally {
                pstmt.close();
            }
        } else {
            logger.error("no data to upload in: "+getTableName());
            throw new NoDataException("Нет данных для загрузки");
        }
    }

    protected abstract String getSqlText(String type);

    protected abstract T getEntity(ResultSet rs) throws SQLException;

    protected abstract void executePrepearedInsert(PreparedStatement pstmt, T entity) throws SQLException;

    protected abstract String getTableName();
}
