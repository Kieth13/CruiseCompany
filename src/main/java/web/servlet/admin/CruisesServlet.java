package web.servlet.admin;

import dao.CruiseDao;
import dao.ShipDao;
import dao.impl.CruiseDaoImpl;
import dao.impl.ShipDaoImpl;
import model.Cruise;
import model.Ship;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/cruises")
public class CruisesServlet extends HttpServlet {
    private final CruiseDao cruiseDao = CruiseDaoImpl.getInstance();
    private final ShipDao shipDao = ShipDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordsPerPage = 5;
        int currentPage = 1;

        String recordsPerPageString = req.getParameter("recordsPerPage");
        if (recordsPerPageString != null && !recordsPerPageString.isEmpty()) {
            recordsPerPage = Integer.parseInt(req.getParameter("recordsPerPage"));
            currentPage = Integer.parseInt(req.getParameter("currentPage"));
        }

        List<Cruise> cruises = cruiseDao.getAllCruisesByLimitAndOffset(currentPage, recordsPerPage);
        req.setAttribute("cruises", cruises);
        req.setAttribute("allShips", shipDao.findAll());
        req.setAttribute("ships", findCruiseShips(cruises));
        req.setAttribute("noOfPages", countNumberOfPage(recordsPerPage));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);

        req.getRequestDispatcher("cruises.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cruiseId = Integer.parseInt(req.getParameterValues("cruiseId")[0]);
        cruiseDao.deleteById(cruiseId);
        resp.sendRedirect("cruises");
    }

    private List<Ship> findCruiseShips(List<Cruise> cruises) {
        List<Ship> ships = new ArrayList<>();
        for (Cruise cruise : cruises) {
            ships.add(shipDao.getById(cruise.getShipId()));
        }
        return ships;
    }

    private int countNumberOfPage(int recordsPerPage) {
        int rows = cruiseDao.getNumberOfRows();
        int nOfPages = rows / recordsPerPage;
        if (nOfPages % recordsPerPage > 0) {
            nOfPages++;
        }
        return nOfPages;
    }
}
