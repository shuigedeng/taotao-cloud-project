from  django import conf

def kingadmin_auto_discover():
    for app_name in conf.settings.INSTALLED_APPS:
        # mod = importlib.import_module(app_name, 'kingadmin')
        try:
            mod = __import__('%s.kingadmin' % app_name)
            #print(mod.kingadmin)
        except ImportError :
            pass