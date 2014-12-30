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
</head>
<body>
	<header>M2CAT</header>
	<form method="post">
		<input type="text" name="search" /> <input type="submit" name="submit" value="search"/>
	</form>
	
	
	<t:results docs="${docs}" base="${base}">
	
	</t:results>
	
	<footer>
		built by <a href="http://sems.uni-rostock.de/" title="Simulation Experiment Management for Systems Biology">SEMS</a> @ <a href="http://www.uni-rostock.de/" title="University of Rostock">University of Rostock</a>
		&nbsp;|&nbsp;<a id="about-footer-link" href="#" title="About page">About</a>
	</footer>
</body>
</html>
