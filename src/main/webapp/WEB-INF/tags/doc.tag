<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="doc" required="false" type="de.unirostock.sems.m2cat.GraphModelDocument" %>
<%@ attribute name="base" required="false" type="java.lang.String" %>

<h3>${doc.fileName}</h3>

Following resources available:
<ul>
	<c:forEach items="${doc.resources}" var="res" >
		<li><t:res res="${res}">
		</t:res></li>
	</c:forEach>
</ul>
<a href="${base}/file/archive/${doc.docId}">download CombineArchive</a>
(<a href="http://webcat.sems.uni-rostock.de/rest/import?remote=${base}/file/archive/${doc.docId}&name=${doc.fileName}">open in CAT</a>)
