package services;

import entity.Task;
import utils.DBConnection;
import entity.Employee;
import entity.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTask implements IService<Task> {
    private final Connection con;
    private final ServiceEmployee serviceEmployee;
    private final ServiceProject serviceProject;

    public ServiceTask() {
        this.con = DBConnection.getInstance().getConnection();
        this.serviceEmployee = new ServiceEmployee();
        this.serviceProject = new ServiceProject();
    }

    @Override
    public void add(Task task) throws SQLException {
        if (con == null) {
            System.out.println("Connection Error");
            return;
        }
        String query = "INSERT INTO task (title, description, status, priority, start_date, due_date, " +
                "completion_date, assigned_to, project_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getPriority());
            ps.setDate(5, task.getStartDate());
            ps.setDate(6, task.getDueDate());
            ps.setDate(7, task.getCompletionDate());
            ps.setObject(8, task.getAssignedTo() != null ? task.getAssignedTo().getId() : null);
            ps.setInt(9, task.getProject().getProject_id()); // Assumes project is required
            ps.setTimestamp(10, task.getCreatedAt());
            ps.setTimestamp(11, task.getUpdatedAt());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setTaskId(rs.getInt(1));
                    }
                }
            }
            System.out.println(rowsAffected + " row(s) added");
        }
    }

    @Override
    public void update(Task task) throws SQLException {
        String query = "UPDATE task SET title = ?, description = ?, status = ?, priority = ?, start_date = ?, " +
                "due_date = ?, completion_date = ?, assigned_to = ?, project_id = ?, updated_at = ? WHERE task_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getPriority());
            ps.setDate(5, task.getStartDate());
            ps.setDate(6, task.getDueDate());
            ps.setDate(7, task.getCompletionDate());
            ps.setObject(8, task.getAssignedTo() != null ? task.getAssignedTo().getId() : null);
            ps.setInt(9, task.getProject().getProject_id()); // Assumes project is required
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            ps.setInt(11, task.getTaskId());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated");
        }
    }

    @Override
    public void delete(Task task) throws SQLException {
        String query = "DELETE FROM task WHERE task_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, task.getTaskId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted");
        }
    }

    @Override
    public List<Task> readAll() throws SQLException {
        String query = "SELECT * FROM task";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                Employee employee = serviceEmployee.readById(rs.getInt("assigned_to"));
                Project project = serviceProject.readById(rs.getInt("project_id"));
                Task task = new Task(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getDate("start_date"),
                        rs.getDate("due_date"),
                        rs.getDate("completion_date"),
                        employee,
                        project,
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                tasks.add(task);
            }
            return tasks;
        }
    }
    public List<Task> getTaskByEmployee(Employee employee) throws SQLException {
        String query = "SELECT * FROM task WHERE assigned_to = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, employee.getId());
        ResultSet rs = ps.executeQuery();
        List<Task> tasks = new ArrayList<>();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        ServiceProject serviceProject = new ServiceProject();
        while (rs.next()) {
            Employee employee1 = serviceEmployee.readById(rs.getInt("assigned_to"));
            Project project = serviceProject.readById(rs.getInt("project_id"));
            tasks.add(new Task(
                    rs.getInt("task_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getString("priority"),
                    rs.getDate("start_date"),
                    rs.getDate("due_date"),
                    rs.getDate("completion_date"),
                    employee,
                    project,
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
            ));
        }
        return tasks;
    }

    @Override
    public Task readById(int id) throws SQLException {
        String query = "SELECT * FROM task WHERE task_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee employee = serviceEmployee.readById(rs.getInt("assigned_to"));
                    Project project = serviceProject.readById(rs.getInt("project_id"));
                    return new Task(
                            rs.getInt("task_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("priority"),
                            rs.getDate("start_date"),
                            rs.getDate("due_date"),
                            rs.getDate("completion_date"),
                            employee,
                            project,
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                }
            }
            return null;
        }
    }

    public List<Task> getTasksByProjectId(int projectId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM task WHERE project_id = ?";
        Project project = serviceProject.readById(projectId);
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee assignedTo = serviceEmployee.readById(rs.getInt("assigned_to"));
                    Task task = new Task(
                            rs.getInt("task_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("priority"),
                            rs.getDate("start_date"),
                            rs.getDate("due_date"),
                            rs.getDate("completion_date"),
                            assignedTo,
                            project,
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    tasks.add(task);
                }
            }
        }
        System.out.println("Fetched " + tasks.size() + " tasks for project ID: " + projectId);
        return tasks;
    }

    public void updateStatus(int taskId, String newStatus) throws SQLException {
        String query = "UPDATE task SET status = ? WHERE task_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, taskId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated for task " + taskId + " to status: " + newStatus);
        }
    }
}