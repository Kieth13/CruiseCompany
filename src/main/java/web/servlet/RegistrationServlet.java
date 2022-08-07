package web.servlet;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;
import model.enums.Role;
import service.Validate;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private final UserDao userDao = UserDaoImpl.getInstance();
    private final Validate validate = new Validate();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        User user = new User();
        user.setEmail(req.getParameter("regEmail"));
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("regPassword"));
        user.setLastName(req.getParameter("lastName"));
        user.setRole(Role.CUSTOMER);
        user.setBalance(0.0F);

        if (!validate.checkEmail(user.getEmail())){
            userDao.create(user);
            req.getSession().removeAttribute("loginExists");
            resp.sendRedirect(getServletContext().getContextPath() + "/");
        } else {
            req.getSession().setAttribute("loginExists", "This e-mail had been used already. Please, try to use another e-mail");
            resp.sendRedirect(getServletContext().getContextPath() + "/");
        }
    }
}
