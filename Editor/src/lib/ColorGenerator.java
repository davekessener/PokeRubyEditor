package lib;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.scene.paint.Color;

public class ColorGenerator
{
	private Deque<Color> mColors;
	private int mStep;
	
	public ColorGenerator()
	{
		mStep = 255;
		mColors = new ArrayDeque<>();
	}
	
	public Color getNextColor()
	{
		if(mColors.isEmpty())
		{
			generate();
		}
		
		return mColors.poll();
	}
	
	private void generate()
	{
		for(int r = 0 ; r < 256 ; r += mStep)
		{
			for(int g = 0 ; g < 256 ; g += mStep)
			{
				for(int b = 0 ; b < 256 ; b += mStep)
				{
					pushColor(r, g, b);
				}
			}
		}
		
		mStep /= 2;
	}
	
	private void pushColor(int r, int g, int b)
	{
		if((r == 0 && g == 0 && b == 0) || (r == 255 && g == 255 && b == 255)) return;
		
		mColors.push(Color.rgb(r, g, b));
	}
}
