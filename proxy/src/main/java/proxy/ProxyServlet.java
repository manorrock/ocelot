package proxy;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class ProxyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("=====================");
        System.out.println(request.getMethod());
        System.out.println(request.getServerName());
        System.out.println(request.getServerPort());
        System.out.println(request.getRequestURL().toString());
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
        response.setStatus(200);
        response.flushBuffer();
    }
}
