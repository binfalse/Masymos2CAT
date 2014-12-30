<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="docs" required="false" type="java.util.List" %>
<%@ attribute name="base" required="false" type="java.lang.String" %>
<%@ attribute name="user" required="false" type="de.unirostock.sems.m2cat.web.User" %>

<h2>Search Results</h2>
<small>(search results are currently showing at most 20 hits.)</small>
<c:forEach items="${docs}" var="doc" >
	<t:doc doc="${doc}" base="${base}" user="${user}">
	
	</t:doc>
</c:forEach>