package chico.support;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.addHeader("DbSecurityAccess-Control-Allow-Origin", "*");
        resp.addHeader("DbSecurityAccess-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");

        if (req.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
