package web.servlet;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.enums.Role;
import service.Validate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.nonNull;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDao userDao = UserDaoImpl.getInstance();
    private final Validate validate = new Validate();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("loginEmail");
        String password = req.getParameter("loginPassword");
        HttpSession session = req.getSession();

        if (nonNull(session) &&
                nonNull(session.getAttribute("email")) &&
                nonNull(session.getAttribute("password"))) {
            Role role = (Role) session.getAttribute("role");
            if (role.equals(Role.ADMIN)) {
                resp.sendRedirect("admin/orders");
            } else if (role.equals(Role.CUSTOMER)) {
                resp.sendRedirect(getServletContext().getContextPath() + "/");
            }
        } else if (validate.checkUser(email, password)) {
            Role role = userDao.getByEmailAndPassword(email, password).getRole();
            int id = userDao.getByEmailAndPassword(email, password).getId();
            req.getSession().setAttribute("password", password);
            req.getSession().setAttribute("email", email);
            req.getSession().setAttribute("role", role);
            req.getSession().setAttribute("id", id);
            if (role.equals(Role.ADMIN)) {
                req.getSession().removeAttribute("errorMessage");
                resp.sendRedirect("admin/orders");
            } else if (role.equals(Role.CUSTOMER)) {
                req.getSession().removeAttribute("errorMessage");
                resp.sendRedirect(getServletContext().getContextPath() + "/");
            }
        } else {
            req.getSession().setAttribute("errorMessage", "Illegal email or password!");
            resp.sendRedirect(getServletContext().getContextPath() + "/");
        }
    }
}
