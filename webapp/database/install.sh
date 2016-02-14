echo "Building docker container for OpenRDF enviroment"
docker build -t opensesame .

echo "Starting OpenRDF"
docker run -ti -p 8081:8080 -v ${PWD}/data:/data --rm=true --name my_opensesame opensesame
