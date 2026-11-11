package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.registry.InMemoryRpcRegistry;
import com.taotao.cloud.rpc.common.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link RegistryService} 的进程内实现，也是 SPI 默认扩展 {@code in-memory} 的落点。
 * <p>
 * 与 {@link InMemoryRpcRegistry} 不是一回事：后者实现 {@link RpcRegistry}
 * （以 {@link ServiceInstance} 为中心、带心跳续约），本类实现的是
 * {@code RegistryDirectory} / {@code ReferenceConfig} / {@code ServiceConfig}
 * 实际依赖的 URL 契约。
 */
public class InMemoryRegistryService implements RegistryService {

    private static final Logger log = LogManager.getLogger(InMemoryRegistryService.class);

    private final Map<String, List<URL>> providers = new ConcurrentHashMap<String, List<URL>>();
    private final Map<String, List<NotifyListener>> listeners = new ConcurrentHashMap<String, List<NotifyListener>>();

    @Override
    public void register(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        String key = keyOf(url);
        List<URL> list = providers.get(key);
        if (list == null) {
            list = new CopyOnWriteArrayList<URL>();
            List<URL> prev = providers.putIfAbsent(key, list);
            if (prev != null) {
                list = prev;
            }
        }
        if (!containsAddress(list, url)) {
            list.add(url);
            notifyListeners(key);
        }
    }

    @Override
    public void unregister(URL url) {
        if (url == null) {
            return;
        }
        String key = keyOf(url);
        List<URL> list = providers.get(key);
        if (list == null) {
            return;
        }
        boolean removed = false;
        for (int i = 0; i < list.size(); i++) {
            if (addressesEqual(list.get(i), url)) {
                list.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            notifyListeners(key);
        }
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            throw new IllegalArgumentException("url or listener == null");
        }
        String key = keyOf(url);
        List<NotifyListener> subs = listeners.get(key);
        if (subs == null) {
            subs = new CopyOnWriteArrayList<NotifyListener>();
            List<NotifyListener> prev = listeners.putIfAbsent(key, subs);
            if (prev != null) {
                subs = prev;
            }
        }
        if (!subs.contains(listener)) {
            subs.add(listener);
        }
        listener.notify(lookup(url));
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        if (url == null) {
            return;
        }
        List<NotifyListener> subs = listeners.get(keyOf(url));
        if (subs != null && listener != null) {
            subs.remove(listener);
        }
    }

    @Override
    public List<URL> lookup(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        List<URL> list = providers.get(keyOf(url));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<URL>(list));
    }

    @Override
    public void destroy() {
        providers.clear();
        listeners.clear();
    }

    private static String keyOf(URL url) {
        String service = url.getServiceInterface();
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("URL has no serviceInterface: " + url);
        }
        return service;
    }

    private static boolean containsAddress(List<URL> list, URL url) {
        for (URL u : list) {
            if (addressesEqual(u, url)) {
                return true;
            }
        }
        return false;
    }

    private static boolean addressesEqual(URL a, URL b) {
        return a.getAddress().equals(b.getAddress());
    }

    private void notifyListeners(String key) {
        List<NotifyListener> subs = listeners.get(key);
        if (subs == null || subs.isEmpty()) {
            return;
        }
        List<URL> urls = Collections.unmodifiableList(new ArrayList<URL>(providers.get(key) == null
                ? Collections.<URL>emptyList() : providers.get(key)));
        for (NotifyListener listener : subs) {
            try {
                listener.notify(urls);
            } catch (Throwable t) {
                log.error("Failed to notify listener {} for {}", listener.getClass().getName(), key, t);
            }
        }
    }
}
