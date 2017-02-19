package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.exception.NoDataException;

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

    private List<T> listEntities = new ArrayList<>();

    private boolean downloaded = false;

    private boolean uploaded = false;

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

    public void loadFromDB() throws SQLException {
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
    }

    public void uploadToDB() throws SQLException, NoDataException {
        if (listEntities != null && listEntities.size() == 0) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(getSqlText("insert"));
            try {

                for (T entity :
                        listEntities) {
                    executePrepearedInsert(pstmt, entity);
                }
                setUploaded(true);
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }

    public abstract String getSqlText(String type);

    protected abstract T getEntity(ResultSet rs) throws SQLException;

    protected abstract void executePrepearedInsert(PreparedStatement pstmt, T entity) throws SQLException;
}
