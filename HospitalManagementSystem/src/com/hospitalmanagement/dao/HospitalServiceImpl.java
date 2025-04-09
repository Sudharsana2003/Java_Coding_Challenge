package com.hospitalmanagement.dao;

import com.hospitalmanagement.entity.Appointment;
import com.hospitalmanagement.exception.PatientNumberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import com.hospitalmanagement.util.*;

public class HospitalServiceImpl implements IHospitalService {

    private Connection getConnection() throws SQLException {
        return DBConnUtil.getConnection();
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) throws PatientNumberNotFoundException {
        Appointment appointment = null;
        String sql = "SELECT appointmentId, patientId, doctorId, appointmentDate, description FROM appointment WHERE appointmentId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                appointment = new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        rs.getObject("appointmentDate", LocalDate.class),
                        rs.getString("description")
                );
            } else {
                throw new PatientNumberNotFoundException("Appointment with ID " + appointmentId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PatientNumberNotFoundException("Database error while retrieving appointment: " + e.getMessage());
        }
        return appointment;
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(int patientId) throws PatientNumberNotFoundException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT appointmentId, patientId, doctorId, appointmentDate, description FROM appointment WHERE patientId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        rs.getObject("appointmentDate", LocalDate.class),
                        rs.getString("description")
                ));
            }
            if (appointments.isEmpty()) {
                String checkPatientSql = "SELECT patientId FROM patient WHERE patientId = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkPatientSql)) {
                    checkStmt.setInt(1, patientId);
                    ResultSet patientRs = checkStmt.executeQuery();
                    if (!patientRs.next()) {
                        throw new PatientNumberNotFoundException("Patient with ID " + patientId + " not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PatientNumberNotFoundException("Database error while retrieving appointments for patient: " + e.getMessage());
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(int doctorId) throws PatientNumberNotFoundException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT appointmentId, patientId, doctorId, appointmentDate, description FROM appointment WHERE doctorId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("patientId"),
                        rs.getInt("doctorId"),
                        rs.getObject("appointmentDate", LocalDate.class),
                        rs.getString("description")
                ));
            }
            String checkDoctorSql = "SELECT doctorId FROM doctor WHERE doctorId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkDoctorSql)) {
                checkStmt.setInt(1, doctorId);
                ResultSet doctorRs = checkStmt.executeQuery();
                if (appointments.isEmpty() && !doctorRs.next()) {
                    throw new PatientNumberNotFoundException("Doctor with ID " + doctorId + " not found."); // Consider DoctorNotFoundException
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PatientNumberNotFoundException("Database error while retrieving appointments for doctor: " + e.getMessage());
        }
        return appointments;
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointment (patientId, doctorId, appointmentDate, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setObject(3, appointment.getAppointmentDate());
            pstmt.setString(4, appointment.getDescription());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAppointment(Appointment appointment) throws PatientNumberNotFoundException {
        String sql = "UPDATE appointment SET patientId = ?, doctorId = ?, appointmentDate = ?, description = ? WHERE appointmentId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setObject(3, appointment.getAppointmentDate());
            pstmt.setString(4, appointment.getDescription());
            pstmt.setInt(5, appointment.getAppointmentId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PatientNumberNotFoundException("Appointment with ID " + appointment.getAppointmentId() + " not found for update.");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PatientNumberNotFoundException("Database error while updating appointment: " + e.getMessage());
        }
    }

    @Override
    public boolean cancelAppointment(int appointmentId) throws PatientNumberNotFoundException {
        String sql = "DELETE FROM appointment WHERE appointmentId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appointmentId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new PatientNumberNotFoundException("Appointment with ID " + appointmentId + " not found for cancellation.");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PatientNumberNotFoundException("Database error while cancelling appointment: " + e.getMessage());
        }
    }
}