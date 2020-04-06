import { NativeEventEmitter, NativeModules } from 'react-native';

const { Pigeon } = NativeModules;
const PigeonEventEmitter = new NativeEventEmitter(Pigeon);
Pigeon.onMessageReceived = callback =>
  PigeonEventEmitter.addListener('messageReceived', callback);

export default Pigeon;
