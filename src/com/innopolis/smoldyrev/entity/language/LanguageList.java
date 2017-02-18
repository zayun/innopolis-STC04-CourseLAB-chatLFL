package com.innopolis.smoldyrev.entity.language;

import com.innopolis.smoldyrev.entity.AbstractEntityList;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.message.Message;
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
@XmlRootElement(name = "Group")
public class LanguageList extends AbstractEntityList {

    private static List<Language> languages = new ArrayList<>();

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        LanguageList.languages = languages;
    }

    public void add(Language language) {
        this.languages.add(language);
    }

    public synchronized void loadFromDB() throws SQLException {
        if (isDownloaded()) languages = null;
        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.getStatement();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"d_Languages\"");
        while (rs.next()) {
            Language language = new Language(rs.getString("ShortName"),
                    rs.getString("FullName"), rs.getString("dialekt"));
            languages.add(language);
        }
        rs.close();
        stmt.close();
        setDownloaded(true);
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {

        if (languages != null) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(
                    "INSERT INTO \"Main\".\"d_Languages\"(\n" +
                            "\t\"ShortName\", \"FullName\", dialekt)\n" +
                            "\tVALUES (?, ?, ?);");
            try {
                for (Language language :
                        languages) {
                    pstmt.setString(1, language.getShortName());
                    pstmt.setString(2, language.getFullName());
                    pstmt.setString(3, language.getDialekt());
                    pstmt.executeUpdate();
                }
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }

    public static Language getLangOnShortName(String id) {
        for (Language lang:
                languages) {
            if (id.equals(lang.getShortName())) return lang;
        }
        return null;
    }

}
