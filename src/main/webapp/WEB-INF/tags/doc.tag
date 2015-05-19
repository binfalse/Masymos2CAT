<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="doc" required="false" type="de.unirostock.sems.m2cat.graph.GraphModelDocument" %>
<%@ attribute name="base" required="false" type="java.lang.String" %>
<%@ attribute name="user" required="false" type="de.unirostock.sems.m2cat.web.User" %>

<h3>${doc.fileName}</h3>
<div class="resources">
	Following resources available:
	<ul>
		<c:forEach items="${doc.resources}" var="res" >
			<li><t:res res="${res}">
			</t:res></li>
		</c:forEach>
	</ul>
	<c:choose>
		<c:when test="${user.valid}">
			<a href="${base}/file/archive/${doc.docId}/${user.urlUserInfo}/archive.omex">download CombineArchive</a> | 
			<!--  a href="http://webcat.sems.uni-rostock.de/rest/import?remote=${base}/file/archive/${doc.docId}/${user.urlUserInfo}/archive.omex&name=${doc.fileName}">open in CAT</a-->
			
			<c:choose>
				<c:when test="${doc.cellML}">
					<a href="${base}/webcat/${doc.docId}" id="${doc.docId}">open in CAT</a>
				</c:when>
				<c:otherwise>
					<a href="http://webcat.sems.uni-rostock.de/rest/import?remote=${base}/file/archive/${doc.docId}/${user.urlUserInfo}/archive.omex&name=${doc.fileName}">open in CAT</a>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<strong>To export the search results you need to provide some information about yourself at the top of the page.</strong>
		</c:otherwise>
	</c:choose>
</div>
