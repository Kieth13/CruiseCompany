package web.servlet.filter;

import model.enums.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.nonNull;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(false);
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpSRequest = (HttpServletRequest) servletRequest;
        String appRoot = httpSRequest.getRequestURI().substring(0, httpSRequest.getContextPath().length());

        if (nonNull(session) &&
                nonNull(session.getAttribute("email")) &&
                nonNull(session.getAttribute("password"))) {
            Role role = (Role) session.getAttribute("role");
            if (role.equals(Role.ADMIN)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                httpResponse.sendRedirect(appRoot + "/");
            }
        }  else {
            httpResponse.sendRedirect(appRoot + "/");
        }
    }
}
