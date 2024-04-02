cd ./build/docker

docker build -t swedockeracc/snyder-backend:latest .

docker push swedockeracc/snyder-backend:latest

pause