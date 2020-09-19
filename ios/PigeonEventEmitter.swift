import Foundation

class PigeonEventEmitter {
    static let sharedInstance = PigeonEventEmitter()
    private static var eventEmitter: RCTEventEmitter!

    private init() {}

    func registerEventEmitter(eventEmitter: RCTEventEmitter) {
        PigeonEventEmitter.eventEmitter = eventEmitter
    }

    func dispatch(name: String, body: Any?) {
        PigeonEventEmitter.eventEmitter.sendEvent(withName: name, body: body)
    }
}
