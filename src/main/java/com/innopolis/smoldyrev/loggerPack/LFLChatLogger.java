package com.innopolis.smoldyrev.loggerPack;

import org.apache.log4j.Logger;

/**
 * Created by smoldyrev on 19.02.17.
 * Спросить на консультациимвыводить ли логгер
 * в отдельный статик класс??? (тогда делать getLogger() synch?)
 */
@Deprecated
public class LFLChatLogger {

    private static Logger logger = Logger.getLogger(LFLChatLogger.class);

    public static Logger getLogger(Class cl) {
        logger = Logger.getLogger(cl);
        return logger;
    }
}
