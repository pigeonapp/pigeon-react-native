import Foundation
import FirebaseInstanceID

@objc(Pigeon)
class Pigeon: NSObject {
    private var pigeonClient = PigeonClient.instance

    @objc
    func setLogLevel(_ logLevel: Int) {
        PigeonLog.setLogLevel(to: logLevel)
    }

    @objc
    func setup(_ options: NSDictionary?) {
        pigeonClient.setPublicKey(to: options?.value(forKey: "publicKey") as! String)
        
        if let baseUri = options?.value(forKey: "baseUri") {
            pigeonClient.setBaseUri(to: baseUri as! String)
        }
        
        PigeonLog.d(tag: #function, "Initialised with \(options)")

        InstanceID.instanceID().instanceID { (result, error) in
            if let error = error {
                PigeonLog.d(tag: #function, "Error fetching remote instance ID: \(error)")
            } else if let result = result {
                PigeonLog.d(tag:  #function, "Firebase initialised with: \(result.token)")
                self.pigeonClient.setDeviceToken(to: result.token)
            }
        }
    }
        
    @objc
    func setCustomerToken(_ customerToken: String) {
        pigeonClient.setCustomerToken(to: customerToken)
    }
        
    @objc(track:data:)
    func track(_ event: String, _ data: NSDictionary?) {
        pigeonClient.track(event, withData: data)
    }
}
