package com.innopolis.smoldyrev.dataManager;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by smoldyrev on 16.02.17.
 * Класс работы по marshalling/unmarshalling
 */
public class FileManager {

    private static Logger logger = Logger.getLogger(FileManager.class);

    /**Сохраняет полученный объект
     * технология jaxb
     * @param obj - любой объект
     * @param filePath - в указанный путь
     * */
    public static void saveToFile(Object obj, String filePath) {
        File file = new File(filePath);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, file);
            logger.trace(obj.getClass().getSimpleName() + " was serialized to " + filePath);
        } catch (JAXBException e) {
            logger.error(e);
            System.out.println(e.getMessage());
        }
    }

    /**Загружает сериализованный в xml объект
     * в object
     * @param c - класс загружаемогообъекта
     * @param filePath - путь к xml
     * @return object - ссылка на десериализоованный объект
     * @throws JAXBException - при ошибке десериализации
     * */
    public static Object getObject(Class c, String filePath) throws JAXBException {
        File file = new File(filePath);
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(file);
        logger.trace("Object was serialized from " + filePath);
        return object;
    }
}
