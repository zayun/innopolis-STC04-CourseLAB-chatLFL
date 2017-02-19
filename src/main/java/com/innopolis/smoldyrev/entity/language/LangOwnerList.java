package com.innopolis.smoldyrev.entity.language;

import com.innopolis.smoldyrev.Main;
import com.innopolis.smoldyrev.entity.AbstractEntityList;


import javax.xml.bind.annotation.XmlElement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlRootElement(name = "Group")
public class LangOwnerList extends AbstractEntityList<LangOwner> {

    private List<LangOwner> langOwners = super.getEntityList();

    @XmlElement
    public List<LangOwner> getLangOwners() {
        return langOwners;
    }

    public void setLangOwners(List<LangOwner> langOwners) {
        super.setEntityList(langOwners);
    }

    public LangOwner getEntityOnID(String id) {
        return null;
    }

    public LangOwner getEntityOnID(int id) {
        return null;
    }

    protected LangOwner getEntity(ResultSet rs) throws SQLException {
        LangOwner langOwner = new LangOwner(
                Main.persones.getEntityOnID(rs.getInt("idPerson")),
                Main.languages.getEntityOnID(rs.getString("idLang")),
                rs.getInt("level"));

        return langOwner;
    }

    protected void executePrepearedInsert(PreparedStatement pstmt, LangOwner langOwner) throws SQLException {

        pstmt.setInt(1, langOwner.getPerson().getId());
        pstmt.setString(2, langOwner.getLanguage().getShortName());
        pstmt.setInt(3, langOwner.getLevel());
        pstmt.executeUpdate();

    }

    @Override
    protected String getTableName() {
        return "r_LangOwner";
    }

    public String getSqlText(String type) {
        if (type.equals("insert")) {
            String sqlText = "INSERT INTO \"Main\".\"r_LangOwner\"(\n" +
                    "\t\"idPerson\", \"idLang\", level)\n" +
                    "\tVALUES (?, ?, ?);";
            return sqlText;
        } else if (type.equals("select")) {
            String sqlText = "select * from \"Main\".\"r_LangOwner\"";
            return sqlText;
        } else return "";
    }
}