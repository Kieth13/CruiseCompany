<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="resources"/>

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
            <c:if test="${not empty userName}">
                <div>
                    <span class="fs-4"><fmt:message key="header.user"/> ${userName}.</span>
                    <span class="fs-4"><fmt:message key="header.balance"/>: ${userBalance}$</span>
                </div>

                <div class="col-md-3 text-end">
                    <a href="userCruises" class="btn btn-outline-primary me-2"><fmt:message key="header.cruises"/></a>
                    <a href="logout" class="btn btn-outline-primary me-2"><fmt:message key="header.logout"/></a>
                    <a class="btn btn-outline-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        &#127760;
                    </a>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="?sessionLocale=en"><span class="flag-icon flag-icon-us"></span> <fmt:message key="lang.eng"/></a>
                        <a class="dropdown-item" href="?sessionLocale=ua"><span class="flag-icon flag-icon-ua"></span> <fmt:message key="lang.ukr"/></a>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty userName}">
                <div>
                    <span class="fs-4"><fmt:message key="header.title"/></span>
                </div>
                <div>
                    <a href="" class="btn btn-outline-primary me-2" data-bs-toggle="modal" data-bs-target="#modalLoginForm" id="login"><fmt:message key="header.login"/></a>
                    <a href="" class="btn btn-outline-primary me-2"  data-bs-toggle="modal" data-bs-target="#modalRegisterForm" id="register"><fmt:message key="header.register"/></a>
                    <a class="btn btn-outline-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        &#127760;
                    </a>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="?sessionLocale=en"><span class="flag-icon flag-icon-us"></span> <fmt:message key="lang.eng"/></a>
                        <a class="dropdown-item" href="?sessionLocale=ua"><span class="flag-icon flag-icon-ua"></span> <fmt:message key="lang.ukr"/></a>
                    </div>
                </div>

            </c:if>
        </header>
    </div>

    <section class="section_cruises">
        <div class="container">
            <h2 class="section-header cruises-header"><fmt:message key="main.section"/></h2>
            <div class="d-flex justify-content-start align-items-center mb-4">
                <button class="btn btn-primary me-2" type="button" data-bs-toggle="collapse" data-bs-target="#collapseDate" aria-expanded="false" aria-controls="collapseExample">
                    <fmt:message key="main.searchByDate"/>
                </button>
                <button class="btn btn-primary me-2" type="button" data-bs-toggle="collapse" data-bs-target="#collapseDays" aria-expanded="false" aria-controls="collapseExample">
                   <fmt:message key="main.searchByDays"/>
                </button>
                <c:if test="${not empty searched}">
                    <form method="post" action="" class="my-form">
                        <button class="btn btn-primary" type="submit" name="action" value="cancelSearch"><fmt:message key="main.canselSearch"/></button>
                    </form>
                </c:if>
            </div>


            <div class="collapse" id="collapseDays">
                <form method="post" action="">
                    <div class="input-group mb-3">
                        <span class="input-group-text"><fmt:message key="main.days"/></span>
                        <input type="number" class="number-input form-control" aria-label="Recipient's username with two button addons" aria-describedby="button-addon4" name="numOfDays" min="1" required>
                        <div class="input-group-append" id="button-addon4">
                            <button class="btn btn-primary" type="submit" name="action" value="searchByDays"><fmt:message key="main.search"/></button>
                        </div>
                    </div>
                </form>
            </div>

            <div class="collapse" id="collapseDate">
                <form method="post" action="">
                    <div class="input-group mb-3">
                        <span class="input-group-text"><fmt:message key="main.date"/></span>
                        <input type="date" class="date-input form-control" aria-label="Recipient's username with two button addons" aria-describedby="button-addon4" name="startDate" required>
                        <div class="input-group-append" id="button-addon4">
                            <button class="btn btn-primary" type="submit" name="action" value="searchByDate"><fmt:message key="main.search"/></button>
                        </div>
                    </div>
                </form>
            </div>

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
                        <c:forEach items="${cruises}" var="cruises" varStatus="cruisesIndex">
                            <c:if test="${cruisesIndex.index == index}">
                                <div class="card-body">
                                    <h5 class="card-title">${cruises.name}</h5>
                                    <h6 class="card-subtitle mb-2"><fmt:message key="card.ship"/>: "${requestScope.shipName}"</h6>
                                    <p class="card-text"><fmt:message key="card.price"/>: ${cruises.price}$/<fmt:message key="card.person"/></p>
                                    <p class="card-text"><fmt:message key="card.route"/>: ${cruises.routeFrom} > ${cruises.numOfPorts} <fmt:message key="card.ports"/> > ${cruises.routeTo}</p>
                                    <p class="card-text"><fmt:message key="card.dates"/>: ${cruises.dayStart} - ${cruises.dayEnd}</p>
                                    <form method="post" action="">
                                        <input type="hidden" name="cruiseId" value="${cruises.id}">
                                        <div class="input-group mb-3">
                                          <span class="input-group-text"><fmt:message key="card.passengers"/></span>
                                          <input type="number" class="number-input form-control" name="numOfPassengers" min="1" required>
                                        </div>
                                        <button type="submit" class="btn btn-primary" name="action" value="reserve"><fmt:message key="card.reserve"/></button>
                                    </form>
                                </div>
                            </c:if>
                         </c:forEach>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <div class="modal fade" id="modalLoginForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold"><fmt:message key="login.header"/></h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body mx-3">
                    <form method="post" action="login">
                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label"><fmt:message key="login.emailLabel"/></label>
                            <input type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder='<fmt:message key="login.emailPlaceholder"/>' name="loginEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword1" class="form-label"><fmt:message key="login.passwordLabel"/></label>
                            <input type="password" name="loginPassword" class="form-control" id="exampleInputPassword1" placeholder='<fmt:message key="login.passwordPlaceholder"/>' required>
                        </div>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" role="alert">
                                <fmt:message key="login.error"/>
                            </div>
                        </c:if>
                        <button type="submit" class="btn btn-primary"><fmt:message key="login.submit"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="modalRegisterForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold"><fmt:message key="registration.header"/></h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body mx-3">
                    <form method="post" action="registration">
                        <div class="mb-3">
                            <label for="exampleInputEmail" class="form-label"><fmt:message key="login.emailLabel"/></label>
                            <input placeholder='<fmt:message key="login.emailPlaceholder"/>' type="text" class="form-control" name="regEmail" id="exampleInputEmail" pattern="[a-z0-9._]+@[a-z]+\.[a-z]{2,3}" title='<fmt:message key="registration.emailTitle"/>' required>
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword" class="form-label"><fmt:message key="login.passwordLabel"/></label>
                            <input placeholder='<fmt:message key="login.passwordPlaceholder"/>' type="password" class="form-control" name="regPassword" id="exampleInputPassword" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title='<fmt:message key="registration.passwordTitle"/>' required>
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputName" class="form-label"><fmt:message key="registration.firstNameLabel"/></label>
                            <input placeholder='<fmt:message key="registration.firstNamePlaceholder"/>' type="text" class="form-control" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputLastName" class="form-label"><fmt:message key="registration.lastNameLabel"/></label>
                            <input placeholder='<fmt:message key="registration.lastNamePlaceholder"/>' type="text" class="form-control" name="lastName" required>
                        </div>
                        <c:if test="${not empty loginExists}">
                            <div class="alert alert-danger" role="alert">
                                <fmt:message key="registration.loginExists"/>
                            </div>
                        </c:if>
                        <button type="submit" class="btn btn-primary"><fmt:message key="registration.register"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        window.onload = function() {
            var test = '<%= session.getAttribute("errorMessage") %>';
            if (test != 'null') {
                var button = document.getElementById('login');
                button.click();
            }
            var test2 = '<%= session.getAttribute("loginExists") %>';
            console.log(test);
            if (test2 != 'null') {
                var button = document.getElementById('register');
                button.click();
            }
        };
    </script>

    <script src="bootstrap/js/bootstrap.bundle.js"></script>

</body>
</html>
