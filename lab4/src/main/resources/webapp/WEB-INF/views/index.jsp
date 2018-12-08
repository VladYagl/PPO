<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    <title>${list.name}</title>
</head>
<body>

<h1>${list.name}</h1>

<table>
    <%--<form:form modelAttribute="tasks" action="/tmp" method="POST">--%>

    <c:forEach var="task" items="${tasks}">
        <tr>
            <td>${task.getId()}</td>
            <td>${task.getName()}</td>
            <td>
                <input type="checkbox" onclick="location.href='/check-task?id=${task.getId()}'"
                        <c:choose>
                            <c:when test="${task.getCompleted()}">
                                checked
                            </c:when>
                        </c:choose>
                />
            </td>
        </tr>
    </c:forEach>

    <%--</form:form>--%>
</table>

<h3>Add new tasks</h3>
<form:form modelAttribute="newTask" method="POST" action="/add-task?list_id=${list.id}">
    <table>
        <tr>
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name"/></td>
            <td><input type="submit" value="add"></td>
        </tr>
    </table>

</form:form>

<br>
<br>

<input type="button" value="Back" onclick="location.href='/'"/>

</body>
</html>