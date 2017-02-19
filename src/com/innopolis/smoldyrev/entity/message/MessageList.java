package com.innopolis.smoldyrev.entity.message;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.entity.AbstractEntityList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.sql.*;
import java.util.List;

@XmlType
@XmlRootElement
public class MessageList extends AbstractEntityList<Message> {

    private List<Message> messages = super.getEntityList();

    @XmlElement
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        super.setEntityList(messages);
    }

    public Message getEntityOnID(String id) {
        return null;
    }

    public Message getEntityOnID(int id) {
        return null;
    }

    protected Message getEntity(ResultSet rs) throws SQLException {
        Message message =
                new Message(rs.getInt("id"),
                        rs.getTimestamp("DateTime"),
                        Main.users.getEntityOnID(rs.getInt("FromUserID")),
                        Main.users.getEntityOnID(rs.getInt("ToUserID")),
                        rs.getString("BodyText"), rs.getBoolean("isRead"), rs.getInt("chatroom"));
        return message;
    }

    protected void executePrepearedInsert(PreparedStatement pstmt, Message message) throws SQLException {

        pstmt.setInt(1, message.getId());
        pstmt.setInt(2, message.getFromUser().getUserID());
        pstmt.setInt(3, message.getToUser().getUserID());
        pstmt.setString(4, message.getBodyText());
        pstmt.setBoolean(5, message.isViewed());
        pstmt.setTimestamp(6, new Timestamp(message.getDate().getTime()));
        pstmt.setInt(7, message.getChatRoom());

        pstmt.executeUpdate();

    }

    @Override
    protected String getTableName() {
        return "r_Messages";
    }

    public String getSqlText(String type) {
        if (type.equals("insert")) {
            String sqlText = "INSERT INTO \"Main\".\"r_Messages\"(\n" +
                    "\tid, \"FromUserID\", \"ToUserID\", " +
                    "\"BodyText\", \"isRead\", \"DateTime\", \"chatroom\")\n" +
                    "\tVALUES (?, ?, ?, ?, ?, ?, ?);";
            return sqlText;
        } else if (type.equals("select")) {
            String sqlText = "select * from \"Main\".\"r_Messages\"";
            return sqlText;
        } else return "";
    }
}
