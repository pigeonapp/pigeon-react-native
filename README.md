# react-native-pigeon

## Getting started

`$ yarn add react-native-pigeon`

or 

`$ npm install react-native-pigeon --save`

### Mostly automatic installation

`$ react-native link react-native-pigeon`

## Usage
```javascript
import Pigeon from 'react-native-pigeon';

Pigeon.setup({ publicKey: 'your_public_key' });
Pigeon.setCustomerToken('customer_token_here');
```

## Track Events

- Note that `setCustomerToken` must be called before `track` otherwise track won't work.

```javascript
import Pigeon from 'react-native-pigeon';

// After Pigeon.setup and Pigeon.setCustomerToken
Pigeon.track('demo_event', { hello: 'world' } /* Event data */);
```

## Listen to remote notifications

```javascript
import Pigeon from 'react-native-pigeon';

// After Pigeon.setup and Pigeon.setCustomerToken
Pigeon.onMessageReceived(({ notification, data }) => {
  const { title, body } = notification;
  // Do something with data, title and body
});
```
