package view.tilemap;

import java.util.Map.Entry;

import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import lib.MetaCollection;
import view.TilemapCanvas;
import view.UI;

public class TabMeta implements UI
{
	private BorderPane mRoot;
	private MetaCollection mMetas;
	private ListView<MetaField> mMetaList;
	private MetaSelectCallback mCallback;
	private TilemapCanvas mMetamap;
	
	public TabMeta(String[][] metas, int ts, TilemapCanvas.TileRenderer r)
	{
		int w = metas.length;
		int h = metas[0].length;
		
		mRoot = new BorderPane();
		mMetas = new MetaCollection();
		mMetaList = new ListView<>();
		
		for(String[] ms : metas)
		{
			mMetas.addMetas(ms);
		}
		
		for(Entry<String, Color> e : mMetas.entrySet())
		{
			mMetaList.getItems().add(new MetaField(e.getKey(), e.getValue()));
		}
		
		mMetaList.setEditable(false);
		mMetaList.prefHeightProperty().bind(mRoot.heightProperty());
		mMetaList.getSelectionModel().select(0);
		mMetaList.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> onSelected(n.getMetaID()));
		
		mMetamap = new TilemapCanvas(w, h, ts, r);
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(mMetaList);
		mRoot.setRight(sp);
		
		sp = new ScrollPane();
		sp.setContent(mMetamap);
		mRoot.setCenter(sp);
		
		mMetamap.draw();
	}
	
	public Color getColorOfMeta(String id) { return mMetas.getColor(id); }
	public void setOnMetaSelected(MetaSelectCallback cb) { mCallback = cb; }
	public void select(String meta)
	{
		for(int i = 0 ; i < mMetaList.getItems().size() ; ++i)
		{
			if(mMetaList.getItems().get(i).getMetaID().equals(meta))
			{
				mMetaList.getSelectionModel().select(i);
				return;
			}
		}
		
		mMetas.addMeta(meta);
		mMetaList.getItems().add(new MetaField(meta, mMetas.getColor(meta)));
		mMetaList.getSelectionModel().select(mMetaList.getItems().size() - 1);
	}
	
	protected void onSelected(String meta)
	{
		if(mCallback != null)
		{
			mCallback.onSelectMeta(meta);
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface MetaSelectCallback { public abstract void onSelectMeta(String id); }
}
