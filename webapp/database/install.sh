docker build -t opensesame .
docker run -ti -p 8080:8080 -v data:/data --rm=true --name my_opensesame opensesame
