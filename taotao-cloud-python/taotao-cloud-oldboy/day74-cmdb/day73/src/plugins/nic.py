from .base import BasePlugin

class NicPlugin(BasePlugin):

    def windows(self):
        output = self.shell_cmd('asdf')
        # 正则表达式
        return output

    def linux(self):
        output = self.shell_cmd('asdf')
        # 正则表达式
        return output
