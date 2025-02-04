package services;

import java.sql.*;
import java.util.List;


public interface Service<T> {
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    void update(T t, int oldCIN) throws SQLException;

}