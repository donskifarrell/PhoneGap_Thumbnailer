/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2011, IBM Corporation
 */

/**
 * Constructor
 */
function Thumbnailer() {
	var THUMBNAIL_HEIGHT = 48;
	var THUMBNAIL_WIDTH = 66;

	function thumbError(err){
		alert('Error creating thumbnail(s): ' + err);
	};

	return {
		createVideoThumbnail: function(url, callback) {	
			if (url.toLowerCase().indexOf("pg_thumbs") >= 0){
				return;
			}
			if (url.toLowerCase().indexOf("file://")==0){
				url =url.substring(7); 
			}

		    cordova.exec(callback, this.thumbError, "Thumbnailer", "createVideoThumbnail", [url]);
		},

		createImageThumbnail: function(url, callback, dimensions) {
			if (url.toLowerCase().indexOf("pg_thumbs") >= 0){
				return;
			}
			if (url.toLowerCase().indexOf("file://")==0){
				url =url.substring(7); 
			}

			dimensions = (typeof dimensions === "undefined") ? [ THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT ] : dimensions;
		    cordova.exec(callback, this.thumbError, "Thumbnailer", "createImageThumbnail", [url, dimensions]);
		},

		createAlbumThumbnails: function(path, callback, dimensions) {
			if (url.toLowerCase().indexOf("pg_thumbs") >= 0){
				alert("Already within a thumbnail directory!");
				return;
			}
			if (url.toLowerCase().indexOf("file://")==0){
				url =url.substring(7); 
			}

			dimensions = (typeof dimensions === "undefined") ? [ THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT ] : dimensions;
		    cordova.exec(callback, this.thumbError, "Thumbnailer", "createAlbumThumbnails", [url, dimensions]);
		}
	}
};

if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.thumbnailer) {
    window.plugins.thumbnailer = new Thumbnailer();
}
