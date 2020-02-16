<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<style>
    .green{background-color: green}
    .red{background-color: red}
</style>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <c:forEach var="meal" items="${list}">
        <tr class="${meal.isExcess() ? 'red':'green'}">
            <td><c:out value="${meal.getDescription()}"/></td>
            <td><c:out value="${meal.getCalories()}"/></td>
            <fmt:parseDate value="${ meal.getDateTime() }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>