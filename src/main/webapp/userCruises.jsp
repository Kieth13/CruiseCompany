<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources" />

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cruise Company</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="styles/styles.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.1/css/flag-icon.min.css">
</head>
<body>

    <div class="container">
        <header class="header py-3 mb-4">
            <div>
                <span class="fs-4"><fmt:message key="header.user"/>, ${user.name}. </span>
                <span class="fs-4"><fmt:message key="header.balance"/>: ${user.balance}$</span>
            </div>
            <div class="col-md-3 text-end">
                <a href="${pageContext.request.contextPath}" class="btn btn-outline-primary me-2"><fmt:message key="header.allCruises"/></a>
                <a href="logout" class="btn btn-outline-primary me-2"><fmt:message key="header.logout"/></a>
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

    <section class="section_cruises">
        <div class="container">
            <h2 class="section-header cruises-header"><fmt:message key="section.header"/></h2>

            <c:if test="${not empty notEnoughMoney}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="section.notEnoughMoney"/>
                </div>
            </c:if>
            <c:remove var="notEnoughMoney"/>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="section.errorUploading"/>
                </div>
            </c:if>
            <c:remove var="errorMsg"/>

            <div class="row">
                <c:forEach items="${cruises}" var="amount" varStatus="generalIndex">
                    <c:set var="index" value="${generalIndex.index}" />
                    <div class="card col-3 my-card">
                        <img src="img/available.jpg" class="card-image">
                        <c:forEach items="${ships}" var="ships" varStatus="shipIndex">
                            <c:if test="${shipIndex.index == index}">
                                <c:set var="shipName" value="${ships.name}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${orders}" var="orders" varStatus="orderIndex">
                            <c:if test="${orderIndex.index == index}">
                                <c:set var="numOfPassengers" value="${orders.numOfPassengers}" scope="request" />
                                <c:set var="totalPrice" value="${orders.totalPrice}" scope="request" />
                                <c:set var="status" value="${orders.status}" scope="request" />
                            </c:if>
                        </c:forEach>
                        <c:forEach items="${cruises}" var="cruises" varStatus="cruisesIndex">
                            <c:if test="${cruisesIndex.index == index}">
                                <div class="card-body">
                                    <h5 class="card-title">${cruises.name}</h5>
                                    <h6 class="card-subtitle mb-2"><fmt:message key="card.ship"/>: "${requestScope.shipName}"</h6>
                                    <p class="card-text">${cruises.routeFrom} > ${cruises.numOfPorts} <fmt:message key="card.ports"/> > ${cruises.routeTo}</p>
                                    <p class="card-text">${cruises.dayStart} - ${cruises.dayEnd}</p>
                                    <p class="card-text"><fmt:message key="card.totalPrice"/>: ${requestScope.totalPrice}$ (<fmt:message key="card.for"/> ${numOfPassengers} <fmt:message key="card.people"/>)</p>
                                    <h6 class="card-subtitle mb-2"> <fmt:message key="card.status"/>: ${status}</h6>
                                    <form method="post" action="userCruises" enctype="multipart/form-data">
                                        <input type="hidden" name="cruiseId" value="${cruises.id}">
                                        <button type="submit" class="btn btn-primary me-3" name="button" value="cancel"><fmt:message key="card.cancel"/></button>
                                        <c:if test="${status == 'CONFIRMED'}">
                                            <button type="submit" class="btn btn-primary" name="button" value="pay"><fmt:message key="card.pay"/></button>
                                        </c:if>
                                        <c:if test="${status == 'PAID'}">
                                            <input type="file" name="file" />
                                            <button type="submit" class="btn btn-primary" name="button" value="upload"><fmt:message key="card.upload"/></button>
                                        </c:if>
                                    </form>

                                    <c:if test="${not empty uploadedFile}">
                                        <hr>
                                        <a href="files/${uploadedFile}" download>Download File</a>
                                    </c:if>

                                    <c:remove var="uploadedFile" />



                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <script src="bootstrap/js/bootstrap.bundle.js"></script>

</body>
</html>
