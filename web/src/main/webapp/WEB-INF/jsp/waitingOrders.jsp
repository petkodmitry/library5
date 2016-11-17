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
<body><H3>Заказы, ожидающие выдачи</H3>
<HR>

<c:if test="${waitingOrdersList != null && !waitingOrdersList.isEmpty()}">
<form>
    <table border="1">
            <tr>
                <td>Пользователь</td>
                <td>ID книги</td>
                <td>Наименование</td>
                <td>Автор</td>
                <td>Место выдачи</td>
                <td>Дата заказа</td>
                <%--<td>Дата возврата</td>--%>
                <td>Выдача читателю</td>
                <td>Закрыть заказ</td>
            </tr>
        <c:forEach items="${waitingOrdersList}" var="order">
            <tr>
                <td>${order.getLogin()}</td>
                <td>${order.getBookId()}</td>
                <td>${order.getTitle()}</td>
                <td>${order.getAuthor()}</td>
                <td>${order.getPlace().toString()}</td>
                <td>${order.getStartDate()}</td>
                <%--<td>${order.getEndDate()}</td>--%>
                <td>
                    <a href="controller?cmd=provideBook&orderId=${order.getOrderId()}">Выдать</a>
                </td>
                <td>
                    <a href="controller?cmd=closeOrder&orderId=${order.getOrderId()}">Закрыть</a>
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
