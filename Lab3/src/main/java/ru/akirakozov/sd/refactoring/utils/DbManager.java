package ru.akirakozov.sd.refactoring.utils;

import java.sql.*;

public class DbManager {
    private String url;

    public DbManager(String url) {
        this.url = url;
    }

    public void update(String sql) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url)) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    public void query(String sql, SQLConsumer<ResultSet> callback) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            callback.accept(rs);
            rs.close();
            stmt.close();
        }
    }
}
