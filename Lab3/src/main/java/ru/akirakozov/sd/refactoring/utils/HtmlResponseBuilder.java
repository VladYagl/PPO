package ru.akirakozov.sd.refactoring.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("UnusedReturnValue")
public class HtmlResponseBuilder {
    private final HttpServletResponse response;
    private final StringBuilder body = new StringBuilder();

    public HtmlResponseBuilder(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse buildPlain() throws IOException {
        response.getWriter().print(body.toString());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        return response;
    }

    public HttpServletResponse build() throws IOException {
        response.getWriter().print("<html><body>\n" + body.toString() + "</body></html>\n");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        return response;
    }

    public HtmlResponseBuilder addProduct(Product product) {
        if (product != null) {
            body.append(product.getName()).append("\t").append(product.getPrice()).append("</br>\n");
        } else {
            body.append("null");
        }
        return this;
    }

    public HtmlResponseBuilder addH1(String s) {
        body.append("<h1>").append(s).append("</h1>\n");
        return this;
    }

    public HtmlResponseBuilder addLine(String s) {
        body.append(s).append("\n");
        return this;
    }
}
