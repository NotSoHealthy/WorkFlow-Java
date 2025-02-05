package services;

import java.sql.*;
import java.util.List;


public interface IService<T> {
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    List<T> readAll() throws SQLException;
    T readById(int id) throws SQLException;
}