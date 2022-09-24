<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.params.ReplenishParams" %>
<%@ page import="com.provider.constants.Paths" %>

<%@ taglib prefix="pro" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Replenish account</title>
</head>
<body>
<pro:header/>
<form method="post" action="${pageContext.request.contextPath}/${Paths.REPLENISH}"
      class="row g-3 w-50 my-5 p-sm-2 mx-auto border border-0 shadow rounded rounded-3">
    <div class="col-md-1">
        <label for="amountInputId" class="col-form-label">${requestScope[ReplenishParams.CURRENCY]}</label>
    </div>
    <div class="col">
        <input type="number" step="0.01" name="${ReplenishParams.AMOUNT}" class="form-control" id="amountInputId"
               placeholder="Amount">
    </div>
    <div class="col-md-3">
        <button type="submit" class="btn btn-success mb-3 w-100">Replenish</button>
    </div>
    <input type="number" name="${ReplenishParams.ACCOUNT_ID}" value="${param[ReplenishParams.ACCOUNT_ID]}"
           readonly hidden aria-label="">
</form>
</body>
</html>

