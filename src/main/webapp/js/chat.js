define(["core", "websocket"], function(core, websocket) {
	var hiddens, stompClient;

	function connect() {
		stompClient = websocket.connect("/messaging", function(frame, stompClient) {
			var username = $("#txtName").val();
			console.log("Connected as " + username);
            var subscription = stompClient.subscribe("/topic/messaging", function(message) {
                addMessage(message);
            });
			updateUI(true);
		}, function(errmsg) {
			console.log("Connection error.");
		});
	}
	
	function disconnect() {
		websocket.disconnect(stompClient, function() {
			stompClient = null;
			console.log($("#txtName").val() + " disconnected.");
			updateUI(false);
		});
	}
	
	function updateUI(connected) {
		$("#txtName").prop("readonly", connected);
		$("#btnConnect").toggleClass("hidden", connected);
		hiddens.toggleClass("hidden", !connected);
		connected && $("#txtMessage").focus();
		!connected && $("#txtName").focus();
	}
	
	function addMessage(message) {
		var messageObj = core.parseJSON(message.body);
		console.log(messageObj);
		var messageText = core.stringify(messageObj);
		var messageUI = $("<div />").addClass("message").text(messageText);
		$("#messages").append(messageUI).scrollTop( $("#messages").prop("scrollHeight") );
	}

	console.log("chat.js loaded.");
	
	return {
		name: "chat",
		init: function() {
			
			if (!hiddens)
				hiddens = $(".hidden");
			
			$("#txtName").on("keypress", function(e) {
				if (e.keyCode == 13) {
					$("#btnConnect").click();
				}
			}).focus();
		
			$("#btnConnect, #btnDisconnect").on("click", function(e) {
				if ($("#txtName").val()) {
					!stompClient ? connect() : disconnect();
				} else {
					$("#txtName").focus();
				}
			});
		
			$("#btnSend").on("click", function(e) {
				var messageObj = {name: $("#txtName").val(), message: $("#txtMessage").val()};
				if (messageObj.message) {
					stompClient.send("/app/messaging", {}, core.stringify(messageObj));
				} else {
					$("#txtMessage").focus();
				}
			});
			
			$("#txtMessage").on("keydown", function(e) {
				if (e.ctrlKey && e.keyCode == 13) {
					$("#btnSend").click();
				}
			});
		}
	};
	
});