package com.hospitalmanagement.main;

import com.hospitalmanagement.dao.HospitalServiceImpl;
import com.hospitalmanagement.util.DBConnUtil;
import com.hospitalmanagement.entity.Appointment;
import com.hospitalmanagement.exception.PatientNumberNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainModule {

    private static void displayAppointment(Appointment appointment) {
        if (appointment != null) {
            System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
            System.out.println("| Appointment ID  | Patient ID  | Doctor ID  | Appointment Date | Description                       |");
            System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
            System.out.printf("| %-15d | %-11d | %-10d | %-16s | %-33s |\n",
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getAppointmentDate(),
                    appointment.getDescription());
            System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
        } else {
            System.out.println("No appointment details to display.");
        }
    }

    public static void main(String[] args) {
        boolean running = true; // Add this line
        try {
            DBConnUtil.getConnection();
        } catch (SQLException e) {
            System.out.println("Error initializing database connection: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        HospitalServiceImpl service = new HospitalServiceImpl();

        while (running) { 
            System.out.println("\n----------Sudharsana Hospital Management System----------");
            System.out.println("1. Get Appointment by ID");
            System.out.println("2. Get Appointments for a Patient");
            System.out.println("3. Get Appointments for a Doctor");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Update an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.next();
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Appointment ID: ");
                        try {
                            int appId = scanner.nextInt();
                            Appointment appointment = service.getAppointmentById(appId);
                            displayAppointment(appointment);
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for Appointment ID. Please enter a number.");
                            scanner.next();
                        } catch (PatientNumberNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.print("Enter Patient ID: ");
                        try {
                            int patientId = scanner.nextInt();
                            List<Appointment> patientAppointments = service.getAppointmentsForPatient(patientId);
                            if (!patientAppointments.isEmpty()) {
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                                System.out.println("| Appointment ID  | Patient ID  | Doctor ID  | Appointment Date | Description                       |");
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                                for (Appointment app : patientAppointments) {
                                    System.out.printf("| %-15d | %-11d | %-10d | %-16s | %-33s |\n",
                                            app.getAppointmentId(),
                                            app.getPatientId(),
                                            app.getDoctorId(),
                                            app.getAppointmentDate(),
                                            app.getDescription());
                                }
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                            } else {
                                System.out.println("No appointments found for this patient.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for Patient ID. Please enter a number.");
                            scanner.next();
                        } catch (PatientNumberNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.print("Enter Doctor ID: ");
                        try {
                            int doctorId = scanner.nextInt();
                            List<Appointment> doctorAppointments = service.getAppointmentsForDoctor(doctorId);
                            if (!doctorAppointments.isEmpty()) {
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                                System.out.println("| Appointment ID  | Patient ID  | Doctor ID  | Appointment Date | Description                       |");
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                                for (Appointment app : doctorAppointments) {
                                    System.out.printf("| %-15d | %-11d | %-10d | %-16s | %-33s |\n",
                                            app.getAppointmentId(),
                                            app.getPatientId(),
                                            app.getDoctorId(),
                                            app.getAppointmentDate(),
                                            app.getDescription());
                                }
                                System.out.println("+-----------------+-------------+------------+------------------+-----------------------------------+");
                            } else {
                                System.out.println("No appointments found for this doctor.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for Doctor ID. Please enter a number.");
                            scanner.next();
                        } catch (PatientNumberNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Enter Patient ID: ");
                        try {
                            int newPatientId = scanner.nextInt();
                            System.out.print("Enter Doctor ID: ");
                            int newDoctorId = scanner.nextInt();
                            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
                            String date = scanner.next();
                            scanner.nextLine(); 
                            System.out.print("Enter Description: ");
                            String desc = scanner.nextLine();

                            Appointment newAppointment = new Appointment(0, newPatientId, newDoctorId,
                                    LocalDate.parse(date), desc);

                            boolean scheduled = service.scheduleAppointment(newAppointment);
                            System.out.println(scheduled ? "Appointment scheduled successfully." : "Failed to schedule appointment.");
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for Patient ID or Doctor ID. Please enter a number.");
                            scanner.next();
                        } catch (java.time.format.DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Appointment ID to update: ");
                        try {
                            int updateId = scanner.nextInt();
                            System.out.print("Enter new Patient ID: ");
                            int updPatientId = scanner.nextInt();
                            System.out.print("Enter new Doctor ID: ");
                            int updDoctorId = scanner.nextInt();
                            System.out.print("Enter new Appointment Date (YYYY-MM-DD): ");
                            String updDate = scanner.next();
                            scanner.nextLine(); // consume newline
                            System.out.print("Enter new Description: ");
                            String updDesc = scanner.nextLine();

                            Appointment updatedAppointment = new Appointment(updateId, updPatientId, updDoctorId,
                                    LocalDate.parse(updDate), updDesc);

                            boolean updated = service.updateAppointment(updatedAppointment);
                            System.out.println(updated ? "Appointment updated successfully." : "Failed to update appointment.");
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for IDs. Please enter a number.");
                            scanner.next();
                        } catch (java.time.format.DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        } catch (PatientNumberNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.print("Enter Appointment ID to cancel: ");
                        try {
                            int cancelId = scanner.nextInt();
                            boolean cancelled = service.cancelAppointment(cancelId);
                            System.out.println(cancelled ? "Appointment cancelled successfully." : "Failed to cancel appointment.");
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for Appointment ID. Please enter a number.");
                            scanner.next();
                        } catch (PatientNumberNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 7:
                        System.out.println("----------Thank You Hospital Management System----------");
                        try {
                            DBConnUtil.closeConnection();
                        } catch (SQLException e) {
                            System.out.println("Error closing database connection: " + e.getMessage());
                        } finally {
                            running = false; 
                            scanner.close();
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        System.out.println("Application terminated."); 
        
        
        
    }
}