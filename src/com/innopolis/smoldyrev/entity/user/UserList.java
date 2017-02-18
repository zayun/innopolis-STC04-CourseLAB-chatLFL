package com.innopolis.smoldyrev.entity.user;

import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 16.02.17.
 */

@XmlType
@XmlRootElement(name = "Group")
public class UserList extends AbstractEntityList{

    private static List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void add(User user) {
        this.users.add(user);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public synchronized void loadFromDB() throws SQLException {

        if (isDownloaded()) users = null;

        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.getStatement();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"d_Users\"");
        while (rs.next()) {
            User user = new User(rs.getInt("userID"), rs.getString("login"), rs.getString("pwd"),
                    PersonList.getPersonOnID(rs.getInt("personID")));
            users.add(user);
        }
        rs.close();
        stmt.close();
        setDownloaded(true);
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {

        if (users != null) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(
                    "INSERT INTO \"Main\".\"d_Users\"(\n" +
                            "\t\"userID\", login, pwd, \"personID\")\n" +
                            "\tVALUES (?, ?, ?, ?)");
            try {
                for (User user :
                        users) {
                    pstmt.setInt(1, user.getUserID());
                    pstmt.setString(2, user.getLogin());
                    pstmt.setString(3, user.getPasswd());
                    pstmt.setInt(4, user.getPerson().getId());
                    pstmt.executeUpdate();
                }
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }

    public static User getUserOnID(int id) {
        for (User user :
                users) {
            if (user.getUserID() == id) return user;
        }
        return null;
    }
}
