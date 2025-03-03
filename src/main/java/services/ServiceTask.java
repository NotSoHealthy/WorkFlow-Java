package services;

import entity.Task;
import entity.Employee;
import entity.Project;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTask implements IService<Task> {
    private Connection con;

    public ServiceTask() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Task task) throws SQLException {
        if (con == null) {
            System.out.println("Connection Error");
            return;
        }
        String query = "INSERT INTO task (title, description, status, priority, start_date, due_date, " +
                "completion_date, assigned_to, project_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
        ps.setString(3, task.getStatus());
        ps.setString(4, task.getPriority());
        ps.setDate(5, task.getStartDate());
        ps.setDate(6, task.getDueDate());
        ps.setDate(7, task.getCompletionDate());
        ps.setInt(8, task.getAssignedTo().getId()); // Assuming Employee has an getId() method
        ps.setInt(9, task.getProject().getProject_id()); // Assuming Project has a getProject_id() method
        ps.setTimestamp(10, task.getCreatedAt());
        ps.setTimestamp(11, task.getUpdatedAt());

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                task.setTaskId(rs.getInt(1)); // Set the generated task_id
            }
        }
        ps.close();
        System.out.println(rowsAffected + " row(s) added");
    }

    @Override
    public void update(Task task) throws SQLException {
        String query = "UPDATE task SET title = ?, description = ?, status = ?, priority = ?, start_date = ?, " +
                "due_date = ?, completion_date = ?, assigned_to = ?, project_id = ?, updated_at = ? WHERE task_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
        ps.setString(3, task.getStatus());
        ps.setString(4, task.getPriority());
        ps.setDate(5, task.getStartDate());
        ps.setDate(6, task.getDueDate());
        ps.setDate(7, task.getCompletionDate());
        ps.setInt(8, task.getAssignedTo().getId());
        ps.setInt(9, task.getProject().getProject_id());
        ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
        ps.setInt(11, task.getTaskId());

        int rowsAffected = ps.executeUpdate();
        ps.close();
        System.out.println(rowsAffected + " row(s) updated");
    }

    @Override
    public void delete(Task task) throws SQLException {
        String query = "DELETE FROM task WHERE task_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, task.getTaskId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Task> readAll() throws SQLException {
        String query = "SELECT * FROM task";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Task> tasks = new ArrayList<>();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        ServiceProject serviceProject = new ServiceProject();

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
        ps.close();
        return tasks;
    }

    @Override
    public Task readById(int id) throws SQLException {
        String query = "SELECT * FROM task WHERE task_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        ServiceProject serviceProject = new ServiceProject();
        if (rs.next()) {
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
            ps.close();
            return task;
        }
        ps.close();
        return null;
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
}