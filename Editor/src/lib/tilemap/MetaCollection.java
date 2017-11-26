package lib.tilemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.scene.paint.Color;
import lib.ColorGenerator;

public class MetaCollection
{
	private final Map<String, Color> mMetas;
	private final ColorGenerator mColorGenerator;
	
	public MetaCollection()
	{
		mMetas = new HashMap<>();
		mColorGenerator = new ColorGenerator();
	}
	
	public void addMetas(String ... ss)
	{
		for(String s : ss)
		{
			addMeta(s);
		}
	}
	
	public void addMeta(String s)
	{
		if(!mMetas.containsKey(s))
		{
			mMetas.put(s, mColorGenerator.getNextColor());
		}
	}
	
	public Color getColor(String id)
	{
		return mMetas.get(id);
	}
	
	public Set<Entry<String, Color>> entrySet()
	{
		return mMetas.entrySet();
	}
}
