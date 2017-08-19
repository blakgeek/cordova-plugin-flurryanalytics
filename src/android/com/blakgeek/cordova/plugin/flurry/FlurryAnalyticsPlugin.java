package com.blakgeek.cordova.plugin.flurry;

import android.util.Log;

import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.flurry.android.FlurryEventRecordStatus;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class FlurryAnalyticsPlugin extends CordovaPlugin implements FlurryAgentListener {

    private static final String LOGTAG = "FlurryPlugin";
    private static final List<String> SUPPORTED_ACTIONS = Arrays.asList(
            "initialize",
            "logEvent",
            "endTimedEvent",
            "logPageView",
            "logError",
            "setLocation",
            "setUserId",
            "setGender",
            "setAge",
            "startSession",
            "endSession"
    );


    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (SUPPORTED_ACTIONS.contains(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        switch (action) {
                            case "initialize":
                                init(args, callbackContext);
                                break;
                            case "logEvent":
                                logEvent(args, callbackContext);
                                break;
                            case "endTimedEvent":
                                endTimedEvent(args, callbackContext);
                                break;
                            case "logPageView":
                                logPageView(callbackContext);
                                break;
                            case "logError":
                                logError(args, callbackContext);
                                break;
                            case "setLocation":
                                setLocation(args, callbackContext);
                                break;
                            case "setUserId":
                                setUserId(args, callbackContext);
                                break;
                            case "setAge":
                                setAge(args, callbackContext);
                                break;
                            case "setGender":
                                setGender(args, callbackContext);
                                break;
                            case "startSession":
                                startSession(callbackContext);
                                break;
                            case "endSession":
                                endSession(callbackContext);
                                break;
                        }
                    } catch (JSONException e) {
                        Log.d("Flurry exception: ", e.getMessage());
                        callbackContext.error("flurry json exception: " + e.getMessage());
                    }
                }
            });
            return true;
        } else {
            Log.d(LOGTAG, "Invalid Action: " + action);
            callbackContext.error("Invalid Action: " + action);
            return false;
        }
    }

    private void setLocation(JSONArray args, CallbackContext callbackContext) throws JSONException {

        float latitude = (float) args.getDouble(0);
        float longitude = (float) args.getDouble(1);

        try {
            FlurryAgent.setLocation(latitude, longitude);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void logError(JSONArray args, CallbackContext callbackContext) throws JSONException {

        try {
            FlurryAgent.onError(args.getString(0), args.getString(1), new Exception(args.getString(1)));
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void logPageView(CallbackContext callbackContext) {

        try {
            FlurryAgent.onPageView();
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void endTimedEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {

        String event = args.getString(0);
        if (args.isNull(1)) {
            FlurryAgent.endTimedEvent(event);
        } else {
            FlurryAgent.endTimedEvent(event, this.jsonObjectToMap(args.getJSONObject(1)));
        }
        callbackContext.success();
    }

    private void init(JSONArray args, CallbackContext callbackContext) throws JSONException {

        try {

            FlurryAgent.Builder builder = new FlurryAgent.Builder();

            // deal with all the optional configuration data
            if (!args.isNull(1)) {

                JSONObject options = args.getJSONObject(1);

                if (!options.isNull("version")) {
                    FlurryAgent.setVersionName(options.getString("version"));
                }
                if (!options.isNull("continueSessionSeconds")) {
                    // TODO: validate the value is less than 5 and return error
                    builder.withContinueSessionMillis(options.getInt("continueSessionSeconds") * 1000);
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
                    }
                }
                if (!options.isNull("age")) {
                    FlurryAgent.setAge(options.getInt("age"));
                }

                if (!options.isNull("enablePulse")) {
                    builder.withPulseEnabled(options.getBoolean("enablePulse"));
                }

                switch (options.optString("logLevel").toUpperCase()) {

                    case "VERBOSE":
                        builder.withLogLevel(Log.VERBOSE);
                        builder.withLogEnabled(true);
                        break;
                    case "DEBUG":
                        builder.withLogLevel(Log.DEBUG);
                        builder.withLogEnabled(true);
                        break;
                    case "INFO":
                        builder.withLogLevel(Log.INFO);
                        builder.withLogEnabled(true);
                        break;
                    case "WARN":
                        builder.withLogLevel(Log.WARN);
                        builder.withLogEnabled(true);
                        break;
                    case "ERROR":
                        builder.withLogLevel(Log.ERROR);
                        builder.withLogEnabled(true);
                        break;
                }


                if (!options.isNull("enableEventLogging")) {

                    builder.withLogEnabled(options.getBoolean("enableEventLogging"));
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

            // app key is the only that is required.
            String appKey = args.getString(0);
            builder.withListener(this);
            builder.build(cordova.getActivity(), appKey);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }

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

    private void setUserId(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            FlurryAgent.setUserId(args.getString(0));
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void setAge(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            FlurryAgent.setUserId(args.optString(0));
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void setGender(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            char gender = args.optString(0, "x").toLowerCase().charAt(0);
            if (gender == 'm') {
                FlurryAgent.setGender(Constants.MALE);
            } else if (gender == 'f') {
                FlurryAgent.setGender(Constants.FEMALE);
            }
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private Map<String, String> jsonObjectToMap(JSONObject json) throws JSONException {
        if (json == null) {
            Log.d(LOGTAG, "not json");
            return null;
        }
        @SuppressWarnings("unchecked")
        Iterator<String> nameItr = json.keys();
        Map<String, String> params = new HashMap<>();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            params.put(name, json.getString(name));
        }
        return params;
    }

    private void startSession(CallbackContext callbackContext) {

        try {
            FlurryAgent.onStartSession(cordova.getActivity());
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void endSession(CallbackContext callbackContext) {

        try {
            FlurryAgent.onEndSession(cordova.getActivity());
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    @Override
    public void onSessionStarted() {

    }
}
