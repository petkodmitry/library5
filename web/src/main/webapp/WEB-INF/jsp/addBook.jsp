<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head><title>Новая книга</title>
</head>
<body><H3>Форма для добавления новой книги:</H3>
<HR>
<FORM name="addBookForm"
      method="POST"
      action="controller">
    <table border="0">
        <tr>
            <td align="right">Наименование:</td>
            <td><INPUT type="text"
                       name="newTitle"
                       title="Введите наименование добавляемой книги"
                       value="${regData.getTitle()}"></td>
        </tr>
        <tr>
            <td align="right">Автор:</td>
            <td><INPUT type="text"
                       name="newAuthor"
                       title="Введите автора добавляемой книги"
                       value="${regData.getAuthor()}"></td>
        </tr>
        <tr style="height: 25px">
            <td>
            </td>
            <td align="right"><INPUT type="submit" formaction="controller?cmd=addBook" title="Добавить новую книгу" value="Добавить"></td>
        </tr>
    </table>
    <br>
</FORM>
<BR><a href="controller?cmd=searchBookAdmin">Назад</a>
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
