package lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import controller.EditorController;
import lib.action.Action;
import lib.action.BasicAction;
import lib.misc.Recursive;
import lib.misc.Vec2;
import model.Direction;
import model.JsonModel;
import model.Map;
import model.Tilemap;
import model.Tileset;

public class Utils
{
	public static <T> T with(T o, Consumer<T> f)
	{
		f.accept(o);
		
		return o;
	}
	
	public static <T> T transform(T o, Function<T, T> f)
	{
		return f.apply(o);
	}
	
	public static <T> void ifPresent(T o, Consumer<T> f)
	{
		if(o != null)
		{
			f.accept(o);
		}
	}
	
	public static <K, V> void reject(java.util.Map<K, V> map, Predicate<V> f)
	{
		map.entrySet().stream().filter(e -> f.test(e.getValue())).collect(Collectors.toSet()).forEach(e -> map.remove(e.getKey()));
	}
	
	public static <T> void reject(List<T> list, Predicate<T> f)
	{
		list.stream().filter(f).collect(Collectors.toSet()).forEach(e -> list.remove(e));
	}
	
	public static Action ReversableAction(Object subject, String attrName, Object value)
	{
		try
		{
			Class<?> c = subject.getClass();
			Method getter = c.getMethod("get" + attrName);
			Method setter = Arrays.stream(c.getMethods()).filter(m -> m.getName().equals("set" + attrName)).findAny().get();
			
			Object old = getter.invoke(subject);
			
			return new BasicAction(NoException(() -> setter.invoke(subject, old)), NoException(() -> setter.invoke(subject, value)));
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Action Append(Action a, Runnable r)
	{
		return new BasicAction(() -> { a.undo(); r.run(); }, () -> { a.redo(); r.run(); });
	}
	
	public static interface ThrowingRunnable { public abstract void run() throws Throwable; }
	
	public static Runnable NoException(ThrowingRunnable runner) { return NoException(runner, e -> { throw new RuntimeException(e); }); }
	public static Runnable NoException(ThrowingRunnable runner, Consumer<Throwable> handler)
	{
		return () -> {
			try
			{
				runner.run();
			}
			catch(Throwable e)
			{
				handler.accept(e);
			}
		};
	}
	
	public static <T> Set<Vec2> findAllAdjacent(Vec2 start, Function<Vec2, T> lookup)
	{
		final Recursive<Consumer<Vec2>> f = new Recursive<>();
		final Set<Vec2> r = new HashSet<>();
		final T t = lookup.apply(start);
		
		f.impl = p -> {
			if(!r.contains(p) && t.equals(lookup.apply(p)))
			{
				r.add(p);
				
				for(Direction d : Direction.values())
				{
					f.impl.accept(p.add(d.distance));
				}
			}
		};
		
		f.impl.accept(start);
		
		return r;
	}
	
	public static Map loadMap(String id)
	{
		return loadJSON(new Map(), "map", id);
	}
	
	public static Tilemap loadTilemap(String id)
	{
		return loadJSON(new Tilemap(), "tilemap", id);
	}
	
	public static Tileset loadTileset(String id)
	{
		return loadJSON(new Tileset(), "tileset", id);
	}
	
	public static <T extends JsonModel> T loadJSON(T json, String type, String id)
	{
		return with(json, e -> e.load(EditorController.Instance.getLoader().loadData(type, id)));
	}
	
	public static String capitalize(String s)
	{
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	private Utils() { }
}
