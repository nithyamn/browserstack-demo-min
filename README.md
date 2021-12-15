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
* Local - `sh runLocalTest.sh`
* Single - `mvn test -P parallel_web`

### Automate

Android
* Single - `mvn test -P single_and`
* Local - `mvn test -P local_and`
* Single - `mvn test -P parallel_and`

iOS
* Single - `mvn test -P single_ios`
* Local - `mvn test -P local_ios`
* Single - `mvn test -P parallel_ios`

Camera Injection
* `mvn -Dtest=CameraInjection test`




