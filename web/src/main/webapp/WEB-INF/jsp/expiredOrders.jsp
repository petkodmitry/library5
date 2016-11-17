<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Заявки</title>
</head>
<body><H3>Просроченные заказы</H3>
<HR>

<c:if test="${expiredOrdersList != null && !expiredOrdersList.isEmpty()}">
<form>
    <table border="1">
            <tr>
                <td>Пользователь</td>
                <td>ID книги</td>
                <td>Наименование</td>
                <td>Автор</td>
                <td>Место выдачи</td>
                <td>Дата заказа</td>
                <td>Дата возврата</td>
                <td>Просрочка, дней</td>
                <td>Заблокирован</td>
                <td>Заблокировать</td>
            </tr>
        <c:forEach items="${expiredOrdersList}" var="order">
            <tr>
                <td>${order.getLogin()}</td>
                <td>${order.getBookId()}</td>
                <td>${order.getTitle()}</td>
                <td>${order.getAuthor()}</td>
                <td>${order.getPlace().toString()}</td>
                <td>${order.getStartDate()}</td>
                <td>${order.getEndDate()}</td>
                <td>${order.getDelayDays()}</td>
                <td>${order.isBlocked() ? "да" : "НЕТ"}</td>
                <td>
                    <a href="controller?cmd=blockUser&login=${order.getLogin()}">Заблокировать</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</form>
</c:if>

<BR><a href="controller?cmd=login">На главную</a>
<BR><BR><c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>

</body>
</html>
