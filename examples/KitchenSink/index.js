/**
 * @format
 */

import { AppRegistry } from 'react-native';
import App from './App';
import { name as appName } from './app.json';
import Pigeon from 'react-native-pigeon';

Pigeon.setLogLevel(0); // 0 debug, 1 info, -1 off

Pigeon.setup({
    publicKey: process.env.PIGEON_PUBLIC_KEY,
    trackAppLifecycleEvents: true,
    trackAppExceptions: true,
});

Pigeon.track('demo_event', { hello: 'world' });

Pigeon.onMessageReceived(({ notification, data }) => {
    const { title, body } = notification;
    console.log(title, body);
    console.log(notification);
    console.log(data);
});

AppRegistry.registerComponent(appName, () => App);
