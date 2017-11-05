package lib;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ObservableMap<K, V> extends Observable implements Map<K, V>
{
	private final Map<K, V> mMap;
	
	public ObservableMap(Map<K, V> map)
	{
		mMap = map;
	}

	@Override public void clear() { mMap.clear(); change(); }
	@Override public boolean containsKey(Object key) { return mMap.containsKey(key); }
	@Override public boolean containsValue(Object value) { return mMap.containsValue(value); }
	@Override public Set<Entry<K, V>> entrySet() { return Collections.unmodifiableSet(mMap.entrySet()); }
	@Override public V get(Object key) { return mMap.get(key); }
	@Override public boolean isEmpty() { return mMap.isEmpty(); }
	@Override public Set<K> keySet() { return Collections.unmodifiableSet(mMap.keySet()); }
	@Override public V put(K key, V value) { V v = mMap.put(key, value); change(); return v; }
	@Override public void putAll(Map<? extends K, ? extends V> map) { mMap.putAll(map); change(); }
	@Override public V remove(Object key) { V v = mMap.remove(key); change(); return v; }
	@Override public int size() { return mMap.size(); }
	@Override public Collection<V> values() { return Collections.unmodifiableCollection(mMap.values()); }
}
