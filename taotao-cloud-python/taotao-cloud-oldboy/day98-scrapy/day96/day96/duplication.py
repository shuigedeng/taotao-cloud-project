
class RepeatFilter(object):
    def __init__(self):
        self.visited_set = set()
    @classmethod
    def from_settings(cls, settings):
        return cls()

    def request_seen(self, request):
        if request.url in self.visited_set:
            return True
        self.visited_set.add(request.url)
        return False

    def open(self):  # can return deferred
        # print('open')
        pass

    def close(self, reason):  # can return a deferred
        # print('close')
        pass
    def log(self, request, spider):  # log that a request has been filtered
        # print('log....')
        pass