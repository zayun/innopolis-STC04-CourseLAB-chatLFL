package com.innopolis.smoldyrev.entity.language;

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
 * Created by smoldyrev on 17.02.17.
 */
@XmlType
@XmlRootElement(name = "Group")
public class LanguageList implements LFLChatLoadable {

    private static volatile boolean downloaded = false;

    private static List<Language> languages = new ArrayList<>();

    public static boolean isDownloaded() {
        return downloaded;
    }

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
        if (downloaded) languages = null;
        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.loadFromDB();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"d_Languages\"");
        while (rs.next()) {
            Language language = new Language(rs.getString("ShortName"),
                    rs.getString("FullName"), rs.getString("dialekt"));
            languages.add(language);
        }
        rs.close();
        stmt.close();
        downloaded = true;
    }

    public static Language getLangOnShortName(String id) {
        for (Language lang:
                languages) {
            if (id.equals(lang.getShortName())) return lang;
        }
        return null;
    }
}
