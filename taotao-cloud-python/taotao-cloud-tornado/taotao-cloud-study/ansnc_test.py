import time
import _thread

gen = None


def long_io():
    def inner():
        global gen
        print('111111')
        time.sleep(5)
        print('222222')
        result = 'io result'
        try:
            gen.send(result)
        except StopIteration:
            pass

    _thread.start_new_thread(inner, ())


def gen_coroutine(func):
    def warpper():
        global gen
        gen = func()
        gen.__next__()

    return warpper


@gen_coroutine
def req_a():
    print('-----a1')
    ret = yield long_io()
    print(ret)
    print('------a2')


def req_b():
    print('------b1')
    time.sleep(2)
    print('------b2')


def main():
    global gen
    gen = req_a()
    gen.__next__()
    req_b()
    while 1:
        pass


if __name__ == '__main__':
    main()
