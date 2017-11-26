package view.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lib.Utils;
import lib.misc.Vec2;
import lib.misc.pair.Pair;
import lib.misc.pair.StrictPair;
import model.Event.Type;
import model.Event;
import view.TiledCanvas;

public class EventCanvas extends TiledCanvas
{
	private final List<Entity> mEntities;
	private Consumer<String> mOnDrag;
	private Consumer<Vec2> mOnDrop;
	private Pair<Vec2, Entity> mDraggedEntity;
	
	public EventCanvas(int w, int h, int ts)
	{
		super(w, h, ts);
		
		mEntities = new ArrayList<>();
		
		this.setTrueClear(true);
	}
	
	public void add(Event event)
	{
		Utils.reject(mEntities, e -> e.event.getID().equals(event.getID()));
		
		if(mDraggedEntity == null || !mDraggedEntity.getSecond().event.getID().equals(event.getID()))
		{
			mEntities.add(new Entity(event));
			
			draw();
		}
	}
		
	public void clear()
	{
		mEntities.clear();
		
		draw();
	}
	
	public void setOnDrag(Consumer<String> cb) { mOnDrag = cb; }
	public void setOnDrop(Consumer<Vec2> cb) { mOnDrop = cb; }
	
	@Override
	protected void drawTiles(GraphicsContext gc)
	{
		mEntities.forEach(e -> Entity.Draw(gc, e.event.getLocation(), e.color, getTileSize()));
		
		if(mDraggedEntity != null)
		{
			Entity.Draw(gc, mDraggedEntity.getFirst(), mDraggedEntity.getSecond().color, getTileSize());
		}
	}
	
	@Override
	protected void mousePressed(MouseEvent me)
	{
		translate(me).ifPresent(p -> {
			mEntities.stream().filter(e -> e.event.getLocation().equals(p)).findAny().ifPresent(e -> {
				this.setSelected(p);
				
				mEntities.remove(e);
				mDraggedEntity = new StrictPair<>(p, e);
				
				if(mOnDrag != null)
				{
					mOnDrag.accept(e.event.getID());
				}
				
				draw();
			});
		});
	}

	@Override
	protected void mouseDrag(MouseEvent e)
	{
		if(mDraggedEntity != null)
		{
			updateDragPosition(translate(e).orElse(mDraggedEntity.getSecond().event.getLocation()));
		}
	}
	
	@Override
	protected void mouseReleased(MouseEvent me)
	{
		if(mDraggedEntity != null)
		{
			Vec2 p = mDraggedEntity.getFirst();
			Event e = mDraggedEntity.getSecond().event;
			
			mDraggedEntity = null;
			
			add(e);
			
			if(mOnDrop != null && !e.getLocation().equals(p))
			{
				mOnDrop.accept(p);
			}
			
			draw();
		}
	}
	
	private void updateDragPosition(Vec2 p)
	{
		if(!mDraggedEntity.getFirst().equals(p))
		{
			mDraggedEntity.setFirst(p);
			
			draw();
		}
	}
	
	private static class Entity
	{
		public final Event event;
		public final Color color;
		
		public Entity(Event event)
		{
			this.event = event;
			this.color = COLORS.get(event.getArgument().getType());
		}
		
		public static void Draw(GraphicsContext gc, Vec2 p, Color c, int ts)
		{
			gc.setFill(c.interpolate(Color.TRANSPARENT, 0.5));
			gc.fillRect(p.getX() * ts, p.getY() * ts, ts, ts);
		}
		
		private static final Map<Type, Color> COLORS = new HashMap<>();
		
		static
		{
			COLORS.put(Type.TEXT, Color.RED);
			COLORS.put(Type.NPC, Color.GREENYELLOW);
			COLORS.put(Type.WARP, Color.VIOLET);
		}
	}
}
