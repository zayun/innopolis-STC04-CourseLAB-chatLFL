package com.innopolis.smoldyrev;

import com.innopolis.smoldyrev.entity.language.LangOwnerList;
import com.innopolis.smoldyrev.entity.language.LanguageList;
import com.innopolis.smoldyrev.entity.message.MessageList;
import com.innopolis.smoldyrev.entity.person.PersonList;
import com.innopolis.smoldyrev.entity.user.UserList;

public class Main {

    public static PersonList persones = new PersonList();
    public static UserList users = new UserList();
    public static LanguageList languages = new LanguageList();
    public static LangOwnerList langOwners = new LangOwnerList();
    public static MessageList messageList = new MessageList();

    public static void main(String[] args){

        SerializeAllTables.go();
//        DeserializeAllTables.go();

    }
}
