package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.exception.NoDataException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 17.02.17.
 */
public interface LFLChatLoadable <T> {


    void add(T entity);

    void setEntityList(List<T> entity);

    List<T> getEntityList();

    void loadFromDB() throws SQLException;

    void uploadToDB() throws SQLException, NoDataException;

    boolean isDownloaded();

    boolean isUploaded();

    void setDownloaded(boolean dwl);

    void setUploaded(boolean upl);
}