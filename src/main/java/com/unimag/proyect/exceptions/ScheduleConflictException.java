package com.unimag.proyect.exceptions;

public class ScheduleConflictException extends RuntimeException{
    public ScheduleConflictException(String message){
        super(message);
    }
}
