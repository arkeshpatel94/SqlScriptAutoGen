<%@ page contentType="text/html; charset=utf-8" %>
<%!
        public static final String _ARJSP_JSP_NAME = "../../finbranch/custom/jsp/customReportPrint.jsp";
%>

<%@ include file="../../finbranch_common.jsp" %>
<%
String sGroupName  = request.getParameter("groupName");
String ctrlJspName  = request.getParameter("ctrlJspName");
%>
<arjsp:init groupName="<%=sGroupName%>" isEntryPoint="false" />

<%
        String _ARJSP_TITLE_NAME = "";
         _ARJSP_TITLE_NAME =ResourceManager.getString(pageContext,"finbranch","FLT000041");
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title><%=_ARJSP_TITLE_NAME%></title>
<LINK href="../Renderer/stylesheets/services.css" rel=STYLESHEET  title="Finacle Stylesheet" type=text/css />
</head>

<%
        String qryStr  = request.getQueryString();
%>

<script>
var qry = '<%=qryStr%>';
</script>

<frameset rows="100%,0%">
        <frame hidden name="fetch" src="../../<%=sGroupName%>/<%=ctrlJspName%>?actionCode=PRINTRPTFILE<%=qryStr%>" marginwidth="10" marginheight="15" scrolling="auto" frameborder="0">
</frameset>
</html>



