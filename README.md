# browserstack-demo

## Pre-requisites
* Set the BrowserStack credentials as Environment variables
```
export BROWSERSTACK_USERNAME=<browserstack-username>
export BROWSERSTACK_ACCESS_KEY=<browserstack-accesskey>
export BROWSERSTACK_APP_ID=<app-hashed-id>
export BROWSERSTACK_MEDIA_URL=<media-hashed-id>
```

## Commands to Execute tests

### Automate
* Single - `mvn test -P single_web`
* Local - `mvn test -P local_web`
* Single - `mvn test -P parallel_web`

### App Automate

Android
* Single - `mvn test -P single_and`
* Local - `mvn test -P local_and`
* Single - `mvn test -P parallel_and`

iOS
* Single - `mvn test -P single_ios`
* Local - `mvn test -P local_ios`
* Single - `mvn test -P parallel_ios`

Device features (Android)
* `mvn -Dtest=DeviceFeaturesAndroid test`

Run All tests 
* `sh run.sh`



