<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.sql.Time" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page import="java.time.LocalDateTime" %><%--
  Created by IntelliJ IDEA.
  User: vitovt
  Date: 09.07.2020
  Time: 19:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dt>DateTime:</dt>
        <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
    </dl>
    <dl>
        <dt>Description:</dt>
        <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
    </dl>
    <dl>
        <dt>Calories:</dt>
        <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
    </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</body>
</html>
