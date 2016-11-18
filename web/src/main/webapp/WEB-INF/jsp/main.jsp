<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Главная страница</title>
</head>
<body>
<H3>Добро пожаловать, ${user}!</H3><HR>
Вы вошли в систему как обычный пользователь<BR><BR>
<a href="controller?cmd=login">На главную</a><BR>
<a href="controller?cmd=myBooks">Мои книги</a><BR>
<a href="controller?cmd=searchBook">Поиск и заказ книг</a><BR>
<a href="controller?cmd=myOrders">Мои заказы в очереди</a><BR>
<a href="controller?cmd=mySeminars">Мои семинары</a><BR>
<BR><a href="controller?cmd=logout">Выход</a><BR>
<c:if test="${requestScope['errorMessage'] != null}">
    <BR>Ошибка: ${errorMessage}
</c:if>
</body>
</html>
