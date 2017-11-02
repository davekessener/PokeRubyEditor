package view.map;

import java.util.HashMap;
import java.util.Map;

public class PreviewManager
{
	private final Map<String, Preview> mPreviews = new HashMap<>();
	
	public Preview getPreview(String id)
	{
		Preview p = mPreviews.get(id);
		
		if(p == null)
		{
			mPreviews.put(id, p = Preview.Create(id));
			p.draw();
		}
		
		return p;
	}
}
