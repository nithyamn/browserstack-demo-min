mvn test -P single_web
mvn test -P single_and
mvn test -P single_ios

mvn test -P parallel_web
mvn test -P parallel_and
mvn test -P parallel_ios

mvn test -P local_web
mvn test -P local_and
mvn test -P local_ios

mvn -Dtest=DeviceFeaturesAndroid test