import Foundation

@objc(PigeonAppDelegate)
class PigeonAppDelegate: NSObject {
    static func swizzleDidReceiveRemoteNotification() {
        DispatchQueue.main.async {
            let appDelegate = UIApplication.shared.delegate
            let appDelegateClass = object_getClass(appDelegate)

            let originalSelector = #selector(UIApplicationDelegate.application(_:didReceiveRemoteNotification:))
            let swizzledSelector = #selector(
                Pigeon.self.handleMessage(_:didReceiveRemoteNotification :)
            )

            guard let swizzledMethod = class_getInstanceMethod(Pigeon.self, swizzledSelector) else {
                return
            }

            if let originalMethod = class_getInstanceMethod(appDelegateClass, originalSelector)  {
                method_exchangeImplementations(originalMethod, swizzledMethod)
            } else {
                class_addMethod(appDelegateClass, swizzledSelector, method_getImplementation(swizzledMethod), method_getTypeEncoding(swizzledMethod))
            }
        }
    }
}
