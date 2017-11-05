package lib;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

public class ObservableList<T> extends Observable implements List<T>
{
	private final List<T> mList;
	private final Property<Number> mSizeProperty;
	
	public ObservableList(List<T> list)
	{
		mList = list;
		mSizeProperty = new SimpleIntegerProperty(list.size());
	}
	
	public Property<Number> sizeProperty()
	{
		return mSizeProperty;
	}
	
	@Override
	protected void change()
	{
		mSizeProperty.setValue(mList.size());
		
		super.change();
	}
	
	@Override public boolean add(T e) { boolean v = mList.add(e); change(); return v; }
	@Override public void add(int index, T element) { mList.add(index, element); change(); }
	@Override public boolean addAll(Collection<? extends T> c) { boolean v = mList.addAll(c); change(); return v; }
	@Override public boolean addAll(int index, Collection<? extends T> c) { boolean v = addAll(index, c); change(); return v; }
	@Override public void clear() { mList.clear(); change(); }
	@Override public boolean contains(Object o) { return mList.contains(o); }
	@Override public boolean containsAll(Collection<?> c) { return mList.containsAll(c); }
	@Override public T get(int index) { return mList.get(index); }
	@Override public int indexOf(Object o) { return mList.indexOf(o); }
	@Override public boolean isEmpty() { return mList.isEmpty(); }
	@Override public Iterator<T> iterator() { return mList.iterator(); }
	@Override public int lastIndexOf(Object o) { return mList.lastIndexOf(o); }
	@Override public ListIterator<T> listIterator() { return mList.listIterator(); }
	@Override public ListIterator<T> listIterator(int index) { return mList.listIterator(index); }
	@Override public boolean remove(Object o) { boolean v = mList.remove(o); change(); return v; }
	@Override public T remove(int index) { T v = mList.remove(index); change(); return v; }
	@Override public boolean removeAll(Collection<?> c) { boolean v = mList.removeAll(c); change(); return v; }
	@Override public boolean retainAll(Collection<?> c) { boolean v = mList.retainAll(c); change(); return v; }
	@Override public T set(int index, T element) { T v = mList.set(index, element); change(); return v; }
	@Override public int size() { return mList.size(); }
	@Override public List<T> subList(int fromIndex, int toIndex) { return mList.subList(fromIndex, toIndex); }
	@Override public Object[] toArray() { return mList.toArray(); }
	@Override public <TT> TT[] toArray(TT[] a) { return mList.toArray(a); }
}
