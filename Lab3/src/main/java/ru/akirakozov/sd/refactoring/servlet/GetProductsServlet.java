package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.Product;
import ru.akirakozov.sd.refactoring.utils.HtmlResponseBuilder;
import ru.akirakozov.sd.refactoring.utils.ProductTable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductTable table;

    public GetProductsServlet(ProductTable table) {
        this.table = table;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = table.get();
        HtmlResponseBuilder builder = new HtmlResponseBuilder(response);
        for (Product product : products) {
            builder.addProduct(product);
        }

        builder.build();
    }
}
