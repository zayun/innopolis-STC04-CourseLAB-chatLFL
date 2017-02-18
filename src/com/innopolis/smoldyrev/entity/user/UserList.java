package com.innopolis.smoldyrev.entity.user;

import com.innopolis.smoldyrev.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.person.PersonList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 16.02.17.
 */

@XmlType
@XmlRootElement(name = "Group")
public class UserList implements LFLChatLoadable {

    private static volatile boolean downloaded = false;

    private static List<User> users = new ArrayList<>();

    public static boolean isDownloaded() {
        return downloaded;
    }

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

        if (downloaded) users = null;

        DatabaseManager dbm = new DatabaseManager();
        ResultSet rs = dbm.loadFromDB("select * from \"Main\".\"d_Users\"");
        while (rs.next()) {
            User user = new User(rs.getInt("userID"), rs.getString("login"), rs.getString("pwd"),
                    PersonList.getPersonOnID(rs.getInt("personID")));
            users.add(user);
        }
        rs.close();
        downloaded = true;
    }

    public static User getUserOnID(int id) {
        for (User user:
                users) {
            if (user.getUserID()==id) return user;
        }
        return null;
    }
}
