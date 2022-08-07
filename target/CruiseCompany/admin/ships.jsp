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
                <li><a href="cruises" class="nav-link px-2 link-dark"><fmt:message key="admin.header.cruises"/></a></li>
                <li><a href="ships" class="nav-link px-2 link-dark link-act"><fmt:message key="admin.header.ships"/></a></li>
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

    <section class="section-ships">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="section-header"><fmt:message key="admin.header.ships"/></h2>
                <button class="btn btn-success mr-2" type="button" data-bs-toggle="modal" data-bs-target="#modalAddShipForm" id="addShip" aria-expanded="false" aria-controls="collapseExample"><fmt:message key="admin.button.addShip"/></button>
            </div>
            <div class="admin-container">
                <table class="table table-bordered" id="myTable">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th><fmt:message key="admin.tableHead.name"/></th>
                            <th><fmt:message key="admin.tableHead.passengerAmount"/></th>
                        </tr>
                    </thead>
                    <c:forEach items="${ships}" var="ships" varStatus="shipsIndex">
                        <tr class="admin-row">
                            <td>${ships.id}</td>
                            <td>${ships.name}</td>
                            <td>${ships.passengerAmount}</td>
                            <td>
                                <form method="post" action="ships">
                                    <input type="hidden" name="shipId" value="${ships.id}">
                                    <button type="submit" class="btn btn-danger me-2" name="btn" value="confirm"><fmt:message key="admin.button.delete"/></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div class="navigation">
                <form method="get" action="ships">
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
                                <a class="page-link" href="ships?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}&status=${status}"><fmt:message key="admin.nav.previous"/></a>
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
                                        <a class="page-link" href="ships?recordsPerPage=${recordsPerPage}&currentPage=${i}&status=${status}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage lt noOfPages}">
                            <li class="page-item">
                                <a class="page-link" href="ships?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}&status=${status}"><fmt:message key="admin.nav.next"/></a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </section>

    <div class="modal fade" id="modalAddShipForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold"><fmt:message key="admin.addShip.header"/></h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body mx-3">
                    <form name="form" method="post" action="addShip" onsubmit="return validatePassengers()">
                        <div class="mb-3">
                            <label for="name" class="form-label"><fmt:message key="admin.addShip.shipName"/></label>
                            <input type="text" class="form-control" id="name" placeholder='<fmt:message key="admin.addShip.shipName"/>' name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="passengerAmount" class="form-label"><fmt:message key="admin.addShip.passengerAmount"/></label>
                            <input type="text" name="passengerAmount" class="form-control" id="passengerAmount" placeholder='<fmt:message key="admin.addShip.passengerAmount"/>' required>
                        </div>
                        <button type="submit" class="btn btn-primary"><fmt:message key="admin.button.submit"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function validatePassengers() {
            var balance = document.forms["form"]["passengerAmount"].value;
            if (isNaN(balance)) {
                alert("Passenger Amount must be a number more than 0");
                return false;
            } else if (balance <= 0) {
                alert("Passenger Amount must be a number more than 0");
                return false;
            }
        }
    </script>

    <script src="../bootstrap/js/bootstrap.bundle.js"></script>

</body>
</html>

