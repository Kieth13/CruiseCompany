package web.servlet.admin;

import dao.CruiseDao;
import dao.impl.CruiseDaoImpl;
import model.Cruise;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/admin/addCruise")
public class AddCruise extends HttpServlet {
    private final CruiseDao cruiseDao = CruiseDaoImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Cruise cruise = new Cruise();
        cruise.setName(req.getParameter("name"));
        cruise.setShipId(Integer.parseInt(req.getParameter("shipId")));
        cruise.setPlacesReserved(0);
        cruise.setRouteFrom(req.getParameter("routeFrom"));
        cruise.setRouteTo(req.getParameter("routeTo"));
        cruise.setNumOfPorts(Integer.parseInt(req.getParameter("numOfPorts")));
        cruise.setDayStart(Date.valueOf(req.getParameter("dayStart")));
        cruise.setDayEnd(Date.valueOf(req.getParameter("dayEnd")));
        cruise.setPrice(Float.parseFloat(req.getParameter("price")));
        cruiseDao.create(cruise);
        resp.sendRedirect("cruises");
    }
}
