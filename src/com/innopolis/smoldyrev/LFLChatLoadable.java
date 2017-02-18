package com.innopolis.smoldyrev;

import java.sql.SQLException;

/**
 * Created by smoldyrev on 17.02.17.
 */
public interface LFLChatLoadable {

    void loadFromDB() throws SQLException;

}
