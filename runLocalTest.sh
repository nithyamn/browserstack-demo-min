python3 -m http.server 8888 &
pid=$!
mvn test -P local_web
kill $pid