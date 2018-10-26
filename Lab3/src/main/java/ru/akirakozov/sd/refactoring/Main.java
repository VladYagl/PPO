package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.utils.DbManager;
import ru.akirakozov.sd.refactoring.utils.ProductTable;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DbManager db = new DbManager("jdbc:sqlite:test.db");
        ProductTable table = new ProductTable(db);
        table.init();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(table)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(table)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(table)), "/query");

        server.start();
//        server.join();
    }
}
