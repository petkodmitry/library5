<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage="error.jsp" %>
<HTML>
<HEAD><TITLE>Список пользователей</TITLE></HEAD>
<BODY><H3>Таблица: пользователи</H3>
<HR>
<H4>Найдено записей: ${totalToShow} из ${total}</H4>

<c:choose>
    <c:when test="${requestScope.get('page') == null}">
        <c:set var="page" value="1"/>
        ${requestScope.putIfAbsent("page", "1")}
    </c:when>
    <c:otherwise>
        <c:set var="page" value="${requestScope.get('page')}"/>
    </c:otherwise>
</c:choose>

<form method="post" action="controller?cmd=showUsers&page=${page}">
    <table>
        <tr>
            <td>количество элементов на странице:</td>
            <td>
                <select title="perPageSelection" name="perPage">
                    <option value="2" ${max == 2 ? 'selected="selected"' : ''}>2</option>
                    <option value="5" ${max == 5 ? 'selected="selected"' : ''}>5</option>
                    <option value="10" ${max == 10 ? 'selected="selected"' : ''}>10</option>
                    <option value="20" ${max == 20 ? 'selected="selected"' : ''}>20</option>
                    <option value="50" ${max == 50 ? 'selected="selected"' : ''}>50</option>
                    <option value="100" ${max == 100 ? 'selected="selected"' : ''}>100</option>
                </select>
            </td>
            <td>
                <INPUT type="submit" title="OK" value="OK">
            </td>
        </tr>
    </table>
</form>

<table border="1" frame="void">
    <tbody>
    <%--Sorting row of the table--%>
    <tr>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=userId&orderType=${"userId".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">ID</a>
            <c:if test="${'asc'.equals(orderType) && 'userId'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'userId'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=firstName&orderType=${"firstName".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Имя</a>
            <c:if test="${'asc'.equals(orderType) && 'firstName'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'firstName'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=lastName&orderType=${"lastName".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Фамилия</a>
            <c:if test="${'asc'.equals(orderType) && 'lastName'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'lastName'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=login&orderType=${"login".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Логин</a>
            <c:if test="${'asc'.equals(orderType) && 'login'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'login'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=password&orderType=${"password".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Пароль</a>
            <c:if test="${'asc'.equals(orderType) && 'password'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'password'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=isAdmin&orderType=${"isAdmin".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Является админом</a>
            <c:if test="${'asc'.equals(orderType) && 'isAdmin'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'isAdmin'.equals(sortBy)}">↑</c:if>
        </td>
        <td><a href="controller?cmd=showUsers&page=1&sortBy=isBlocked&orderType=${"isBlocked".equals(sortBy) ?
                    ("asc".equals(orderType) ? "desc" : "asc") : "asc"}">Заблокирован</a>
            <c:if test="${'asc'.equals(orderType) && 'isBlocked'.equals(sortBy)}">↓</c:if>
            <c:if test="${'desc'.equals(orderType) && 'isBlocked'.equals(sortBy)}">↑</c:if>
        </td>
    </tr>
    <%--Filtering row of the table--%>
    <tr>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="userIdFilter"
                       value="${sessionScope.get("filters").get("userId")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("userId") == null ? page : 1}&filterRemove=userId">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="firstNameFilter"
                       value="${sessionScope.get("filters").get("firstName")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("firstName") == null ? page : 1}&filterRemove=firstName">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="lastNameFilter"
                       value="${sessionScope.get("filters").get("lastName")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("lastName") == null ? page : 1}&filterRemove=lastName">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="loginFilter"
                       value="${sessionScope.get("filters").get("login")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("login") == null ? page : 1}&filterRemove=login">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="passwordFilter"
                       value="${sessionScope.get("filters").get("password")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("password") == null ? page : 1}&filterRemove=password">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="isAdminFilter"
                       value="${sessionScope.get("filters").get("isAdmin")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("isAdmin") == null ? page : 1}&filterRemove=isAdmin">X</a>
            </form>
        </td>
        <td>
            <form style="height: 5px" method="POST" action="controller">
                <input style="width: 50px" type="text" title="установить фильтр" name="isBlockedFilter"
                       value="${sessionScope.get("filters").get("isBlocked")}"/>
                <input type="submit" formaction="controller?cmd=showUsers&page=1" hidden="hidden"/>
                <a href="controller?cmd=showUsers&page=${sessionScope.get("filters").get("isBlocked") == null ? page : 1}&filterRemove=isBlocked">X</a>
            </form>
        </td>
    </tr>
    <c:forEach items="${userSet}" var="user">
        <tr>
            <td><c:out value="${user.getUserId()}"/></td>
            <td><c:out value="${user.getFirstName()}"/></td>
            <td><c:out value="${user.getLastName()}"/></td>
            <td><c:out value="${user.getLogin()}"/></td>
            <td><c:out value="${user.getPassword()}"/></td>
            <td><c:out value="${user.getIsAdmin()}"/></td>
            <td><c:out value="${user.getIsBlocked()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<BR>

<c:set var="aMod" value="${totalToShow mod max}"/>
<c:choose>
    <c:when test="${aMod == 0}">
        <c:set var="endPage" value="${(totalToShow / max)}"/>
    </c:when>
    <c:otherwise>
        <c:set var="endPage" value="${((totalToShow - aMod) / max) + 1}"/>
    </c:otherwise>
</c:choose>

<c:if test="${endPage != 1}">
    <c:forEach begin="${1}" end="${endPage}" var="page1">
        <c:choose>
            <c:when test="${(endPage <= 10) || ((page1 == 1) || (page1 == endPage)
            || ((page + 3) > page1 && (page - 3) < page1))}">
                <c:if test="${page1 < 10}">
                    <c:set var="page2" value="${'0'.concat(page1)}"/>
                </c:if>
                <c:if test="${page1 > 9}">
                    <c:set var="page2" value="${page1}"/>
                </c:if>
                <c:choose>
                    <c:when test="${page == page1}">
                        &nbsp;&nbsp;&nbsp;&nbsp;${page2}
                    </c:when>
                    <c:otherwise>
                        &nbsp;&nbsp;&nbsp;<a href="controller?cmd=showUsers&page=${page1}"> ${page2} </a>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${(page1 == 2 && (page - 3) > 1) || ((page1 == endPage - 1) && (page + 3) < endPage)}">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</c:if>

<HR>
<BR><a href="controller?cmd=login">На главную</a><BR>
<c:if test="${requestScope['errorMessage'] != null}">
    <BR>Ошибка: ${errorMessage}
</c:if>
</BODY>
</HTML>
