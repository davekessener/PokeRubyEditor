package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

import javafx.scene.image.Image;

public class Loader
{
	private String mRoot;
	private final java.util.Map<String, Image> mImages;
	private final List<File> mJSON;
	
	public Loader(File root)
	{
		mRoot = root.getAbsolutePath();
		mImages = new HashMap<>();
		mJSON = new ArrayList<>();
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
	
	public void write(File f, JsonValue v)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			v.writeTo(bw, WriterConfig.PRETTY_PRINT);
			bw.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public JsonValue loadData(String type, String id)
	{
		String fn = mRoot + "/data/" + type + "/" + id + ".json";
		
		try
		{
			File f = new File(fn + ".bak");
			
			if(!mJSON.contains(f))
			{
				if(f.exists()) f.delete();
				
				Files.copy((new File(fn)).toPath(), f.toPath());
				
				mJSON.add(f);
			}
			
			return Json.parse(new FileReader(fn));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Image loadMedia(String type, String fn)
	{
		String path = "file:" + mRoot + "/media/" + type + "/" + fn;
		Image r = mImages.get(path);
		
		if(r == null)
		{
			mImages.put(path, r = new Image(path));
		}
		
		return r;
	}
}
