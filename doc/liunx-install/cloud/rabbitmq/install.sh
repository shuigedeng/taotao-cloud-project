docker search rabbitmq:management

docker pull docker.io/macintoshplus/rabbitmq-management

docker run -d --name rabbitmq -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest -p 15672:15672 -p 5672:5672 macintoshplus/rabbitmq-management

http://127.0.0.1:15672/
