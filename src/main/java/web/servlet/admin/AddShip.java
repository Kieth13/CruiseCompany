package web.servlet.admin;

import dao.ShipDao;
import dao.impl.ShipDaoImpl;
import model.Ship;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/addShip")
public class AddShip extends HttpServlet {
    private final ShipDao shipDao = ShipDaoImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Ship ship = new Ship();
        ship.setName(req.getParameter("name"));
        ship.setPassengerAmount(Integer.parseInt(req.getParameter("passengerAmount")));
        try {
            shipDao.create(ship);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.sendRedirect("ships");
    }
}
