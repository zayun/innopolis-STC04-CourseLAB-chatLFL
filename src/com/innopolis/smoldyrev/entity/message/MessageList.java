package com.innopolis.smoldyrev.entity.message;

import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.user.UserList;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 17.02.17.
 */
@XmlType
@XmlRootElement
public class MessageList extends AbstractEntityList{

    private static List<Message> messages = new ArrayList<>();

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

        if (isDownloaded()) messages = null;

        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.getStatement();
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
        setDownloaded(true);
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {

        if (messages != null) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(
                    "INSERT INTO \"Main\".\"r_Messages\"(\n" +
                            "\tid, \"FromUserID\", \"ToUserID\", " +
                            "\"BodyText\", \"isRead\", \"DateTime\")\n" +
                            "\tVALUES (?, ?, ?, ?, ?, ?);");
            try {
                for (Message message :
                        messages) {
                    pstmt.setInt(1, message.getId());
                    pstmt.setInt(2, message.getFromUser().getUserID());
                    pstmt.setInt(3, message.getToUser().getUserID());
                    pstmt.setString(4, message.getBodyText());
                    pstmt.setBoolean(5, message.isViewed());
                    pstmt.setTimestamp(6, new Timestamp(message.getDate().getTime()));

                    pstmt.executeUpdate();
                }
                setUploaded(true);
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }
}
