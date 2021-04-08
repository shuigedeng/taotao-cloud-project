import abc
class All_file(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def read(self):
        pass

    @abc.abstractmethod
    def write(self):
        pass

class Disk(All_file):
    def read(self):
        print('disk read')

    def write(self):
        print('disk write')

class Cdrom(All_file):
    def read(self):
        print('cdrom read')

    def write(self):
        print('cdrom write')


class Mem(All_file):
    def read(self):
        print('mem read')

    def write(self):
        print('mem write')
#
m1=Mem()
m1.read()
m1.write()
