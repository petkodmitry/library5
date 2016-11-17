<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<html>
<head><title>Регистрация пользователя</title>
</head>
<body><H3>Заполните регистрационную форму:</H3>
<HR>
<FORM name="registerForm"
      method="POST"
      action="controller">
    <table border="0">
        <tr>
            <td align="right">Логин:</td>
            <td><INPUT type="text"
                       name="newLogin"
                       title="Придумайте оригинальный логин"
                       value="${regData.getLogin()}"></td>
            <td>
                <form method="post" action="controller">
                    <BUTTON type="submit" formaction="controller?cmd=register" title="Проверить доступность">Доступность</BUTTON>
                </form>
            </td>
        </tr>
        <tr style="height: 25px">
            <td></td>
            <td>${unavailableMessage}</td>
            <td></td>
        </tr>
        <tr>
            <td align="right">Имя:</td>
            <td><INPUT type="text"
                       name="newName"
                       title="Имя"
                       value="${regData.getFirstName()}"></td>
            <td></td>
        </tr>
        <tr>
            <td align="right">Фамилия:</td>
            <td><INPUT type="text"
                       name="newLastName"
                       title="Фамилия"
                       value="${regData.getLastName()}"></td>
            <td></td>
        </tr>
        <tr>
            <td align="right">Пароль:</td>
            <td><INPUT type="password"
                       name="newPassword"
                       title="Пароль не менее 8 символов"
                       value=""></td>
            <td></td>
        </tr>
        <tr>
            <td align="right">Повторите пароль:</td>
            <td><INPUT type="password"
                       name="repeatPassword"
                       title="Повторите ввод пароля для проверки"
                       value=""></td>
            <td></td>
        </tr>
        <tr style="height: 25px">
            <td>
                <form method="post" action="controller">
                    <BUTTON tabindex="-1" type="submit" formaction="controller?cmd=login" title="Назад">Назад</BUTTON>
                </form>
            </td>
            <td align="right"><INPUT type="submit" formaction="controller?cmd=register" title="Зарегистрироваться" value="Зарегистрироваться"></td>
            <td></td>
        </tr>
    </table>
    <br>
</FORM>
<HR>
<c:if test="${requestScope['errorMessage'] != null}">
    Ошибка: ${errorMessage}
</c:if>
</body>
</html>
