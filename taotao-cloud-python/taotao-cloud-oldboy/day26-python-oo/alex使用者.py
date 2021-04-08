from ftp_client import FtpClient

f1=FtpClient('1.1.1.1')
# f1.put()

if hasattr(f1,'put'):
    func_get=getattr(f1,'put')
    func_get()
else:
    print('其他的逻辑')