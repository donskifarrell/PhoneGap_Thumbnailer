package org.apache.cordova.plugin;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;

//thumbnailer
import android.util.Log;
import android.media.ThumbnailUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Video.Thumbnails;
import java.io.FileOutputStream;
import java.io.File;

/**
 * This class echoes a string called from JavaScript.
 */
public class Thumbnailer extends Plugin {
	private static final String LOG_TAG = "CordovaThumbnailer";
	private static final String GEN_THUMB_DIR = "pg_thumbs";
	private static int THUMBNAIL_WIDTH = 66;
	private static int THUMBNAIL_HEIGHT = 48;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        try {
			if (!validArgs(args)) {
				Log.e(LOG_TAG, "Invalid Arguments Specified!");
				return new PluginResult(PluginResult.Status.ERROR);
			} else {
				JSONArray dimensions = args.getJSONArray(1);
				THUMBNAIL_WIDTH = dimensions.getInt(0);
				THUMBNAIL_HEIGHT = dimensions.getInt(1);
			}

			String mediaUrl = args.getString(0); 
			File thumbFile = createThumbnailFile(mediaUrl);
			if (thumbFile.exists()){
				Log.i(LOG_TAG, "Image thumbnail already exists");
				return new PluginResult(PluginResult.Status.OK, thumbFile.getPath());
			}

        	FileOutputStream out = new FileOutputStream(thumbFile);
			Bitmap bmThumbnail;

			if (action.equals("createVideoThumbnail")) {
				// MINI_KIND: 512 x 384 thumbnail 
    			bmThumbnail = ThumbnailUtils.createVideoThumbnail(mediaUrl, Thumbnails.MINI_KIND);
			} else if (action.equals("createImageThumbnail")) {
    			Bitmap bitmap = BitmapFactory.decodeFile(mediaUrl);
				bmThumbnail = ThumbnailUtils.extractThumbnail(bitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
			} else {
				Log.e(LOG_TAG, "Invalid action supplied to plugin!");
				return new PluginResult(PluginResult.Status.INVALID_ACTION);
			}	

			bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 55, out);
			return new PluginResult(PluginResult.Status.OK, thumbFile.getPath());

		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException - " + e.toString());
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);

		} catch (Exception e) {
			Log.e(LOG_TAG, "General Exception - " + e.toString());
			return new PluginResult(PluginResult.Status.ERROR);
		}
	}

	// Creates the thumbnail file and any directories in the path that are missing.
	private File createThumbnailFile(String mediaUrl){		
		Log.i(LOG_TAG, "Creating thumbnail for media item: " + mediaUrl);
		String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1);	
		String newThumbPath = mediaUrl.substring(0, mediaUrl.lastIndexOf("/")) + "/" + GEN_THUMB_DIR + "/";

		File thumbFile = new File(newThumbPath + fileName);
		thumbFile.getParentFile().mkdirs();
		return thumbFile;
	}

	// Checks that args are valid. Basic at the moment, but can expand this.
	private boolean validArgs(JSONArray args){
		if (args == null || args.length() < 0){
			return false;
		}
		return true;
	}
}