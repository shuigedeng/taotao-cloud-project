import selectors
import socket

sel = selectors.DefaultSelector()

def accept(sock, mask):
    conn, addr = sock.accept()  # Should be ready
    print('accepted', conn, 'from', addr)
    conn.setblocking(False)
    sel.register(conn, selectors.EVENT_READ, read)

def read(conn, mask):
    try:
        data = conn.recv(1000)  # Should be ready
        if not data:
            raise Exception
        print('echoing', repr(data), 'to', conn)
        conn.send(data)  # Hope it won't block
    except Exception as e:
        print('closing', conn)
        sel.unregister(conn)
        conn.close()

sock = socket.socket()
sock.bind(('localhost', 8090))
sock.listen(100)
sock.setblocking(False)

sel.register(sock, selectors.EVENT_READ, accept)
print("server.....")

while True:
    events = sel.select()#[sock,,conn2]
    for key, mask in events:
        callback = key.data
        callback(key.fileobj, mask)

import socket
import select
sk=socket.socket()
sk.bind(("127.0.0.1",9904))
sk.listen(5)

while True:
    r,w,e=select.select([sk,sk],[],[],5)
    r=[sk,]
    for i in r:
        conn,add=i.accept()
        print(conn)
        print("hello")

    print('>>>>>>')