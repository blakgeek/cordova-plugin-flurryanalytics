package com.blakgeek.cordova.plugin.flurry;

import android.util.Log;
import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryEventRecordStatus;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FlurryAnalyticsPlugin extends CordovaPlugin {

    private static final String LOGTAG = "FlurryPlugin";


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if ("init".equals(action)) {
                init(args, callbackContext);
            } else if ("logEvent".equals(action)) {
                logEvent(args, callbackContext);
            } else if ("endTimedEvent".equals(action)) {
                endTimedEvent(args, callbackContext);
            } else if ("logPageView".equals(action)) {
                logPageView(args, callbackContext);
            } else if ("logError".equals(action)) {
                logError(args, callbackContext);
            } else {
                Log.d(LOGTAG, "Invalid Action: " + action);
                callbackContext.error("Invalid Action: " + action);
                return false;
            }
            return true;
        } catch (JSONException e) {
            Log.d("Flurry exception: ", e.getMessage());
            callbackContext.error("flurry json exception: " + e.getMessage());
            return false;
        }
    }

    private void logError(JSONArray args, CallbackContext callbackContext) throws JSONException {

        FlurryAgent.onError(args.getString(0), args.getString(1), (Exception) null);
    }

    private void logPageView(JSONArray args, CallbackContext callbackContext) {

        FlurryAgent.onPageView();
    }

    private void endTimedEvent(JSONArray args, CallbackContext callbackContext) {

    }

    private void init(JSONArray args, CallbackContext callbackContext) throws JSONException {

        // app key is the only that is required.
        String appKey = args.getString(0);

        // deal with all the optional configuration data
        if (!args.isNull(1)) {

            JSONObject options = args.getJSONObject(1);
            if (!options.isNull("versionName")) {
                FlurryAgent.setVersionName(options.getString("versionName"));
            }
            if (!options.isNull("continueSessionMillis")) {
                // TODO: validate the value is between 10 and 5000 and return error
                FlurryAgent.setContinueSessionMillis(options.getInt("continueSessionMillis"));
            }
            if (!options.isNull("userId")) {
                FlurryAgent.setUserId(options.getString("userId"));
            }
            if (!options.isNull("gender")) {
                char gender = options.getString("gender").toLowerCase().charAt(0);
                if (gender == 'm') {
                    FlurryAgent.setGender(Constants.MALE);
                } else if (gender == 'f') {
                    FlurryAgent.setGender(Constants.FEMALE);
                } else {
                    // TODO: log and warning, leave gender as default
                }
            }
            if (!options.isNull("age")) {
                FlurryAgent.setAge(options.getInt("age"));
            }
            if (!options.isNull("location")) {
                JSONArray location = options.getJSONArray("location");
                FlurryAgent.setLocation((float) location.getDouble(0), (float) location.getDouble(1));
            }
            if (!options.isNull("logLevel")) {
                String level = options.getString("logLevel");

                if ("VERBOSE".equalsIgnoreCase(level)) {
                    FlurryAgent.setLogLevel(Log.VERBOSE);
                    FlurryAgent.setLogEnabled(true);
                } else if ("DEBUG".equalsIgnoreCase(level)) {
                    FlurryAgent.setLogLevel(Log.DEBUG);
                    FlurryAgent.setLogEnabled(true);
                } else if ("INFO".equalsIgnoreCase(level)) {
                    FlurryAgent.setLogLevel(Log.INFO);
                    FlurryAgent.setLogEnabled(true);
                } else if ("WARN".equalsIgnoreCase(level)) {
                    FlurryAgent.setLogLevel(Log.WARN);
                    FlurryAgent.setLogEnabled(true);
                } else if ("ERROR".equalsIgnoreCase(level)) {
                    FlurryAgent.setLogLevel(Log.ERROR);
                    FlurryAgent.setLogEnabled(true);
                } else {
                    // TODO: log and return warning, leave log level at default
                }
            }
            if(!options.isNull("enableEventLogging")) {

                FlurryAgent.setLogEvents(options.getBoolean("enableEventLogging"));
            }
        }
        /*
        iOS only noops for Android

        enableEventLogging
        reportSessionsOnClose
        reportSessionsOnPause
        enableSecureTransport
        enableBackgroundSessions
        enableCrashReporting
        */

        FlurryAgent.init(cordova.getActivity(), appKey);
    }

    private void logEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {

        FlurryEventRecordStatus result;
        String eventName = args.getString(0);
        boolean timed = args.getBoolean(1);

        if (args.isNull(2)) {
            result = FlurryAgent.logEvent(eventName, timed);
        } else {
            result = FlurryAgent.logEvent(eventName, this.jsonObjectToMap(args.getJSONObject(2)), timed);
        }

        if (result == FlurryEventRecordStatus.kFlurryEventRecorded) {
            callbackContext.success();
        } else {
            callbackContext.error(result.toString());
        }
    }

    private Map<String, String> jsonObjectToMap(JSONObject json) throws JSONException {
        if (json == null) {
            Log.d(LOGTAG, "not json");
            return null;
        }
        @SuppressWarnings("unchecked")
        Iterator<String> nameItr = json.keys();
        Map<String, String> params = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            params.put(name, json.getString(name));
        }
        return params;
    }
}
