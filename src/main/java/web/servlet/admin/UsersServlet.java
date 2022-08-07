package web.servlet.admin;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class UsersServlet extends HttpServlet {
    private final UserDao userDao = UserDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordsPerPage = 5;
        int currentPage = 1;

        String recordsPerPageString = req.getParameter("recordsPerPage");
        if (recordsPerPageString != null && !recordsPerPageString.isEmpty()) {
            recordsPerPage = Integer.parseInt(req.getParameter("recordsPerPage"));
            currentPage = Integer.parseInt(req.getParameter("currentPage"));
        }

        List<User> users = userDao.getAllUsersByLimitAndOffset(currentPage, recordsPerPage);
        req.setAttribute("users", users);
        req.setAttribute("noOfPages", countNumberOfPage(recordsPerPage));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);

        req.getRequestDispatcher("users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameterValues("userId")[0]);
        userDao.deleteById(userId);
        resp.sendRedirect("users");
    }

    private int countNumberOfPage(int recordsPerPage) {
        int rows = userDao.getNumberOfRows();
        int nOfPages = rows / recordsPerPage;
        if (nOfPages % recordsPerPage > 0) {
            nOfPages++;
        }
        return nOfPages;
    }
}