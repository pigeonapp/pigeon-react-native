#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(Pigeon, NSObject)
RCT_EXTERN_METHOD(setLogLevel:(NSInteger *)to)
RCT_EXTERN_METHOD(setup:(NSDictionary *)options)
RCT_EXTERN_METHOD(setCustomerToken:(NSString *)identityToken)
RCT_EXTERN_METHOD(track:(NSString *)event data:(NSDictionary *)data)
@end
