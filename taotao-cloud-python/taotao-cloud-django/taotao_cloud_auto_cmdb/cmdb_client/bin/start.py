import os
import sys


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.environ['USER_SETTINGS'] = 'config.settings'
sys.path.append(BASE_DIR)


if __name__ == '__main__':
    from src import script
    script.run()
