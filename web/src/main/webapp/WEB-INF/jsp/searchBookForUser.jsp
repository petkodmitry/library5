<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Поиск книг</title>
</head>
<body><H3>Поиск книги для заказа</H3>
<HR>
<form method="post" action="controller">
    <INPUT type="hidden" name="cmd" value="searchbook">
    <table>
        <tr>
            <td><input name="searchTextInBook" title="Введите слово или часть слова для поиска книги" type="text"
                       value=""></td>
            <td>
                <INPUT type="submit" title="Поиск по названию или автору" value="Найти">
            </td>
        </tr>
    </table>
</form>
<BR><BR>

<c:if test="${searchBookForUser != null && !searchBookForUser.isEmpty()}">
    <form>
        <table border="1">
            <tr>
                <td>Наименование</td>
                <td>Автор</td>
                <td>Наличие</td>
                <td>Заказ на абонемент</td>
                <td>Заказ в ЧЗ</td>
            </tr>
            <c:forEach items="${searchBookForUser}" var="book">
                <tr>
                    <td>${book.getTitle()}</td>
                    <td>${book.getAuthor()}</td>
                    <td>
                        <c:if test="${book.getIsBusy() == false}">Available</c:if>
                        <c:if test="${book.getIsBusy() == true}">N/A</c:if>
                    </td>
                    <td>
                        <c:if test="${book.getIsBusy() == false}">
                            <a href="controller?cmd=orderToHome&bookId=${book.getBookId()}">Заказать</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${book.getIsBusy() == false}">
                            <a href="controller?cmd=orderToReadingRoom&bookId=${book.getBookId()}">Заказать</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
</c:if>

<BR><a href="controller?cmd=myorders">Мои заказы в очереди</a>
<BR><a href="controller?cmd=login">На главную</a>
<BR><BR><c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>
</body>
</html>
