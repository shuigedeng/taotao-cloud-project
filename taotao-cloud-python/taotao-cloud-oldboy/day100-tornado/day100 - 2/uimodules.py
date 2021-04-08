from tornado.web import UIModule
from tornado import escape

class Custom(UIModule):
    def css_files(self):
        return "commons.css"
    def embedded_css(self):
        return ".c1{display:none}"
    def javascript_files(self):
        return "commons.js"
    def embedded_javascript(self):
        return "function f1(){alert(123);}"
    def render(self, val):
        # return "老村长"
        # return '<h1>老村长</h1>'
        v = escape.xhtml_escape('<h1>老村长</h1>')
        return v