<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" %>
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
                <li><a href="orders" class="nav-link px-2 link-dark link-act"><fmt:message key="admin.header.orders"/></a></li>
                <li><a href="cruises" class="nav-link px-2 link-dark"><fmt:message key="admin.header.cruises"/></a></li>
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

    <section class="section-orders">
        <div class="container">
            <h2 class="section-header"><fmt:message key="admin.header.orders"/></h2>
            <form method="get" action="orders">
                <input type="hidden" name="command" value="status">
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <button type="submit" class="nav-link RESERVED"  name="status" value="RESERVED"><fmt:message key="admin.status.reserved"/></button>
                    </li>
                    <li class="nav-item">
                        <button type="submit" class="nav-link CONFIRMED"  name="status" value="CONFIRMED"><fmt:message key="admin.status.confirmed"/></button>
                    </li>
                    <li class="nav-item">
                        <button type="submit" class="nav-link PAID"  name="status" value="PAID"><fmt:message key="admin.status.paid"/></button>
                    </li>
                    <li class="nav-item">
                        <button type="submit" class="nav-link COMPLETED"  name="status" value="COMPLETED"><fmt:message key="admin.status.completed"/></button>
                    </li>
                    <li class="nav-item">
                         <button type="submit" class="nav-link DECLINED"  name="status" value="DECLINED"><fmt:message key="admin.status.declined"/></button>
                    </li>
                </ul>
            </form>

            <div class="admin-container">
                <table class="table table-bordered" id="myTable">
                <thead>
                    <tr>
                        <th><fmt:message key="admin.tableHead.orderID"/></th>
                        <th><fmt:message key="admin.tableHead.userName"/></th>
                        <th><fmt:message key="admin.tableHead.userLastName"/></th>
                        <th><fmt:message key="admin.tableHead.passengers"/></th>
                        <th><fmt:message key="admin.tableHead.cruiseName"/></th>
                        <th><fmt:message key="admin.tableHead.shipName"/></th>
                        <th><fmt:message key="admin.tableHead.dates"/></th>
                        <th><fmt:message key="admin.tableHead.price"/></th>
                        <th><fmt:message key="admin.tableHead.docs"/></th>
                    </tr>
                </thead>
                    <c:forEach items="${orders}" var="amount" varStatus="generalIndex">
                        <c:set var="index" value="${generalIndex.index}"/>
                        <c:forEach items="${orders}" var="orders" varStatus="ordersIndex">
                            <c:if test="${ordersIndex.index == index}">
                                <c:set var="orderId" value="${orders.id}" scope="request" />
                                <c:set var="orderPassengers" value="${orders.numOfPassengers}" scope="request" />
                                <c:set var="orderPrice" value="${orders.totalPrice}" scope="request" />
                                <c:set var="userDocs" value="${orders.userDocs}" scope="request" />
                                <c:set var="status" value="${orders.status}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${ships}" var="ships" varStatus="shipsIndex">
                            <c:if test="${shipsIndex.index == index}">
                                <c:set var="shipName" value="${ships.name}" scope="request" />
                                <c:set var="shipPassengers" value="${ships.passengerAmount}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${users}" var="users" varStatus="usersIndex">
                            <c:if test="${usersIndex.index == index}">
                                <c:set var="userName" value="${users.name}" scope="request" />
                                 <c:set var="userLastName" value="${users.lastName}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${cruises}" var="cruises" varStatus="cruisesIndex">
                            <c:if test="${cruisesIndex.index == index}">
                                <tr class="admin-row ${requestScope.status}">
                                    <td>${requestScope.orderId}</td>
                                    <td>${requestScope.userName}</td>
                                    <td>${requestScope.userLastName}</td>
                                    <td>${cruises.placesReserved}/${requestScope.shipPassengers} +(${requestScope.orderPassengers})</td>
                                    <td>${cruises.name}</td>
                                    <td>${requestScope.shipName}</td>
                                    <td>${cruises.dayStart} - ${cruises.dayEnd}</td>
                                    <td>${requestScope.orderPrice}</td>
                                    <c:if test="${status == 'RESERVED'}">
                                        <td>
                                             <form method="post" action="orders">
                                                <input type="hidden" name="orderId" value="${requestScope.orderId}">
                                                <button type="submit" class="btn btn-primary me-2" name="button" value="confirm"><fmt:message key="admin.button.confirm"/></button>
                                                <button type="submit" class="btn btn-primary" name="button" value="decline"><fmt:message key="admin.button.decline"/></button>
                                             </form>
                                        </td>
                                    </c:if>
                                    <c:if test="${not empty requestScope.userDocs}">
                                        <td>
                                             <td><a href="../files/${requestScope.userDocs}" download>Download Documents</a></td>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </table>
            </div>

            <div class="navigation">
                <form method="get" action="orders">
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
                                <a class="page-link" href="orders?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}&status=${status}"><fmt:message key="admin.nav.previous"/></a>
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
                                        <a class="page-link" href="orders?recordsPerPage=${recordsPerPage}&currentPage=${i}&status=${status}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage lt noOfPages}">
                            <li class="page-item">
                                <a class="page-link" href="orders?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}&status=${status}"><fmt:message key="admin.nav.next"/></a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </section>

    <script>
        var status = '<%=request.getAttribute("status")%>';
        var current = document.getElementsByClassName(status);
        current[0].className = current[0].className += " active";
    </script>

    <script src="../bootstrap/js/bootstrap.bundle.js"></script>

</body>
</html>

