from conf import settings

def pack():
    response = {}
    for k,v in settings.PLUGINS.items():
        import importlib
        m_path,classname = v.rsplit('.',maxsplit=1)
        m = importlib.import_module(m_path)
        cls = getattr(m,classname)
        response[k] = cls().execute()
    return response