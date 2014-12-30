<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="res" required="false" type="de.unirostock.sems.m2cat.meth.Resource" %>

<strong>${res.name}</strong> (${res.type})


