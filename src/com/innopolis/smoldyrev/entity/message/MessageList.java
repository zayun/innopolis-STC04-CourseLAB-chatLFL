package com.innopolis.smoldyrev.entity.message;

import com.innopolis.smoldyrev.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.user.User;
import com.innopolis.smoldyrev.entity.user.UserList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 17.02.17.
 */
@XmlType
@XmlRootElement
public class MessageList implements LFLChatLoadable {

    private static volatile boolean downloaded = false;

    private static List<Message> messages = new ArrayList<>();

    public static boolean isDownloaded() {
        return downloaded;
    }

    public void setMessages(List<Message> messages) {
        MessageList.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void add(Message message) {
        this.messages.add(message);
    }

    public synchronized void loadFromDB() throws SQLException {

        if (downloaded) messages = null;

        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.loadFromDB();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"r_Messages\"");
        while (rs.next()) {
            Message message =
                    new Message(rs.getInt("id"),
                            rs.getTimestamp("DateTime"),
                            UserList.getUserOnID(rs.getInt("FromUserID")),
                            UserList.getUserOnID(rs.getInt("ToUserID")),
                    rs.getString("BodyText"), rs.getBoolean("isRead"));
            messages.add(message);
        }
        rs.close();
        stmt.close();
        downloaded = true;
    }
}
