package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.HtmlResponseBuilder;
import ru.akirakozov.sd.refactoring.utils.ProductTable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductTable table;

    public QueryServlet(ProductTable table) {
        this.table = table;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlResponseBuilder builder = new HtmlResponseBuilder(response);

        if ("max".equals(command)) {
            builder.addH1("Product with max price: ").addProduct(table.maxPrice());
        } else if ("min".equals(command)) {
            builder.addH1("Product with min price: ").addProduct(table.minPrice());
        } else if ("sum".equals(command)) {
            builder.addLine("Summary price: ").addLine(String.valueOf(table.sum()));
        } else if ("count".equals(command)) {
            builder.addLine("Number of products: ").addLine(String.valueOf(table.count()));
        } else {
            builder.addLine("Unknown command: " + command);
        }

        builder.build();
    }

}
