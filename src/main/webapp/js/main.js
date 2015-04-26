require(["core"], function(core) {

	var hrefMap = {};
	
	$(function() {
		var links = $("#menu a"), target = $("#mainPanel");

		$(window).on("load hashchange", function() {
			var path = location.pathname + location.hash;

			// load content of link which href matches the location hash
			var filtered = links.filter(function(i, elem) {
				return $(elem).attr("href") == path;
			}).each(function() {
				loadLink(this);
			});
		});
		
		function loadLink(elem) {
			links.removeClass("selected");
			$(elem).addClass("selected");
			var href = $(elem).attr("href");
			if (!hrefMap[href]) {
				var src = $(elem).attr("src");
				$.ajax({
					url: src,
					type: 'POST',
					success: function(data, textStatus, jqXHR) {				
						var node = $(data);
						target.html(hrefMap[href] = node);
						console.log("Loaded " + href + " to main panel.");
					},
					error: function(jqXHR, textStatus, errorThrown) {
						console.log("Error when loading " + href);
					}
				});
			} else {
				var node = hrefMap[href];
				var scripts = node.find("script");
				scripts.each(function(i, elem) {
					eval( $(elem).text() );
				});
				target.html(hrefMap[href]);
			}
		}
		
		
	});
});