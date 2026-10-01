package gui;

import database.DatabaseHelper;
import model.Course;
import model.Department;
import model.Lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private DatabaseHelper db = new DatabaseHelper();

    private JList<Department> departmentList;
    private JList<Lecturer> lecturerList;
    private JList<Course> courseList;

    private DefaultListModel<Department> departmentModel = new DefaultListModel<>();
    private DefaultListModel<Lecturer> lecturerModel = new DefaultListModel<>();
    private DefaultListModel<Course> courseModel = new DefaultListModel<>();

    // Colors
    private final Color PRIMARY = new Color(72, 52, 212);
    private final Color SECONDARY = new Color(99, 79, 237);
    private final Color BACKGROUND = new Color(245, 244, 255);
    private final Color PANEL_BG = Color.WHITE;
    private final Color TEXT_COLOR = new Color(40, 40, 40);
    private final Color HEADER_TEXT = Color.WHITE;
    private final Color LIST_SELECTED = new Color(72, 52, 212);

    public MainWindow() {
        setTitle("Kisii University — Lecturer Course Tracker");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // ── TOP LOGO/HEADER AREA ──
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY);
        headerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel logo = new JLabel("  Kisii University");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(HEADER_TEXT);

        JLabel subtitle = new JLabel("SIST Faculty - Lecturer Course Tracker");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(200, 195, 255));

        JPanel logoTextPanel = new JPanel();
        logoTextPanel.setLayout(new BoxLayout(logoTextPanel, BoxLayout.Y_AXIS));
        logoTextPanel.setBackground(PRIMARY);
        logoTextPanel.add(logo);
        logoTextPanel.add(subtitle);

        headerPanel.add(logoTextPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ── MAIN CONTENT AREA ──
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Department Panel
        contentPanel.add(createPanel("  Departments", departmentModel,
                departmentList = new JList<>(departmentModel)));

        // Lecturer Panel
        contentPanel.add(createPanel(" Lecturers", lecturerModel,
                lecturerList = new JList<>(lecturerModel)));

        // Course Panel
        contentPanel.add(createPanel("Courses", courseModel,
                courseList = new JList<>(courseModel)));

        add(contentPanel, BorderLayout.CENTER);

        // ── LOAD DATA ──
        loadDepartments();

        // When department is clicked
        departmentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Department selected = departmentList.getSelectedValue();
                if (selected != null) {
                    loadLecturers(selected.getId());
                    courseModel.clear();
                }
            }
        });

        // When lecturer is clicked
        lecturerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Lecturer selected = lecturerList.getSelectedValue();
                if (selected != null) {
                    loadCourses(selected.getId());
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private <T> JPanel createPanel(String title, DefaultListModel<T> model, JList<T> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 215, 255), 1),
                new EmptyBorder(0, 0, 8, 0)
        ));

        // Panel header
        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(HEADER_TEXT);
        header.setBackground(SECONDARY);
        header.setOpaque(true);
        header.setBorder(new EmptyBorder(10, 14, 10, 14));
        panel.add(header, BorderLayout.NORTH);

        // Style the list
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setForeground(TEXT_COLOR);
        list.setBackground(PANEL_BG);
        list.setSelectionBackground(LIST_SELECTED);
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(36);
        list.setBorder(new EmptyBorder(4, 10, 4, 10));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadDepartments() {
        departmentModel.clear();
        for (Department d : db.getDepartments()) departmentModel.addElement(d);
    }

    private void loadLecturers(int departmentId) {
        lecturerModel.clear();
        for (Lecturer l : db.getLecturersByDepartment(departmentId)) lecturerModel.addElement(l);
    }

    private void loadCourses(int lecturerId) {
        courseModel.clear();
        for (Course c : db.getCoursesByLecturer(lecturerId)) courseModel.addElement(c);
    }
}