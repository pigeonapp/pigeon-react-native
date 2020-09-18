import Foundation
import Alamofire

class PigeonClient {
    static let instance = PigeonClient()
    private var anonymousUid: String?
    private var publicKey: String?
    private var uid: String?
    private var deviceToken: String?
    private var customerToken: String?
    private var baseUri: String = "https://api.pigeonapp.io/v1"

    private init() {
        generateAnonymousUid()
    }

    func setBaseUri(to: String) {
        self.baseUri = to
    }

    func setPublicKey(to: String) {
        self.publicKey = to
    }

    func setDeviceToken(to: String) {
        self.deviceToken = to
        
        saveContact()
    }

    func setCustomerToken(to: String) {
        self.customerToken = to
        
        saveContact()
    }

    private func generateAnonymousUid(withRegenerate: Bool = false) {
        let preferences = UserDefaults.standard
        let anonymousUidKey = Constants.USER_PREFERENCES_KEY_ANONYMOUS_UID

        if !withRegenerate && preferences.object(forKey: anonymousUidKey) != nil {
            self.anonymousUid = preferences.string(forKey: anonymousUidKey) ?? nil
        } else {
            self.anonymousUid = UUID().uuidString
            preferences.set(self.anonymousUid, forKey: anonymousUidKey)
        }
    }

    private func makeHttpRequest<T: Encodable>(
        uri: String,
        _ request: T,
        withHeaders: Dictionary<String, String> = [:],
        _ onSuccess: @escaping (Data?) -> Void,
        _ onError: @escaping (Error) -> Void
    ) {

        let headerDictionary = withHeaders.merging(
            [ "X-Public-Key": publicKey ?? "" ]
        ) { (current, _) in current }

        let headers = HTTPHeaders.init(headerDictionary)

        // For debugging
        if let jsonData = try? JSONEncoder().encode(request),
        let jsonString = String(data: jsonData, encoding: .utf8) {
            PigeonLog.d(tag: #function, jsonString)
        }

        AF.request("\(baseUri)/\(uri)", method: .post, parameters: request, encoder: JSONParameterEncoder.default, headers: headers).validate().responseJSON { response in switch response.result {
                    case .success:
                        onSuccess(response.data)
                    case let .failure(error):
                        onError(error)
            }
        }
    }

    func track(_ event: String, withData: NSDictionary?) {
        if customerToken == nil {
            PigeonLog.d(tag: #function, "track failed, customerToken: \(customerToken)")
            return
        }

        let trackData: JSONValue?

        if withData == nil {
            trackData = nil
        } else if JSONSerialization.isValidJSONObject(withData!) {
            trackData = try? JSONDecoder().decode(JSONValue.self, from: JSONSerialization.data(withJSONObject: withData!))
        } else {
            PigeonLog.d(tag: #function, "Encountered an error while encoding track data")
            return
        }

        let trackRequest = TrackRequest(event: event, data: trackData)
        
        let headers = [ "X-Customer-Token": customerToken! ]

        PigeonLog.d(tag: #function, "Calling track with \(trackRequest)")

        makeHttpRequest(uri: "event_logs", trackRequest, withHeaders: headers,
            { response in PigeonLog.d(tag: #function, "Sent track with \(String(describing: response))") },
            { error in PigeonLog.d(tag: #function, "Encountered an error during track(): \(error)") }
        )
    }

    func saveContact() {
        if deviceToken == nil || customerToken == nil {
            PigeonLog.d(tag: #function, "saveContact failed, deviceToken: \(deviceToken) customerToken: \(customerToken)")
            return
        }

        let deviceKind = "IOS"
        let deviceName = UIDevice().name
        let saveContactRequest = SaveContactRequest(
            name: deviceName, value: deviceToken!, kind: deviceKind
        )

        PigeonLog.d(tag: #function, "Calling saveContact with \(saveContactRequest)")

        let headers = [ "X-Customer-Token": customerToken! ]

        makeHttpRequest(uri: "contacts", saveContactRequest, withHeaders: headers,
            { response in PigeonLog.d(tag: #function, "Saved Contact with \(String(describing: response))") },
            { error in PigeonLog.d(tag: #function, "Encountered an error during saveContact(): \(error)") }
        )
    }
}
