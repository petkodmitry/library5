<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head><title>Успешная регистрация</title>
</head>
<body><H3>Регистрация нового пользователя прошла успешно!</H3>
<HR>
<a href="controller?cmd=register">Зарегистрировать еще одного пользователя</a><BR><BR><BR>
<a href="controller?cmd=login">На главную</a><BR>
<c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>
</body>
</html>
