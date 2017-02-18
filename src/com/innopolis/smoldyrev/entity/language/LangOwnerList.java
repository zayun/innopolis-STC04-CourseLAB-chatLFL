package com.innopolis.smoldyrev.entity.language;

/**
 * Created by smoldyrev on 17.02.17.
 */

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

@XmlType
@XmlRootElement(name = "Group")
public class LangOwnerList extends AbstractEntityList {

    private static List<LangOwner> langOwners = new ArrayList<>();

    public List<LangOwner> getLangOwners() {
        return langOwners;
    }

    public void setLangOwners(List<LangOwner> langOwners) {
        LangOwnerList.langOwners = langOwners;
    }

    public void add(LangOwner langOwner) {
        this.langOwners.add(langOwner);
    }

    public synchronized void loadFromDB() throws SQLException {

        if (isDownloaded()) langOwners = null;

        DatabaseManager dbm = new DatabaseManager();
        Statement stmt = dbm.getStatement();
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
        setDownloaded(true);
    }

    public synchronized void uploadToDB() throws SQLException, NoDataException {

        if (langOwners != null) {
            DatabaseManager dbm = new DatabaseManager();
            PreparedStatement pstmt = dbm.getPrepearedStatement(
                    "INSERT INTO \"Main\".\"r_LangOwner\"(\n" +
                            "\t\"idPerson\", \"idLang\", level)\n" +
                            "\tVALUES (?, ?, ?);");
            try {
                for (LangOwner langOwner :
                        langOwners) {
                    pstmt.setInt(1, langOwner.getPerson().getId());
                    pstmt.setString(2, langOwner.getLanguage().getShortName());
                    pstmt.setInt(3, langOwner.getLevel());
                    pstmt.executeUpdate();
                }
            } finally {
                pstmt.close();
            }
        } else throw new NoDataException("Отсутствуют данные для загрузки");
    }
}
