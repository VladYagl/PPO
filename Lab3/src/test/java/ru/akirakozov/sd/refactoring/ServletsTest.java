package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServletsTest {

    private final static String DB_PATH = "test.db";

    @BeforeEach
    void setUp() {
        try {
            //noinspection ResultOfMethodCallIgnored
            assert new File(DB_PATH).delete();
            Main.main(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String request(String uri) throws IOException {
        URL url = new URL("http://localhost:8081/" + uri);

        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            return in.lines().collect(Collectors.joining());
        }
    }

    private String get() throws IOException {
        return request("get-products");
    }

    private String add(String name, int price) throws IOException {
        return request(String.format("add-product?name=%s&price=%d", name, price));
    }

    private String query(String command) throws IOException {
        return request("query?command=" + command);
    }

    private String expectGet(List<String> names, List<Integer> prices) {
        final StringBuilder body = new StringBuilder();
        for (int i = 0; i < prices.size(); i++) {
            body.append(names.get(i)).append("\t").append(prices.get(i)).append("</br>");
        }
        return "<html><body>" + body.toString() + "</body></html>";
    }

    @Test
    void coreTest() throws IOException {
        assertEquals("<html><body><h1>Product with min price: </h1>null</body></html>", query("min"));
        assertEquals("<html><body><h1>Product with max price: </h1>null</body></html>", query("max"));
        assertEquals("<html><body>Summary price: 0</body></html>", query("sum"));
        assertEquals("<html><body>Number of products: 0</body></html>", query("count"));

        assertEquals("OK", add("black_dildo", 69));
        assertEquals("OK", add("white_dildo", 96));
        assertEquals(expectGet(List.of("black_dildo", "white_dildo"), List.of(69, 96)), get());
        assertEquals("OK", add("anal_lube", 3));
        assertEquals("OK", add("candy_ken_dress", 9999));
        assertEquals(expectGet(
                List.of("black_dildo", "white_dildo", "anal_lube", "candy_ken_dress"),
                List.of(69, 96, 3, 9999)
        ), get());
        assertEquals("<html><body><h1>Product with min price: </h1>anal_lube\t3</br></body></html>", query("min"));
        assertEquals("<html><body><h1>Product with max price: </h1>candy_ken_dress\t9999</br></body></html>", query("max"));
        assertEquals("<html><body>Summary price: 10167</body></html>", query("sum"));
        assertEquals("<html><body>Number of products: 4</body></html>", query("count"));

        assertEquals("<html><body>Unknown command: wrong_command</body></html>", query("wrong_command"));
    }
}