package services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.*;
import java.util.List;


public interface IService<T> {
    void add(T t) throws SQLException, JsonProcessingException;
    void update(T t) throws SQLException, JsonProcessingException;
    void delete(T t) throws SQLException;
    List<T> readAll() throws SQLException, JsonProcessingException;
    T readById(int id) throws SQLException, JsonProcessingException;
}