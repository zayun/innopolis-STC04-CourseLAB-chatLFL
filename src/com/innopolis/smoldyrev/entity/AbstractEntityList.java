package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.exception.NoDataException;

import java.sql.SQLException;

/**
 * Created by smoldyrev on 18.02.17.
 */
public abstract class AbstractEntityList implements LFLChatLoadable{

    private static volatile boolean downloaded = false;

    private static volatile boolean uploaded = false;

    public static boolean isDownloaded() {
        return downloaded;
    }

    public static void setDownloaded(boolean downloaded) {
        AbstractEntityList.downloaded = downloaded;
    }

    public static boolean isUploaded() {
        return uploaded;
    }

    public static void setUploaded(boolean uploaded) {
        AbstractEntityList.uploaded = uploaded;
    }

    public abstract void loadFromDB() throws SQLException;

    public abstract void uploadToDB() throws SQLException, NoDataException;
}
