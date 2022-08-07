package web.servlet;

import dao.CruiseDao;
import dao.OrderDao;
import dao.ShipDao;
import dao.UserDao;
import dao.impl.CruiseDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.ShipDaoImpl;
import dao.impl.UserDaoImpl;
import model.Cruise;
import model.Order;
import model.Ship;
import model.User;
import model.enums.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
@WebServlet("/userCruises")
public class UserCruisesServlet extends HttpServlet {
    private final UserDao userDaoImpl = UserDaoImpl.getInstance();
    private final ShipDao shipDaoImpl = ShipDaoImpl.getInstance();
    private final CruiseDao cruiseDaoImpl = CruiseDaoImpl.getInstance();
    private final OrderDao orderDaoImpl = OrderDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = String.valueOf(req.getSession().getAttribute("role"));
        String login = String.valueOf(req.getSession().getAttribute("email"));
        String password = String.valueOf(req.getSession().getAttribute("password"));

        if (role.equals("CUSTOMER")) {
            User user = userDaoImpl.getByEmailAndPassword(login, password);
            List<Order> userOrderedCruises = orderDaoImpl.getAllOrdersByUserId(user.getId());
            req.setAttribute("orders", userOrderedCruises);
            List<Cruise> userCruises = findAllUserCruises(userOrderedCruises);
            req.setAttribute("user", user);
            req.setAttribute("cruises", userCruises);
            req.setAttribute("ships", findAlCruiseShips(userCruises));
            req.getRequestDispatcher("userCruises.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("logout");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String button_param = req.getParameter("button");
        String login = String.valueOf(req.getSession().getAttribute("email"));
        String password = String.valueOf(req.getSession().getAttribute("password"));
        User user = userDaoImpl.getByEmailAndPassword(login, password);
        int cruiseId = Integer.parseInt(req.getParameter("cruiseId"));
        Order order = orderDaoImpl.getByCruiseAndUser(cruiseDaoImpl.getById(cruiseId).getId(), user.getId());
        if(button_param.equals("pay")) {
            float newBalance = user.getBalance()-order.getTotalPrice();
            if (newBalance >= 0) {
                user.setBalance(newBalance);
                userDaoImpl.update(user);
                order.setStatus(Status.PAID);
                orderDaoImpl.update(order);
                resp.sendRedirect("userCruises");
            } else {
                req.getSession().setAttribute("notEnoughMoney", "Sorry, you don't have enough money on your balance.");
                resp.sendRedirect("userCruises");
            }

        } else if(button_param.equals("cancel")){
            orderDaoImpl.deleteById(order.getId());
            resp.sendRedirect("userCruises");
        } else if(button_param.equals("upload")){
            Part filePart = req.getPart("file");
            String fileName = "Order" + order.getId() + ".pdf";
            String fileType = filePart.getContentType();
            if (fileType.equals("application/pdf")) {
                String outputFile = getServletContext().getRealPath("files/").concat(fileName);
                InputStream fileContent = filePart.getInputStream();
                Files.copy(fileContent, Paths.get(outputFile), StandardCopyOption.REPLACE_EXISTING);
                order.setUserDocs(fileName);
                orderDaoImpl.update(order);
            } else {
                req.getSession().setAttribute("errorMsg", "File needs to be in pdf");
            }

            resp.sendRedirect("userCruises");
        }

    }

    private List<Cruise> findAllUserCruises(List<Order> userOrderedCruises) {
        List<Cruise> userCruises = new ArrayList<>();
        for (Order order: userOrderedCruises) {
            userCruises.add(cruiseDaoImpl.getById(order.getCruiseId()));
        }
        return userCruises;
    }

    private List<Ship> findAlCruiseShips(List<Cruise> cruises) {
        List<Ship> cruiseShips = new ArrayList<>();
        for (Cruise cruise : cruises) {
            cruiseShips.add(shipDaoImpl.getById(cruise.getShipId()));
        }
        return cruiseShips;
    }

}
