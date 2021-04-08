


import queue,time

import multiprocessing
def foo(q):
    time.sleep(1)
    print("son process",id(q))
    q.put(123)
    q.put("yuan")

if __name__ == '__main__':
    #q=queue.Queue()
    q=multiprocessing.Queue()
    p=multiprocessing.Process(target=foo,args=(q,))
    p.start()
    #p.join()
    print("main process",id(q))
    print(q.get())
    print(q.get())






from multiprocessing import Process, Pipe
def f(conn):
    conn.send([12, {"name":"yuan"}, 'hello'])
    response=conn.recv()
    print("response",response)
    conn.close()
    print("q_ID2:",id(conn))

if __name__ == '__main__':

    parent_conn, child_conn = Pipe() #双向管道

    print("q_ID1:",id(child_conn))
    p = Process(target=f, args=(child_conn,))
    p.start()

    print(parent_conn.recv())   # prints "[42, None, 'hello']"
    parent_conn.send("儿子你好!")
    p.join()


from multiprocessing import Process, Manager

def f(d, l,n):

    d[n] = '1'    #{0:"1"}
    d['2'] = 2    #{0:"1","2":2}

    l.append(n)    #[0,1,2,3,4,   0,1,2,3,4,5,6,7,8,9]
    #print(l)


if __name__ == '__main__':

    with Manager() as manager:

        d = manager.dict()#{}

        l = manager.list(range(5))#[0,1,2,3,4]


        p_list = []

        for i in range(10):
            p = Process(target=f, args=(d,l,i))
            p.start()
            p_list.append(p)

        for res in p_list:
            res.join()

        print(d)
        print(l)
