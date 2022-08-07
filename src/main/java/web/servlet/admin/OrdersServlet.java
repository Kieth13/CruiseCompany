package web.servlet.admin;

import dao.OrderDao;
import dao.CruiseDao;
import dao.ShipDao;
import dao.UserDao;
import dao.impl.OrderDaoImpl;
import dao.impl.CruiseDaoImpl;
import dao.impl.ShipDaoImpl;
import dao.impl.UserDaoImpl;
import model.Order;
import model.Cruise;
import model.Ship;
import model.User;
import model.enums.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@WebServlet("/admin/orders")
public class OrdersServlet extends HttpServlet {
    private final UserDao userDao = UserDaoImpl.getInstance();
    private final OrderDao orderDao = OrderDaoImpl.getInstance();
    private final CruiseDao cruiseDao = CruiseDaoImpl.getInstance();
    private final ShipDao shipDao = ShipDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordsPerPage = 5;
        int currentPage = 1;
        Status status = Status.RESERVED;

        String recordsPerPageString = req.getParameter("recordsPerPage");
        if (recordsPerPageString != null && !recordsPerPageString.isEmpty()) {
            recordsPerPage = parseInt(req.getParameter("recordsPerPage"));
            currentPage = parseInt(req.getParameter("currentPage"));
        }

        String statusString = req.getParameter("status");
        if (statusString != null && !statusString.isEmpty()) {
            status = Status.valueOf(statusString);
        }
        List<Order> orders = orderDao.getAllOrdersByStatusAndLimitAndOffset(status, currentPage, recordsPerPage);
        req.setAttribute("orders", orders);
        List<Cruise> cruises = findOrderCruises(orders);
        req.setAttribute("cruises", cruises);
        req.setAttribute("users", findOrderUsers(orders));
        req.setAttribute("ships", findOrderShips(cruises));
        req.setAttribute("status", status.name());
        req.setAttribute("noOfPages", countNumberOfPage(recordsPerPage, status));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.getRequestDispatcher("orders.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String button_param = req.getParameter("button");
        int orderId = parseInt(req.getParameter("orderId"));
        Order order = orderDao.getById(orderId);
        if (button_param.equals("confirm")) {
            Cruise cruise = cruiseDao.getById(order.getCruiseId());
            cruise.setPlacesReserved(cruise.getPlacesReserved() + order.getNumOfPassengers());
            cruiseDao.update(cruise);
            order.setStatus(Status.CONFIRMED);

        } else if (button_param.equals("decline")) {
            order.setStatus(Status.DECLINED);
        }
        orderDao.update(order);
        resp.sendRedirect("orders");
    }

    private List<Cruise> findOrderCruises(List<Order> orders) {
        List<Cruise> cruises = new ArrayList<>();
        for (Order order : orders) {
            cruises.add(cruiseDao.getById(order.getCruiseId()));
        }
        return cruises;
    }

    private List<User> findOrderUsers(List<Order> orders) {
        List<User> users = new ArrayList<>();
        for (Order order : orders) {
            users.add(userDao.getById(order.getUserId()));
        }
        return users;
    }

    private List<Ship> findOrderShips(List<Cruise> cruises) {
        List<Ship> ships = new ArrayList<>();
        for (Cruise cruise : cruises) {
            ships.add(shipDao.getById(cruise.getShipId()));
        }
        return ships;
    }

    private int countNumberOfPage(int recordsPerPage, Status status) {
        int rows = orderDao.getNumberOfRows(status);
        int nOfPages = rows / recordsPerPage;
        if (nOfPages % recordsPerPage > 0) {
            nOfPages++;
        }
        return nOfPages;
    }
}
