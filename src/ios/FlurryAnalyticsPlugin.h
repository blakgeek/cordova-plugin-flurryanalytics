#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface FlurryPlugin : CDVPlugin

- (void)init:(CDVInvokedUrlCommand *)command;

- (void)logEvent:(CDVInvokedUrlCommand *)command;

- (void)endTimedEvent:(CDVInvokedUrlCommand *)command;

- (void)logPageView:(CDVInvokedUrlCommand *)command;

- (void)setUserId:(CDVInvokedUrlCommand *)command;

- (void)setGender:(CDVInvokedUrlCommand *)command;

- (void)setAge:(CDVInvokedUrlCommand *)command;

- (void)setLocation:(CDVInvokedUrlCommand *)command;

- (void)logError:(CDVInvokedUrlCommand *)command;

- (void)setSessionReportsOnCloseEnabled:(CDVInvokedUrlCommand *)command;

- (void)setSessionReportsOnPauseEnabled:(CDVInvokedUrlCommand *)command;

- (void)setShowErrorInLogEnabled:(CDVInvokedUrlCommand *)command;

- (void)setEventLoggingEnabled:(CDVInvokedUrlCommand *)command;

- (void)setDebugLogEnabled:(CDVInvokedUrlCommand *)command;

- (void)setCrashReportingEnabled:(CDVInvokedUrlCommand *)command;

- (void)setAppVersion:(CDVInvokedUrlCommand *)command;

- (void)setSessionContinueSeconds:(CDVInvokedUrlCommand *)command;

- (void)startSession:(CDVInvokedUrlCommand *)command;

- (void)endSession:(CDVInvokedUrlCommand *)command;
@end
