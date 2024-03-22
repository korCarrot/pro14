<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
    <meta charset="utf-8">
</head>
<body>

<fmt:setLocale value="ko_KR"/>
<%--<fmt:setLocale value="en_US"/>--%>

<h1>
회원정보<br><br>
<fmt:bundle basename="resource.member">
    이름:<fmt:message key="mem.name" /><br>
    주소:<fmt:message key="mem.address" /><br>
    직업:<fmt:message key="mem.job" />
    </fmt:bundle>
</h1>
</body>
</html>
