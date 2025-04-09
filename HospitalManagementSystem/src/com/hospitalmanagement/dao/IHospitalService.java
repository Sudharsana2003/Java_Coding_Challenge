package com.hospitalmanagement.dao;

//import packages
import com.hospitalmanagement.entity.Appointment;
import com.hospitalmanagement.exception.PatientNumberNotFoundException;
import java.util.List;

public interface IHospitalService {

    Appointment getAppointmentById(int appointmentId) throws PatientNumberNotFoundException;

    List<Appointment> getAppointmentsForPatient(int patientId) throws PatientNumberNotFoundException;

    List<Appointment> getAppointmentsForDoctor(int doctorId) throws PatientNumberNotFoundException;

    boolean scheduleAppointment(Appointment appointment);

    boolean updateAppointment(Appointment appointment) throws PatientNumberNotFoundException; 

    boolean cancelAppointment(int appointmentId) throws PatientNumberNotFoundException; 
}