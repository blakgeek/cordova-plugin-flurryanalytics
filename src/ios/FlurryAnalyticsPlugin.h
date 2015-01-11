#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface FlurryAnalyticsPlugin : CDVPlugin
- (void)initialize:(CDVInvokedUrlCommand *)command;

- (void)logEvent:(CDVInvokedUrlCommand *)command;

- (void)endTimedEvent:(CDVInvokedUrlCommand *)command;

- (void)logPageView:(CDVInvokedUrlCommand *)command;

- (void)logError:(CDVInvokedUrlCommand *)command;

- (void)setLocation:(CDVInvokedUrlCommand *)command;

- (void)startSession:(CDVInvokedUrlCommand *)command;

- (void)endSession:(CDVInvokedUrlCommand *)command;
@end
