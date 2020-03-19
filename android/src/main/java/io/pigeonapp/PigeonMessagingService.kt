package io.pigeonapp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PigeonMessagingService : FirebaseMessagingService() {
    private val TAG = PigeonMessagingService::class.qualifiedName
    private val pigeonClient: PigeonClient = PigeonClient.getInstance()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pigeonClient.setDeviceToken(token)
        PigeonLog.d(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        pigeonClient.handleMessage(remoteMessage)
    }
}
