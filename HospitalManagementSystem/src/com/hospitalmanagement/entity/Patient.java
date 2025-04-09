package com.hospitalmanagement.entity;

import java.time.LocalDate;

public class Patient {
	//instance variables
    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String contactNumber;
    private String address;

    // Default Constructor
    public Patient() {
    }

    // Parameterized Constructor
    public Patient(int patientId, String firstName, String lastName, LocalDate dateOfBirth, String gender, String contactNumber, String address) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters - access private fields
    public int getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    // Setters - modify them
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    //override annotations(optional)
    //1.Used to avoid mistakes like toSting() (typo), the compiler will show an error instead of silently treating it as a new method.
    //2.Overridden Method (Parent Class Method): toString() in Object class.
    //3.Overriding Method (Child Class Method): toString() in Doctor class.
    
    @Override
    public String toString() {
        return "Patient{" +
               "patientId=" + patientId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", gender='" + gender + '\'' +
               ", contactNumber='" + contactNumber + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}