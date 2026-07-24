package com.example.JMSCommerce.Exception;

public class DuplicateRecordException extends RuntimeException {
//    public DuplicateRecordException() {
//        super("Duplicate record");
//    }
    public DuplicateRecordException(String message) {
        super("Duplicate record present in database: " + message);
    }
}

