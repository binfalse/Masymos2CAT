<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<!DOCTYPE html>
<!-- M2CAT -- an interface to bundle search results from masymos to
 combine archives and send them to the CombineArchiveWeb interface -->
 
<!-- Copyright (C) 2014  SEMS Group

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. -->
<html lang="en">
<head>
	<title>M2CAT</title>
	<link rel="stylesheet" href="res/style.css" type="text/css" />
	<script src="res/jquery-2.1.3.min.js"></script>
</head>
<body>
	<header>
		<h1>M2CAT &mdash; From Masymos To CAT</h1>
	</header>
	<div id="meta">
		<form method="post">
			<strong>meta data about you</strong><br/>
			First Name: <input type="text" name="firstname" value="${user.firstName}"/> <br/>
			Last Name: <input type="text" name="lastname" value="${user.lastName}"/> <br/>
			Mail: <input type="text" name="mail" value="${user.mail}"/> <br/>
			Organization: <input type="text" name="org" value="${user.organization}" /> <br/>
			<input type="submit" value="save" name="savemyinfo" />
			<c:choose>
				<c:when test="${user.valid}">
					<small>(valid)</small>
				</c:when>
				<c:otherwise>
					<small>(invalid)</small>
				</c:otherwise>
			</c:choose>
		</form>
	</div>
	
	<div id="abstract">
		<a href="https://sems.uni-rostock.de/projects/m2cat/">M2CAT</a> is a web based tool to export reproducible research results.
		It links the graph based database <a href="https://sems.uni-rostock.de/projects/masymos/">MASYMOS</a> to the <a href="https://sems.uni-rostock.de/projects/combinearchive/">CombineArchiveToolkit</a> (CAT).
	</div>
	
	<div id="search">
		<form method="post">
			Search for models and resources in the database:<br/>
			<input type="text" name="search" /> <input type="submit" name="submit" value="search"/>
		</form>
	</div>
	
	<c:if test="${dbErr}">
		<div id="dbErr">
			Apparently, we ran into an error with the DB connection!? Is the database running?
		</div>
	</c:if>
	
	<div id="results">
		<t:results docs="${docs}" base="${base}" user="${user}" searchTerm="${searchTerm}">
		
		</t:results>
	</div>
	
	<footer>
		built by <a href="http://sems.uni-rostock.de/" title="Simulation Experiment Management for Systems Biology">SEMS</a> @ <a href="http://www.uni-rostock.de/" title="University of Rostock">University of Rostock</a>
		&nbsp;
	</footer>
</body>
</html>
