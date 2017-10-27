package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
	
	public JsonValue loadData(String type, String id)
	{
		try
		{
			return Json.parse(new FileReader(mRoot + "/data/" + type + "/" + id + ".json"));
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
