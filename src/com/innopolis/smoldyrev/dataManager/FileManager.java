package com.innopolis.smoldyrev.dataManager;

import com.innopolis.smoldyrev.entity.AbstractEntityList;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by smoldyrev on 16.02.17.
 */
public class FileManager {

    private static Logger logger = Logger.getLogger(FileManager.class);

    public static void saveToFile(Object obj, String filePath) {
        File file = new File(filePath);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, file);
            logger.trace(obj.getClass() + " was serialized to " + filePath);
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static Object getObject(Class c, String filePath) throws JAXBException {
        File file = new File(filePath);
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(file);
        logger.trace("Object was serialized from " + filePath);
        return object;
    }
}
