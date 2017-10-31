package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import javafx.scene.image.Image;

public class Loader
{
	private String mRoot;
	
	public Loader(File root)
	{
		mRoot = root.getAbsolutePath();
	}
	
	public File getFile(String ... ps)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(mRoot);
		
		for(String p : ps)
		{
			sb.append(File.separator).append(p);
		}
		
		return new File(sb.toString());
	}
	
	public JsonValue loadData(String type, String id)
	{
		try
		{
			String fn = mRoot + "/data/" + type + "/" + id + ".json";
			
			File f = new File(fn + ".bak");
			
			if(f.exists()) f.delete();
			
			Files.copy((new File(fn)).toPath(), f.toPath());
			
			return Json.parse(new FileReader(fn));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Image loadMedia(String type, String fn)
	{
		return new Image("file:" + mRoot + "/media/" + type + "/" + fn);
	}
}
