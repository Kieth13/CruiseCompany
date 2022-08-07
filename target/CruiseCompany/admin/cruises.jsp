<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cruise Company</title>
    <link rel="stylesheet" href="../bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="../styles/styles.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.1/css/flag-icon.min.css">
</head>
<body>
    <div class="container">
        <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
            <span class="fs-4"><fmt:message key="admin.header.hi"/></span>

            <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
                <li><a href="orders" class="nav-link px-2 link-dark"><fmt:message key="admin.header.orders"/></a></li>
                <li><a href="cruises" class="nav-link px-2 link-dark link-act"><fmt:message key="admin.header.cruises"/></a></li>
                <li><a href="ships" class="nav-link px-2 link-dark"><fmt:message key="admin.header.ships"/></a></li>
                <li><a href="users" class="nav-link px-2 link-dark"><fmt:message key="admin.header.users"/></a></li>
            </ul>

            <div class="text-end">
                <a href="../logout" class="btn btn-outline-primary me-2"><fmt:message key="header.logout"/></a>
                <a class="btn btn-outline-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    &#127760;
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <a class="dropdown-item" href="?sessionLocale=en"><span class="flag-icon flag-icon-us"></span> <fmt:message key="lang.eng"/></a>
                    <a class="dropdown-item" href="?sessionLocale=ua"><span class="flag-icon flag-icon-ua"></span> <fmt:message key="lang.ukr"/></a>
                </div>
            </div>
        </header>
    </div>

    <section class="section-cruises">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="section-header"><fmt:message key="admin.header.cruises"/></h2>
                <a href="" class="btn btn-success mr-2" data-bs-toggle="modal" data-bs-target="#modalAddCruiseForm" id="addCruise"><fmt:message key="admin.button.addCruise"/></a>
            </div>
            <div class="admin-container">
                <table class="table table-bordered" id="myTable">
                    <thead>
                        <tr>
                            <th><fmt:message key="admin.tableHead.cruiseID"/></th>
                            <th><fmt:message key="admin.tableHead.cruiseName"/></th>
                            <th><fmt:message key="admin.tableHead.shipID"/></th>
                            <th><fmt:message key="admin.tableHead.shipName"/></th>
                            <th><fmt:message key="admin.tableHead.passengers"/></th>
                            <th><fmt:message key="admin.tableHead.route"/></th>
                            <th><fmt:message key="admin.tableHead.dates"/></th>
                            <th><fmt:message key="admin.tableHead.price"/></th>
                        </tr>
                    </thead>
                    <c:forEach items="${cruises}" var="amount" varStatus="generalIndex">
                        <c:set var="index" value="${generalIndex.index}"/>
                        <c:forEach items="${ships}" var="ships" varStatus="shipsIndex">
                            <c:if test="${shipsIndex.index == index}">
                                <c:set var="shipId" value="${ships.id}" scope="request" />
                                <c:set var="shipName" value="${ships.name}" scope="request" />
                                <c:set var="shipPassengerAmount" value="${ships.passengerAmount}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${cruises}" var="cruises" varStatus="cruisesIndex">
                            <c:if test="${cruisesIndex.index == index}">
                                <tr class="admin-row">
                                    <td>${cruises.id}</td>
                                    <td>${cruises.name}</td>
                                    <td>${requestScope.shipId}</td>
                                    <td>${requestScope.shipName}</td>
                                    <td>${cruises.placesReserved}/${requestScope.shipPassengerAmount}</td>
                                    <td>${cruises.routeFrom} -> ${cruises.numOfPorts} -> ${cruises.routeTo}</td>
                                    <td>${cruises.dayStart} - ${cruises.dayEnd}</td>
                                    <td>${cruises.price}</td>
                                    <td>
                                        <form method="post" action="cruises">
                                            <input type="hidden" name="cruiseId" value="${cruises.id}">
                                            <button type="submit" class="btn btn-danger me-2" name="btn" value="confirm"><fmt:message key="admin.button.delete"/></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </table>
            </div>

            <div class="navigation">
                <form method="get" action="cruises">
                    <div class="input-group">
                        <input type="hidden" name="currentPage" value="1">
                        <input type="hidden" name="status" value=${status}>
                        <span class="input-group-text"><fmt:message key="admin.nav.items"/></span>
                        <select class="form-select" id = "records" name="recordsPerPage">
                            <option value="1">1</option>
                            <option value="3">3</option>
                            <option value="5" selected>5</option>
                            <option value="10">10</option>
                        </select>
                        <button type="submit" class="btn btn-primary"><fmt:message key="admin.button.submit"/></button>
                    </div>
                </form>
                <nav>
                    <ul class="pagination justify-content-end">
                        <c:if test="${currentPage != 1}">
                            <li class="page-item">
                                <a class="page-link" href="cruises?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}&status=${status}"><fmt:message key="admin.nav.previous"/></a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${noOfPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <li class="page-item active">
                                        <a class="page-link"> ${i}</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link" href="cruises?recordsPerPage=${recordsPerPage}&currentPage=${i}&status=${status}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage lt noOfPages}">
                            <li class="page-item">
                                <a class="page-link" href="cruises?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}&status=${status}"><fmt:message key="admin.nav.next"/></a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </section>

    <div class="modal fade" id="modalAddCruiseForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold"><fmt:message key="admin.addCruise.header"/></h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body mx-3">
                    <form name="form" method="post" action="addCruise" onsubmit="return validate()">
                        <div class="mb-3">
                            <label for="cruiseName" class="form-label"><fmt:message key="admin.addCruise.cruiseName"/></label>
                            <input type="text" class="form-control" id="cruiseName" placeholder='<fmt:message key="admin.addCruise.cruiseName"/>' name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="shipId" class="form-label"><fmt:message key="admin.addCruise.chooseShip"/></label>
                            <select name="shipId" class="form-control" required>
                                <c:forEach items="${allShips}" var="allShips">
                                    <option value="${allShips.id}">${allShips.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="routeFrom" class="form-label"><fmt:message key="admin.addCruise.routeFrom"/></label>
                            <input type="text" class="form-control" id="routeFrom" placeholder='<fmt:message key="admin.addCruise.routeFrom"/>' name="routeFrom" required>
                        </div>
                        <div class="mb-3">
                            <label for="routeFrom" class="form-label"><fmt:message key="admin.addCruise.routeTo"/></label>
                            <input type="text" class="form-control" id="routeTo" placeholder='<fmt:message key="admin.addCruise.routeTo"/>' name="routeTo"  required>
                        </div>
                        <div class="mb-3">
                            <label for="numOfPorts" class="form-label"><fmt:message key="admin.addCruise.numberOfPorts"/></label>
                            <input type="number" class="form-control" id="numOfPorts" placeholder='<fmt:message key="admin.addCruise.numberOfPorts"/>' name="numOfPorts" min="1" required>
                        </div>
                        <div class="mb-3">
                            <label for="dayStart" class="form-label"><fmt:message key="admin.addCruise.startDate"/></label>
                            <input type="date" class="form-control" id="dayStart" placeholder='<fmt:message key="admin.addCruise.startDate"/>' name="dayStart" min="2022-07-01" required>
                        </div>
                        <div class="mb-3">
                            <label for="dayEnd" class="form-label"><fmt:message key="admin.addCruise.endDate"/></label>
                            <input type="date" class="form-control" id="dayEnd" placeholder='<fmt:message key="admin.addCruise.endDate"/>' name="dayEnd" min="2022-07-01" required>
                        </div>
                        <div class="mb-3">
                            <label for="price" class="form-label"><fmt:message key="admin.addCruise.price"/></label>
                            <input type="text" class="form-control" id="price" placeholder="Price" name="price" required>
                        </div>
                        <button type="submit" class="btn btn-primary"><fmt:message key="admin.button.submit"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>

	<script>
        function validate() {
            var price = document.forms["form"]["price"].value;
            var firstDate = document.forms["form"]["dayStart"].value;
            var secondDate = document.forms["form"]["dayEnd"].value;

            if (isNaN(price)) {
                alert("Price must be a number more than 0");
                return false;
            } else if (price <= 0) {
                alert("Price must be a number more than 0");
                return false;
            } else if (firstDate >= secondDate) {
                alert("Seems like Start date is bigger than End Date. Please correct that mistake");
                return false;
            }
        }
    </script>

    <script>
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth()+1; //January is 0 so need to add 1 to make it 1!
        var yyyy = today.getFullYear();
        if(dd<10){
          dd='0'+dd
        }
        if(mm<10){
          mm='0'+mm
        }

        today = yyyy+'-'+mm+'-'+dd;
        document.getElementById("dayStart").setAttribute("min", today);
        document.getElementById("dayEnd").setAttribute("min", today);
    </script>

    <script src="../bootstrap/js/bootstrap.bundle.js"></script>

</body>
</html>