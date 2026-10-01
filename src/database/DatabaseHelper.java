package database;

import model.Course;
import model.Department;
import model.Lecturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // Get all departments
    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM department");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                departments.add(new Department(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return departments;
    }

    // Get lecturers by department
    public List<Lecturer> getLecturersByDepartment(int departmentId) {
        List<Lecturer> lecturers = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM lecturer WHERE department_id = ?");
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lecturers.add(new Lecturer(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lecturers;
    }

    // Get courses by lecturer
    public List<Course> getCoursesByLecturer(int lecturerId) {
        List<Course> courses = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM course WHERE lecturer_id = ?");
            stmt.setInt(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("course_name")));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return courses;
    }
}
