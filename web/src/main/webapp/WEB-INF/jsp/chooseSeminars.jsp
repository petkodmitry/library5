<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head>
    <title>Семинары</title>
</head>
<body><H3>Доступные семинары</H3>
<HR>

<c:if test="${availableSeminars != null && !availableSeminars.isEmpty()}">
    <form>
        <table border="1">
            <tr>
                <td>Тема семинара</td>
                <td>Дата прохождения</td>
                <td>Записаться</td>
            </tr>
            <c:forEach items="${availableSeminars}" var="seminar">
                <tr>
                    <td>${seminar.getSubject()}</td>
                    <td>${seminar.getSeminarDate()}</td>
                    <td>
                        <a href="controller?cmd=subscribeToSeminar&seminarId=${seminar.getSeminarId()}">Записаться на семинар</a>
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
