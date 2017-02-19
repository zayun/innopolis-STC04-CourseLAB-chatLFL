package com.innopolis.smoldyrev.entity.language;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by smoldyrev on 16.02.17.
 * Класс сущности, хранимой в таблице d_Languages
 * реализованы геттеры и сеттеры всех полей
 */
@XmlType(propOrder={"shortName","fullName","dialekt"})
@XmlRootElement
public class Language {

    private String shortName;
    private String fullName;
    private String dialekt;

    /**Дефолтный конструктор
     * требуется для JAXB
     * */
    public Language() {
    }

    public Language(String shortName, String fullName, String dialekt) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.dialekt = dialekt;
    }

    @XmlAttribute(name = "id")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @XmlElement
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @XmlElement
    public String getDialekt() {
        return dialekt;
    }

    public void setDialekt(String dialekt) {
        this.dialekt = dialekt;
    }
}
