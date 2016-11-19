<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head><title>Новый семинар</title>
</head>
<body><H3>Форма для добавления нового семинара:</H3>
<HR>
<FORM name="addSeminarForm"
      method="POST"
      action="controller">
    <table border="0">
        <tr>
            <td align="right">Тема семинара:</td>
            <td><INPUT type="text"
                       name="newSubject"
                       title="Введите тему семинара"
                       value="${regData.getSubject()}"></td>
        </tr>
        <tr>
            <td align="right">Дата семинара:</td>
            <td><INPUT type="date"
                       name="newDate"
                       title="Введите дату семинара"
                       value="${regData.getSeminarDate()}"></td>
        </tr>
        <tr style="height: 25px">
            <td>
            </td>
            <td align="right"><INPUT type="submit" formaction="controller?cmd=addSeminar" title="Добавить новый семинар" value="Добавить"></td>
        </tr>
    </table>
    <br>
</FORM>
<BR><a href="controller?cmd=adminSeminars">Список семинаров</a><BR>
<BR><a href="controller?cmd=login">На главную</a><BR>
<HR>
<c:if test="${requestScope['info'] != null}">
    ${info}<BR>
</c:if>
<c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>
</body>
</html>
