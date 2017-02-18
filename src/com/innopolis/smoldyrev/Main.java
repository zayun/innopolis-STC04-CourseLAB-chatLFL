package com.innopolis.smoldyrev;

public class Main {

    public static void main(String[] args){

        DeserializeAllTables.go();



//
//        PersonList pl = new PersonList();
//        LFLChatLoadable obj = pl;
//        File file = new File("temp/persones.xml");

//        DatabaseManager.initDatabase();
//        ThreadForSerialize.setPack(false);
//        MessageList messageList = new MessageList();
//        Thread t5 = new Thread(new ThreadForSerialize(messageList, "temp/messages2.xml"));
//        t5.start();


//        try {
//            t5.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        MessageList ml = new MessageList();
//        obj = ml;
//        file = new File("temp/messages.xml");
//
//        try {
//            obj = (LFLChatLoadable) FileManager.getObject(obj.getClass(),file);
//            try {
//                obj.uploadToDB();
//            } catch (NoDataException e) {
//                System.out.println(e.getMessage());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        } catch (JAXBException e) {
//            System.out.println(e.getMessage());
//        }

//        DatabaseManager.closeConnection();

//        try {
//            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
//            try {
//                obj.uploadToDB();
//            } catch (NoDataException e) {
//                System.out.println(e.getMessage());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        } catch (JAXBException e) {
//            System.out.println(e.getMessage());
//        }
//
//        UserList ul = new UserList();
//        obj = ul;
//        file = new File("temp/users.xml");
//
//        try {
//            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
//            try {
//                obj.uploadToDB();
//            } catch (NoDataException e) {
//                System.out.println(e.getMessage());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        } catch (JAXBException e) {
//            System.out.println(e.getMessage());
//        }
//
//        LanguageList ll = new LanguageList();
//        obj = ll;
//        file = new File("temp/languages.xml");
//
//        try {
//            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
//            try {
//                obj.uploadToDB();
//            } catch (NoDataException e) {
//                System.out.println(e.getMessage());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        } catch (JAXBException e) {
//            System.out.println(e.getMessage());
//        }
//
//        LangOwnerList lol = new LangOwnerList();
//        obj = lol;
//        file = new File("temp/langOwners.xml");
//
//        try {
//            obj = (LFLChatLoadable) FileManager.getObject(file,obj.getClass());
//            try {
//                obj.uploadToDB();
//            } catch (NoDataException e) {
//                System.out.println(e.getMessage());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        } catch (JAXBException e) {
//            System.out.println(e.getMessage());
//        }




    }
}
