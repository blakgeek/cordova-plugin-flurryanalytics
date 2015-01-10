


function FlurryAnalytics() {

	/*
	 the only function that is actually required to start tracking sessions

	 options:

	 versionName
	 continueSessionMillis       (must be between 10 and 5000)
	 userId
	 gender
	 age
	 location
	 logLevel                    (DEBUG, ERROR, WARN)
	 reportSessionsOnClose       (defaults to true)
	 reportSessionsOnPause       (defaults to true)
	 enableSecureTransport       (defaults to false)
	 enableBackgroundSessions    (defaults to false)
	 enableLogging             (defaults to false)
	 enableEventLogging          (defaults to true)
	 enableDebugLog              (defaults to false)
	 enableCrashReporting        (defaults to false)
	 */
	this.init = function(appKey, options, successCallback, failureCallback) {
		
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'init', [appKey, options]);
	};

	// the params parameter is optional
	this.logEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback = null,
			failureCallback = null,
			params = null;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		}

		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'logEvent', [event, false, params]);
	};

	// the params parameter is optional
	this.startTimedEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback = null,
			failureCallback = null,
			params = null;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		}

		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'logEvent', [event, true, params]);
	};

	// the params parameter is optional
	this.endTimedEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback = null,
			failureCallback = null,
			params = null;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		}
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'endTimedEvent', [event, params]);
	};

	this.logPageView = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'logPageView', []);
	};

	this.logError = function(code, message, successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'logError', [code, message]);
	};


	// only needed for older versions of Android
	this.startSession = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'startSession', []);
	};

	// only needed for older versions of Android
	this.endSession = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, 'FlurryAnalyticsPlugin', 'endSession', []);
	};

}

if(typeof module !== undefined && module.exports) {

	module.exports = FlurryAnalytics;
}
