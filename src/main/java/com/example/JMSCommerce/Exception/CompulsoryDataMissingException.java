package com.example.JMSCommerce.Exception;


public class CompulsoryDataMissingException extends RuntimeException {
//    public CompulsoryDataMissingException() {
//        super("Some compulsory data missing");
//    }

    public CompulsoryDataMissingException(String message) {
        super("Some compulsory data missing : " + message);
    }

}
