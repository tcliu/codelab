define(["core"], function(core) {
	var labels, messages;
	
	function constructCallbackMessage(uploadObj) {
		console.log(uploadObj);
		var m = uploadObj.message;
		for (var i=0; i<uploadObj.data.length; i++) {
			var rec = uploadObj.data[i];
			m += "<br />" + "[" + (i+1) + "] " + rec.fileName + " " + rec.code + " (" + rec.size + " bytes)";
		}
		return m;
	}
	
	function showMessage(msg) {
		$("#msg_upload").html(msg).show();
	}
	
	return {
		name: "upload",
		init: function() {
			$("#btnSubmit").click(function(e) {
				if ($(":file").val()) {
					$("#frm_upload").submit();
				} else {
					showMessage( core.prop("messages/upload.selectFiles") );
				}
			});
			
			$("#frm_upload").submit(function(e) {
				var url = $(this).attr("action");
				var formData = new FormData(this);
				$.ajax({
					url: url,
					type: 'POST',
					data: formData,
					mimeType: "multipart/form-data",
					contentType: false,
					cache: false,
					processData: false,
					success: function(data, textStatus, jqXHR) {
						var uploadObj = $.parseJSON(data);
						var cbmsg = constructCallbackMessage(uploadObj);
						showMessage(cbmsg);
						$(":file").replaceWith( $(":file").clone() ); 
					},
					error: function(jqXHR, textStatus, errorThrown) {
						showMessage("Failed to upload to " + url);
					}           
				});
				e.preventDefault();
			});
		}
	};
});