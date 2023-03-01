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

- Upload the app (`src/test/resources/executables/regression.apk`)
- Upload the media to be injected (for both Image injection and QR scanning - Images in `src/test/resources/images`)
- Add the app hashed id and the media URL in the `src/test/java/app/android/DeviceFeaturesAndroid.java`file
* Run: `mvn -Dtest=DeviceFeaturesAndroid test`

Run All tests 
* `sh run.sh`



