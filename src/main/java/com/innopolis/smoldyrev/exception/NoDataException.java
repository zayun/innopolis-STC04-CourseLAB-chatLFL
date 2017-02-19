package com.innopolis.smoldyrev.exception;

/**
 * Created by smoldyrev on 18.02.17.
 * Exception при попытке десериализации пустого объекта
 */
public class NoDataException extends Exception {

    public NoDataException(String message) {

        super(message);

    }

}
