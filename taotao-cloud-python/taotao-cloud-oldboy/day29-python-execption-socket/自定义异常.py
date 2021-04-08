class EgonException(BaseException):
    def __init__(self,msg):
        self.msg=msg


# raise  TypeError('类型错误')
# raise EgonException('自己定制的异常')

# print(EgonException('自己定制的异常'))

TypeError
KeyError
Exception