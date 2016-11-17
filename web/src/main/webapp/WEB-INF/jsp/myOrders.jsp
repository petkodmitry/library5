<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Мои заказы</title>
</head>
<body><H3>Список моих необработанных заказов</H3>
<HR>

<c:if test="${myOrdersList != null && !myOrdersList.isEmpty()}">
    <form>
        <table border="1">
            <tr>
                <td>ID книги</td>
                <td>Наименование</td>
                <td>Автор</td>
                <td>Место</td>
                <td>Дата заказа</td>
                <td></td>
            </tr>
            <c:forEach items="${myOrdersList}" var="order">
                <tr>
                    <td>${order.getBookId()}</td>
                    <td>${order.getTitle()}</td>
                    <td>${order.getAuthor()}</td>
                    <td>${order.getPlace().toString()}</td>
                    <td>${order.getStartDate()}</td>
                    <td>
                        <a href="controller?cmd=cancelUserOrder&orderId=${order.getOrderId()}">Отменить заказ</a>
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
