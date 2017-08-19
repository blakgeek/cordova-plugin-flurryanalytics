#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>

@interface FlurryAnalyticsPlugin : CDVPlugin
- (void)initialize:(CDVInvokedUrlCommand *)command;

- (void)setUserId:(CDVInvokedUrlCommand *)command;

- (void)setAge:(CDVInvokedUrlCommand *)command;

- (void)setGender:(CDVInvokedUrlCommand *)command;

- (void)logEvent:(CDVInvokedUrlCommand *)command;

- (void)endTimedEvent:(CDVInvokedUrlCommand *)command;

- (void)logPageView:(CDVInvokedUrlCommand *)command;

- (void)logError:(CDVInvokedUrlCommand *)command;

- (void)setLocation:(CDVInvokedUrlCommand *)command;

@end
