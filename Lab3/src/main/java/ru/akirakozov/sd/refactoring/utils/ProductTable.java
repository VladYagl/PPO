package ru.akirakozov.sd.refactoring.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductTable {
    private final DbManager db;

    public ProductTable(DbManager db) {
        this.db = db;
    }

    private List<Product> query(String sql) {
        try {
            List<Product> result = new ArrayList<>();
            db.query(sql, rs -> {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");

                    result.add(new Product(name, price));
                }
            });

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int queryInt(String sql) {
        try {
            final int[] result = new int[1];
            db.query(sql, rs -> {
                if (rs.next()) {
                    result[0] = rs.getInt(1);
                } else {
                    throw new SQLException("Wrong query: \"" + sql + "\"");
                }
            });

            return result[0];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";

        try {
            db.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> get() {
        return this.query("SELECT * FROM PRODUCT");
    }

    public Product maxPrice() {
        List<Product> list = this.query("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public Product minPrice() {
        List<Product> list = this.query("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public int sum() {
        return this.queryInt("SELECT SUM(price) FROM PRODUCT");
    }

    public int count() {
        return this.queryInt("SELECT COUNT(*) FROM PRODUCT");
    }

    public void add(Product product) {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";

        try {
            db.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
