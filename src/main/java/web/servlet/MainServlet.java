package web.servlet;

import dao.CruiseDao;
import dao.OrderDao;
import dao.ShipDao;
import dao.UserDao;
import dao.impl.OrderDaoImpl;
import dao.impl.CruiseDaoImpl;
import dao.impl.ShipDaoImpl;
import dao.impl.UserDaoImpl;
import model.Cruise;
import model.Order;
import model.Ship;
import model.User;
import model.enums.Role;
import model.enums.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@WebServlet("")
public class MainServlet extends HttpServlet {
    private final CruiseDao cruiseDao = CruiseDaoImpl.getInstance();
    private final UserDao userDao = UserDaoImpl.getInstance();
    private final OrderDao orderDao = OrderDaoImpl.getInstance();
    private final ShipDao shipDao = ShipDaoImpl.getInstance();


    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String role = String.valueOf(req.getSession().getAttribute("role"));
        String login = String.valueOf(req.getSession().getAttribute("email"));
        String password = String.valueOf(req.getSession().getAttribute("password"));
        HttpSession session = req.getSession();
        if (role.equals("CUSTOMER")) {
            User user = userDao.getByEmailAndPassword(login, password);
            req.setAttribute("userName", user.getName());
            req.setAttribute("userBalance", user.getBalance());
        }
        if (nonNull(session.getAttribute("cruises"))) {
            req.setAttribute("searched", true);
            req.getRequestDispatcher("index.jsp").forward(req, resp);

        } else {
            List<Cruise> cruises = cruiseDao.findAll();
            req.setAttribute("cruises", cruises);
            req.setAttribute("ships", findAlCruiseShips(cruises));
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action= req.getParameter("action");
        switch (action) {
            case "searchByDate": {
                Date dayStart = Date.valueOf(req.getParameter("startDate"));
                List<Cruise> cruises = cruiseDao.getByStartDate(dayStart);
                req.getSession().setAttribute("cruises", cruises);
                req.getSession().setAttribute("ships", findAlCruiseShips(cruises));
                resp.sendRedirect(getServletContext().getContextPath() + "/");
                break;
            }
            case "searchByDays": {
                int days = Integer.parseInt(req.getParameter("numOfDays"));
                List<Cruise> cruises = cruiseDao.getByDays(days);
                req.getSession().setAttribute("cruises", cruises);
                req.getSession().setAttribute("ships", findAlCruiseShips(cruises));
                resp.sendRedirect(getServletContext().getContextPath() + "/");
                break;
            }
            case "cancelSearch" : {
                final HttpSession session = req.getSession();
                session.removeAttribute("cruises");
                session.removeAttribute("ships");
                session.removeAttribute("searched");
                resp.sendRedirect(getServletContext().getContextPath() + "/");
                break;
            }
            case "reserve":
                String login = String.valueOf(req.getSession().getAttribute("email"));
                String password = String.valueOf(req.getSession().getAttribute("password"));
                User user = userDao.getByEmailAndPassword(login, password);
                if (user.getRole() == Role.CUSTOMER) {
                    Order order = new Order();
                    int cruiseId = Integer.parseInt(req.getParameterValues("cruiseId")[0]);
                    order.setCruiseId(cruiseId);
                    order.setUserId(user.getId());
                    int numOfPassengers = Integer.parseInt(req.getParameter("numOfPassengers"));
                    order.setNumOfPassengers(numOfPassengers);
                    order.setTotalPrice(cruiseDao.getById(cruiseId).getPrice()*numOfPassengers);
                    order.setStatus(Status.RESERVED);
                    orderDao.create(order);
                    req.setAttribute("user", user);
                    resp.sendRedirect(getServletContext().getContextPath() + "/");
                } else {
                    req.getSession().setAttribute("errorMessage", "");
                    resp.sendRedirect(getServletContext().getContextPath() + "/");
                }
                break;
        }
    }

    private List<Ship> findAlCruiseShips(List<Cruise> cruises) {
        List<Ship> cruiseShips = new ArrayList<>();
        for (Cruise cruise : cruises) {
            cruiseShips.add(shipDao.getById(cruise.getShipId()));
        }
        return cruiseShips;
    }
}
