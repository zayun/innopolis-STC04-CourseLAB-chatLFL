package com.innopolis.smoldyrev.dataManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by smoldyrev on 16.02.17.
 */
public class FileManager {

    public static void saveToFile(Object obj, String filePath) {
        File file = new File(filePath);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, file);
            //jaxbMarshaller.marshal(obj, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static Object getObject(File file, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(file);

        return object;
    }
}
