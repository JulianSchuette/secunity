echo "Building docker container for tomcat environment"
docker build -t secunity-rest-api .

echo "Starting Tomcat"
docker run -ti -p 8080:8080 -v ${PWD}/data:/data --rm=true --name webapp_rest-api secunity-rest-api
