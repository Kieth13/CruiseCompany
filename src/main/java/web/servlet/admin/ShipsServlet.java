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
import java.util.List;

import static java.lang.Integer.parseInt;

@WebServlet("/admin/ships")
public class ShipsServlet extends HttpServlet {
    private final ShipDao shipDao = ShipDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordsPerPage = 5;
        int currentPage = 1;

        String recordsPerPageString = req.getParameter("recordsPerPage");
        if (recordsPerPageString != null && !recordsPerPageString.isEmpty()) {
            recordsPerPage = parseInt(req.getParameter("recordsPerPage"));
            currentPage = parseInt(req.getParameter("currentPage"));
        }

        List<Ship> ships = shipDao.getAllShipsByLimitAndOffset(currentPage, recordsPerPage);
        req.setAttribute("ships", ships);
        req.setAttribute("noOfPages", countNumberOfPage(recordsPerPage));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);

        req.getRequestDispatcher("ships.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int shipId = parseInt(req.getParameterValues("shipId")[0]);
        shipDao.deleteById(shipId);
        resp.sendRedirect("ships");
    }

    private int countNumberOfPage(int recordsPerPage) {
        int rows = shipDao.getNumberOfRows();
        int nOfPages = rows / recordsPerPage;
        if (nOfPages % recordsPerPage > 0) {
            nOfPages++;
        }
        return nOfPages;
    }
}


