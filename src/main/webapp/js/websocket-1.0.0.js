// WebSocket module
define(["sockjs", "stomp"], function(sockjs, stomp) {
	
	return {
		name: "WebSocket",
		version: "1.0.0",
		
		connect: function(channel, onConnect, onError) {
			var socket = new SockJS(channel);
			var stompClient = Stomp.over(socket);
			var connectCallback = function(frame) {
				if (onConnect) {
					onConnect(frame, stompClient);
				}
			};
			var errorCallback = function(error) {
				if (onError) {
					onError(error);
				}
			}
			stompClient.connect('', '', connectCallback, errorCallback);
			return stompClient;
		},
	
		disconnect: function(stompClient, onDisconnect) {
			stompClient.disconnect(onDisconnect);
		}
	
	};
});