<%--
  Created by IntelliJ IDEA.
  User: Frank van Heeswijk
  Date: 30-6-2015
  Time: 17:40
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Test</title>
    <asset:javascript src="application" />
    <asset:javascript src="jquery" />
    <asset:javascript src="spring-websocket" />

    <script type="text/javascript">
        $(function() {
            var socket = new SockJS("${createLink(uri: '/stomp')}");
            var client = Stomp.over(socket);

            client.connect({}, function() {
                client.subscribe("/topic/api/user/create_user", function (message) {
                    $("#response").html(message.body);
                });
            });

            $("#test-response").click(function() {
                client.send("/app/api/user/create_user", {}, JSON.stringify({
                    firstName: "John",
                    middleName: null,
                    lastName: "Doe",
                    email: "john@doe.com",
                    password: "johndoe"
                }));
            });
        });
    </script>
</head>

<body>
<div id="response"></div>
<br />
<button id="test-response">Test</button>
</body>
</html>