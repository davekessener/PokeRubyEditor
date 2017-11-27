package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class CacheUtils
{
	private CacheUtils() { }
	
	public static <T extends Node & CachedUI> void RenderPeriodically(Duration d, T ui)
	{
		final Timeline t = renderTimeline(d, ui);
		
		t.setCycleCount(Animation.INDEFINITE);
		
		ui.sceneProperty().addListener((ob, o, n) -> {
			if(n == null)
			{
				t.stop();
			}
			else
			{
				t.play();
			}
		});
	}
	
	public static void ScheduleRenderIn(Duration d, CachedUI ui)
	{
		renderTimeline(d, ui).play();
	}
	
	private static Timeline renderTimeline(Duration d, CachedUI ui)
	{
		return new Timeline(new KeyFrame(d, ae -> {
			if(ui.isDirty())
			{
				ui.render();
			}
		}));
	}
}
