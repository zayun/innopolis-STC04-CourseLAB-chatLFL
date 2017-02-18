package com.innopolis.smoldyrev.threads;

import com.innopolis.smoldyrev.dataManager.FileManager;
import com.innopolis.smoldyrev.entity.LFLChatLoadable;
import com.innopolis.smoldyrev.exception.NoDataException;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;

/**
 * Created by smoldyrev on 18.02.17.
 */
public class ThreadForDeserialize implements Runnable {

    private LFLChatLoadable obj;
    private String filePath;

    public ThreadForDeserialize(LFLChatLoadable obj, String filePath) {
        this.obj = obj;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        try {
            obj = (LFLChatLoadable) FileManager.getObject(obj.getClass(),filePath);
            obj.uploadToDB();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }


    }
}
