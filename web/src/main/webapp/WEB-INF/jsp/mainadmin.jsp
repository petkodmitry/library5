<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Главная страница</title>
</head>
<body>
<H3>Добро пожаловать, ${user}!</H3><HR>
Вы вошли в систему как АДМИНИСТРАТОР<BR><BR>
<a href="controller?cmd=login">На главную</a><BR>
<a href="controller?cmd=showUsers">Список пользователей</a><BR>
<a href="controller?cmd=register">Добавить пользователя</a><BR>
<a href="controller?cmd=waitingOrders">Ожидающие заказы</a><BR>
<a href="controller?cmd=openedOrders">Открытые заказы</a><BR>
<a href="controller?cmd=expiredOrders">Просроченные заказы</a><BR>
<a href="controller?cmd=blacklist">Черный список</a><BR>
<a href="controller?cmd=searchBookAdmin">Управление книгами</a><BR>
<BR><a href="controller?cmd=logout">Выход</a><BR>
<c:if test="${requestScope['errorMessage'] != null}">
    <BR>Ошибка: ${errorMessage}
</c:if>
</body>
</html>
