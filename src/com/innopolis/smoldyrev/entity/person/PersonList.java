package com.innopolis.smoldyrev.entity.person;

import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 16.02.17.
 */
@XmlType
@XmlRootElement
public class PersonList extends AbstractEntityList {


    private static List<Person> persons = new ArrayList<>();

    public List<Person> getPersons() {
        return persons;
    }

    public void add(Person person) {
        this.persons.add(person);
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public synchronized void loadFromDB() throws SQLException {
        if (isDownloaded()) persons = null;
        DatabaseManager dbm = new DatabaseManager();

        Statement stmt = dbm.getStatement();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"d_Persons\"");
        while (rs.next()) {
            Person person = new Person(rs.getInt("personID"), rs.getString("FirstName"), rs.getString("LastName"),
                    rs.getString("email"), rs.getString("phoneNumber"),
                    rs.getDate("birthDay"), rs.getBoolean("male"));
            persons.add(person);
        }
        rs.close();
        stmt.close();
        setDownloaded(true);
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {

        if (persons != null) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(
                    "INSERT INTO \"Main\".\"d_Persons\"(\n" +
                            "\t\"personID\", \"FirstName\", \"LastName\", " +
                            "\"birthDay\", email, \"phoneNumber\", male)\n" +
                            "\tVALUES (?, ?, ?, ?, ?, ?, ?);");
            try {

                for (Person person :
                        persons) {

                    pstmt.setInt(1, person.getId());
                    pstmt.setString(2, person.getFirstName());
                    pstmt.setString(3, person.getLastName());
                    pstmt.setDate(4, new Date(person.getBirthDay().getYear()
                            , person.getBirthDay().getMonth(),
                            person.getBirthDay().getDay()));
                    pstmt.setString(5, person.getEmail());
                    pstmt.setString(6, person.getPhoneNumber());
                    pstmt.setBoolean(7, person.isMale());
                    pstmt.executeUpdate();
                }
                setUploaded(true);
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }

    public static Person getPersonOnID(int id) {
        for (Person person :
                persons) {
            if (person.getId() == id) return person;
        }
        return null;
    }

}
