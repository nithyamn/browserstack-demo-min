python -m SimpleHTTPServer 8888 &
pid=$!
mvn test -P local_web
kill $pid