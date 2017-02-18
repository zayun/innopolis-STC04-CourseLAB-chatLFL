package com.innopolis.smoldyrev.entity.language;

/**
 * Created by smoldyrev on 17.02.17.
 */

import com.innopolis.smoldyrev.LFLChatLoadable;
import com.innopolis.smoldyrev.dataManager.DatabaseManager;
import com.innopolis.smoldyrev.entity.person.PersonList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@XmlType
@XmlRootElement(name = "Group")
public class LangOwnerList implements LFLChatLoadable {

    private static List<LangOwner> langOwners = new ArrayList<>();

    private static volatile boolean downloaded = false;

    public List<LangOwner> getLangOwners() {
        return langOwners;
    }

    public static boolean isDownloaded() {
        return downloaded;
    }

    public void setLangOwners(List<LangOwner> langOwners) {
        LangOwnerList.langOwners = langOwners;
    }

    public void add(LangOwner langOwner) {
        this.langOwners.add(langOwner);
    }

    public synchronized void loadFromDB() throws SQLException {

        if (downloaded) langOwners = null;

        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.loadFromDB();
        ResultSet rs = stmt.executeQuery("select * from \"Main\".\"r_LangOwner\"");
        while (rs.next()) {
            LangOwner langOwner = new LangOwner(
                    PersonList.getPersonOnID(rs.getInt("idPerson")),
                    LanguageList.getLangOnShortName(rs.getString("idLang")),
                    rs.getInt("level"));
            langOwners.add(langOwner);
        }
        rs.close();
        stmt.close();
        downloaded = true;
    }
/*похоже не понадобится*/
/*    public static LangOwner getLangOwner(int personId, String langId) {
        for (LangOwner langOwner :
                langOwners) {
            if ((langOwner.getPerson().getId() == personId)
                    && (langId.equals(langOwner.getLanguage().getShortName()))) {
                return langOwner;
            }
        }
        return null;
    }*/

}
