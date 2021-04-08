# 1. 特殊对象
#     - getPage(url)                  自动完成
#     - _close = defer.Defered()      _close.callback(None)
#
# 2. deter.inlineCallbacks
#
# 3. reactor.callLater(0,函数)
#
# 4.
#     reactor.run()
#     reactor.stop()
#
# 5.
#     dd = defer.DeferList([d1,d2])
#     dd.addCallBack(lambda _:reactor.stop())
#