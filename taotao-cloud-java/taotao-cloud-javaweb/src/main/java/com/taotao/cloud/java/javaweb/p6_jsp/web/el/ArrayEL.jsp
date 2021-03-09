<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数组、集合的访问</title>
</head>
<body>
    <%
        int[] array = new int[]{1,2,3,4,5};
        request.setAttribute("array",array);

        List<String> nums = new ArrayList<>();
        nums.add("A");
        nums.add("B");
        nums.add("C");
        request.setAttribute("nums",nums);

        Map<String,String> maps = new HashMap<>();
        maps.put("CN","中国");
        maps.put("US","美国");
        maps.put("IT","意大利");
        request.setAttribute("maps",maps);
    %>
<!--EL访问数组 -->
    ${array[1]}<br/>
    ${array[2]}<br/>
    ${array[3]}<br/>
<hr/>
    ${nums[0]}<br/>
    ${nums[1]}<br/>
    ${nums.get(2)}
    <hr/>
    ${maps["CN"]}<br/>
    ${maps["US"]}<br/>
    ${maps.IT}
</body>
</html>
