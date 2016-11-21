<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Мои книги</title>
</head>
<body><H3>Список книг у меня на руках</H3>
<HR>

<c:if test="${myBooksList != null && !myBooksList.isEmpty()}">
    <form>
        <table border="1">
            <tr>
                <td>ID книги</td>
                <td>Наименование</td>
                <td>Автор</td>
                <td>Место выдачи</td>
                <td>Дата заказа</td>
                <td>Дата возврата</td>
                <td>Продление на новый срок</td>
            </tr>
            <c:forEach items="${myBooksList}" var="order">
                <tr>
                    <td>${order.getBookId()}</td>
                    <td>${order.getTitle()}</td>
                    <td>${order.getAuthor()}</td>
                    <td>${order.getPlace().toString()}</td>
                    <td>${order.getStartDate()}</td>
                    <td>${order.getEndDate()}</td>
                    <td>
                        <c:if test="${order.getPlace().toString().equals('Абонемент')}">
                            <a href="controller?cmd=prolongOrder&orderId=${order.getOrderId()}">Продлить</a>
                        </c:if>
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
