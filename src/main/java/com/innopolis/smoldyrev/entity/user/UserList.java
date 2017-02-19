package com.innopolis.smoldyrev.entity.user;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.entity.AbstractEntityList;;
import com.innopolis.smoldyrev.entity.person.Person;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.sql.*;
import java.util.List;

@XmlType
@XmlRootElement(name = "Group")
public class UserList extends AbstractEntityList<User> {

    private List<User> users = super.getEntityList();

    @XmlElement
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        super.setEntityList(users);
    }

    public User getEntityOnID(String id) {
        return getEntityOnID(Integer.parseInt(id));
    }

    public User getEntityOnID(int id) {
        for (User user :
                users) {
            if (user.getUserID() == (int) id) {
                return user;
            }
        }
        return null;
    }

    protected User getEntity(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("userID"), rs.getString("userType"),
                rs.getString("login"), rs.getString("pwd"),
                Main.persones.getEntityOnID(rs.getInt("personID")),
                rs.getBoolean("blocked"));

        return user;
    }

    protected void executePrepearedInsert(PreparedStatement pstmt, User user) throws SQLException {

        pstmt.setInt(1, user.getUserID());
        pstmt.setString(2, user.getLogin());
        pstmt.setString(3, user.getPasswd());
        pstmt.setInt(4, user.getPerson().getId());
        pstmt.executeUpdate();
    }

    @Override
    protected String getTableName() {
        return "d_Users";
    }

    public String getSqlText(String type) {

        String sqlText="";

        if (type.equals("insert")) {

            sqlText = "INSERT INTO \"Main\".\"d_Users\"(\n" +
                    "\t\"userID\", login, pwd, \"personID\")\n" +
                    "\tVALUES (?, ?, ?, ?)";
            return sqlText;

        } else if (type.equals("select")) {

            sqlText = "select * from \"Main\".\"d_Users\"";
            return sqlText;

        } else return sqlText;
    }
}
