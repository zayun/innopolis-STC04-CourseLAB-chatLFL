package com.innopolis.smoldyrev.entity.person;

import com.innopolis.smoldyrev.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smoldyrev on 16.02.17.
 */
@XmlType
@XmlRootElement
public class PersonList implements LFLChatLoadable {

    private static volatile boolean downloaded = false;

    private static List<Person> persons = new ArrayList<>();

    public static boolean isDownloaded() {
        return downloaded;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void add(Person person) {
        this.persons.add(person);
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void loadFromDB() throws SQLException {
        if (downloaded) persons = null;
        DatabaseManager dbm = new DatabaseManager();

        Statement stmt = dbm.loadFromDB();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"d_Persons\"");
        while (rs.next()) {
            Person person = new Person(rs.getInt("personID"), rs.getString("FirstName"), rs.getString("LastName"),
                    rs.getString("email"), rs.getString("phoneNumber"),
                    rs.getDate("birthDay"), rs.getBoolean("male"));
            persons.add(person);
        }
        rs.close();
        stmt.close();
        downloaded = true;
    }

    public static Person getPersonOnID(int id) {
        for (Person person :
                persons) {
            if (person.getId() == id) return person;
        }
        return null;
    }

}
