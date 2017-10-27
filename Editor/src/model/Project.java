package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project
{
	public final java.util.Map<String, List<String>> content;
	
	public Project(String dir)
	{
		content = new HashMap<>();
		
		for(String type : new String[] {"map", "tilemap", "tileset"})
		{
			content.put(type, readIDs(dir + File.separator + type));
		}
	}
	
	private static List<String> readIDs(String path)
	{
		List<String> r = new ArrayList<>();
		
		for(File f : (new File(path)).listFiles((dir, fn) -> fn.toLowerCase().endsWith(".json")))
		{
			r.add(f.getName().replaceAll("\\.json$", ""));
		}
		
		return r;
	}
}
