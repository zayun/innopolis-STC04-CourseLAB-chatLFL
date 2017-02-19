package com.innopolis.smoldyrev.entity.language;

import com.innopolis.smoldyrev.entity.person.Person;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by smoldyrev on 16.02.17.
 * Класс сущности, хранимой в таблице r_LangOwners
 * реализованы геттеры и сеттеры всех полей
 */
@XmlType(propOrder={"person","language","level"})
@XmlRootElement
public class LangOwner {

    private Person person;
    private Language language;
    private int level;

    /**Дефолтный конструктор
     * требуется для JAXB
     * */
    public LangOwner() {
    }

    public LangOwner(Person person, Language language, int level) {
        this.person = person;
        this.language = language;
        this.level = level;
    }

    @XmlElement
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @XmlElement
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @XmlElement
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
