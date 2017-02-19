package com.innopolis.smoldyrev.entity.person;

import com.innopolis.smoldyrev.entity.AbstractEntityList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.*;
import java.sql.Date;
import java.util.*;




@XmlType(propOrder = {"persons" }, name = "group")
@XmlRootElement
public class PersonList extends AbstractEntityList<Person> {

    private List<Person> persons = super.getEntityList();

    @XmlElement
    public List<Person> getPersons() {;
        return persons;
    }

    public void setPersons(List<Person> persons) {
        super.setEntityList(persons);
    }

    public Person getEntityOnID(String id) {
        return getEntityOnID(Integer.parseInt(id));
    }

    public Person getEntityOnID(int id) {
        for (Person person :
                persons) {
            if (person.getId() == (int) id) {
                return person;
            }
        }
        return null;
    }

    protected Person getEntity(ResultSet rs) throws SQLException {
        Person person = new Person(rs.getInt("personID"),
                rs.getString("FirstName"), rs.getString("LastName"),
                rs.getString("email"), rs.getString("phoneNumber"),
                rs.getDate("birthDay"), rs.getBoolean("male"));

        return person;
    }

    protected void executePrepearedInsert(PreparedStatement pstmt, Person person) throws SQLException {

        pstmt.setInt(1, person.getId());
        pstmt.setString(2, person.getFirstName());
        pstmt.setString(3, person.getLastName());
        pstmt.setDate(4, new Date(person.getBirthDay().getTime()));
        pstmt.setString(5, person.getEmail());
        pstmt.setString(6, person.getPhoneNumber());
        pstmt.setBoolean(7, person.isMale());
        pstmt.executeUpdate();
    }

    @Override
    protected String getTableName() {
        return "d_Persons";
    }

    public String getSqlText(String type) {
        if (type.equals("insert")) {
            String sqlText = "INSERT INTO \"Main\".\"d_Persons\"(\n" +
                    "\t\"personID\", \"FirstName\", \"LastName\", " +
                    "\"birthDay\", email, \"phoneNumber\", male)\n" +
                    "\tVALUES (?, ?, ?, ?, ?, ?, ?);";
            return sqlText;
        } else if (type.equals("select")) {
            String sqlText = "select * from \"Main\".\"d_Persons\"";
            return sqlText;
        } else return "";
    }
}
