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
<body><H3>Управление семинарами</H3>
<HR>

<BR><a href="controller?cmd=addSeminar">Добавить семинар</a>
<BR><BR>

<c:if test="${allSeminars != null && !allSeminars.isEmpty()}">
    <form>
        <table border="1">
            <tr>
                <td>Тема семинара</td>
                <td>Дата прохождения</td>
                <td>Участники семинара</td>
                <td>Удалить семинар</td>
            </tr>
            <c:forEach items="${allSeminars}" var="seminar">
                <tr>
                    <td>${seminar.getSubject()}</td>
                    <td>${seminar.getSeminarDate()}</td>
                    <td>
                        <a href="controller?cmd=usersOfSeminar&seminarId=${seminar.getSeminarId()}">Участники</a>
                    </td>
                    <td>
                        <a href="controller?cmd=deleteSeminar&seminarId=${seminar.getSeminarId()}">Удалить</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
</c:if>

<BR><a href="controller?cmd=login">На главную</a>
<c:if test="${requestScope['info'] != null}">
    <BR><BR>${info}<BR>
</c:if>

<c:if test="${seminarEntity != null}">
    <form>
        <table border="0">
            <BR>
            <tr>Участники семинара "${seminarEntity.getSubject()}" (ID ${seminarEntity.getSeminarId()}):</tr>
            <BR><BR>
            <c:if test="${seminarEntity.getUsers().isEmpty()}">
                НЕ ЗАПИСАН НИ ОДИН УЧАСТНИК.
            </c:if>
            <c:forEach items="${seminarEntity.getUsers()}" var="users">
                <tr>
                    ${users.getFirstName()}   ${users.getLastName()};<BR>
                </tr>
            </c:forEach>
        </table>
    </form>
</c:if>

<BR><BR><c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>
</body>
</html>
