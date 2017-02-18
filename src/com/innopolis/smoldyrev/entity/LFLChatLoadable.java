package com.innopolis.smoldyrev.entity;

import com.innopolis.smoldyrev.exception.NoDataException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by smoldyrev on 17.02.17.
 */
public interface LFLChatLoadable {

    void loadFromDB() throws SQLException;

    void uploadToDB() throws SQLException, NoDataException;
}
