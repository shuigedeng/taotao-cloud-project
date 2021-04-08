import pika
import uuid


class FibonacciRpcClient(object):
    def __init__(self):


        credentials = pika.PlainCredentials('alex', 'alex123')
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(
            '192.168.14.52', credentials=credentials))
        channel = self.connection.channel()
        self.channel = self.connection.channel()

        result = self.channel.queue_declare(exclusive=True)
        self.callback_queue = result.method.queue

        self.channel.basic_consume(self.on_response, no_ack=True,#准备接受命令结果
                                   queue=self.callback_queue)


    def on_response(self, ch, method, props, body):
        """"callback方法"""
        if self.corr_id == props.correlation_id:
            self.response = body

    def call(self, n):
        self.response = None
        self.corr_id = str(uuid.uuid4()) #唯一标识符
        self.channel.basic_publish(exchange='',
                                   routing_key='rpc_queue',
                                   properties=pika.BasicProperties(
                                       reply_to=self.callback_queue,
                                       correlation_id=self.corr_id,
                                   ),
                                   body=str(n))

        count  = 0
        while self.response is None:
            self.connection.process_data_events() #检查队列里有没有新消息,但不会阻塞
            count +=1
            print("check...",count)

        return int(self.response)


fibonacci_rpc = FibonacciRpcClient()

print(" [x] Requesting fib(30)")
response = fibonacci_rpc.call(5)
print(" [.] Got %r" % response)