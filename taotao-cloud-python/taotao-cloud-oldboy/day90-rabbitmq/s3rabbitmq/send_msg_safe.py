import pika
import time

credentials = pika.PlainCredentials('alex', 'alex123')
connection = pika.BlockingConnection(pika.ConnectionParameters(
    '192.168.14.52',credentials=credentials))
channel = connection.channel()

# 声明queue
channel.queue_declare(queue='task_queue',durable=True)

# n RabbitMQ a message can never be sent directly to the queue, it always needs to go through an exchange.
import sys

message = ' '.join(sys.argv[1:]) or "Hello World! %s" % time.time()

channel.basic_publish(exchange='',
                      routing_key='task_queue',
                      body=message,
                      properties=pika.BasicProperties(
                          delivery_mode=2,  # make message persistent
                      )

                      )
print(" [x] Sent %r" % message)
connection.close()