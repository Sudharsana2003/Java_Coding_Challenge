package com.hospitalmanagement.exception;


//Checked Exception - 1. Direct child class of java.lang.Exception 2.Signal from Compiler 3.program can reasonably be expected to handle gracefully with try-catch or throws
public class PatientNumberNotFoundException extends Exception {

	
	//Constructor with - Method with same name as class name
	//This is Constructor/Method Overloading - as long in Same class
	//parameter - type,count,order
    public PatientNumberNotFoundException(String message) {
        super(message);
    }

    public PatientNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}