<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="c-1_0-rt.tld" %>
<%@ taglib prefix="form" uri="spring-form.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
    <title>Task lists</title>
</head>
<body>

<h1>Task lists:</h1>

<table>
    <%--<form:form modelAttribute="tasks" action="/tmp" method="POST">--%>

    <c:forEach var="list" items="${lists}">
        <tr>
            <td>${list.getId()}</td>
                <%--<td>${list.getName()}</td>--%>
            <td>
                <input type="button" value="${list.getName()}" style="width:100%"
                       onclick="location.href='/get-tasks?list=${list.getId()}'"/>
            </td>
            <td>
                <input type="button" value="❌	" style="border:none;"
                       onclick="location.href='/delete-list?id=${list.getId()}'"/>
            </td>
        </tr>
    </c:forEach>

    <%--</form:form>--%>
</table>

<h3>Add new list</h3>
<form:form modelAttribute="newList" method="POST" action="/add-list">
    <table>
        <tr>
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name"/></td>
        </tr>
    </table>

    <input type="submit" value="add">
</form:form>

</body>
</html>