from lib.conf.config import settings
from .client import Agent
from .client import SSH_SALT


def run():
    if settings.MODE == 'AGENT':
        obj = Agent()
    else:
        obj = SSH_SALT()
    obj.execute()