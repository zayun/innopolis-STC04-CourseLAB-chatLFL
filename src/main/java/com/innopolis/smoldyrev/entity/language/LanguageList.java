package com.innopolis.smoldyrev.entity.language;


import com.innopolis.smoldyrev.entity.AbstractEntityList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.*;
import java.util.List;

@XmlType
@XmlRootElement(name = "Group")
public class LanguageList extends AbstractEntityList<Language> {

    private List<Language> languages = super.getEntityList();

    @XmlElement
    public List<Language> getLanguages() {;
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        super.setEntityList(languages);
    }

    public Language getEntityOnID(String id) {
        for (Language language :
                languages) {
            if (language.getShortName().equals(id)) {
                return language;
            }
        }
        return null;
    }

    public Language getEntityOnID(int id) {
        return getEntityOnID(String.valueOf(id));
    }

    protected Language getEntity(ResultSet rs) throws SQLException {
        Language language = new Language(rs.getString("ShortName"),
                rs.getString("FullName"), rs.getString("dialekt"));
        return language;
    }

    protected void executePrepearedInsert(PreparedStatement pstmt, Language language) throws SQLException {

        pstmt.setString(1, language.getShortName());
        pstmt.setString(2, language.getFullName());
        pstmt.setString(3, language.getDialekt());
        pstmt.executeUpdate();
    }

    @Override
    protected String getTableName() {
        return "d_Languages";
    }

    public String getSqlText(String type) {
        if (type.equals("insert")) {
            String sqlText = "INSERT INTO \"Main\".\"d_Languages\"(\n" +
                    "\t\"ShortName\", \"FullName\", dialekt)\n" +
                    "\tVALUES (?, ?, ?);";
            return sqlText;
        } else if (type.equals("select")) {
            String sqlText = "select * from \"Main\".\"d_Languages\"";
            return sqlText;
        } else return "";
    }
}