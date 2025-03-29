package com.example.supportservice.exception;




public class TicketNotFoundException extends RuntimeException {


    public TicketNotFoundException ( String message) {
        super(message);
    }
}