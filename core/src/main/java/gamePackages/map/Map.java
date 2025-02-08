package gamePackages.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game map loaded from a Tiled map file.
 *
 * The `Map` class provides methods to manage and access various layers of the map,
 * including player spawn points, monsters, walls, items, and campfires.
 */
public class Map {
    protected TiledMap tiledMap;
    protected OrthogonalTiledMapRenderer renderer;
    protected MapLayers layers;
    protected MapLayer playerLayer;
    protected MapLayer monstersLayer;
    protected MapLayer wallsLayer;
    protected MapLayer foregroundLayer;
    protected MapLayer itemsLayer;
    protected MapLayer campfiresLayer;
    protected MapLayers backgroundLayers;
    protected int[] foregroundLayersIndex;
    /**
     * Constructs a new `Map` object by loading a Tiled map file.
     *
     * @param tiledMapFile The path to the Tiled map file (TMX format).
     *
     * This constructor initializes the map and assigns specific layers
     * based on their names in the Tiled editor.
     */
    public Map(String tiledMapFile) {
        this.tiledMap = new TmxMapLoader().load(tiledMapFile);
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.layers = this.tiledMap.getLayers();
        this.playerLayer = new MapLayer();
        this.monstersLayer = new MapLayer();
        this.wallsLayer = new MapLayer();
        this.foregroundLayer = new MapLayer();
        this.itemsLayer = new MapLayer();
        this.campfiresLayer = new MapLayer();
        this.backgroundLayers=new MapLayers();
        this.foregroundLayersIndex=new int[1];

        for (MapLayer layer : this.layers) {
            if (layer.getName().equals("player")) {
                this.playerLayer = layer;
            }
            if (layer.getName().equals("monsters")) {
                this.monstersLayer = layer;
            }
            if (layer.getName().equals("wall_collisions")) {
                this.wallsLayer = layer;
            }
            if (layer.getName().equals("foreground_layer")) {
                this.foregroundLayersIndex[0]=getLayerIndex("foreground_layer");
                this.foregroundLayer = layer;
            }
            if (layer.getName().equals("Items")) {
                this.itemsLayer = layer;
            }
            if (layer.getName().equals("campfires")) {
                this.campfiresLayer = layer;
            }
/*
            if (!layer.getName().equals("foreground_layer")) {
                this.backgroundLayers.add(layer);
            }*/
        }
    }


    /**
     * Gets the renderer for the map.
     *
     * @return The `OrthogonalTiledMapRenderer` used to draw the map.
     */
    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    /**
     * Gets the list of player spawn points.
     *
     * @return A list of `MapObject` objects representing player spawn points.
     */
    public List<MapObject> getPlayerSpawnList() {
        List<MapObject> spawnsList =  new ArrayList<>();
            for (MapObject object : this.playerLayer.getObjects()) {
                if (object.getName().equals("player_spawn")) {
                    spawnsList.add(object);
                }
            }
        return spawnsList;
    }


    /**
     * Gets the list of monster spawn points.
     *
     * @return A list of `MapObject` objects representing monster spawn points.
     */
    public List<MapObject> getMonsterSpawnList() {
        List<MapObject> spawnsList =  new ArrayList<>();
        for (MapObject object : this.monstersLayer.getObjects()) {
            if (object.getProperties().get("type").equals("monster_spawn")) {
                spawnsList.add(object);
            }
        }
        return spawnsList;
    }

    /**
     * Gets the list of wall collision rectangles.
     *
     * @return A list of `Rectangle` objects representing wall collision areas.
     */
    public List<Rectangle> getWallsRectangleList() {
        List<Rectangle> rectanglesList =  new ArrayList<>();
        for (MapObject object : this.wallsLayer.getObjects()) {
            Rectangle tempObjRect = ((RectangleMapObject) object).getRectangle();
            rectanglesList.add(tempObjRect);
        }
        return rectanglesList;
    }

    /**
     * Gets the width of the map in tiles.
     *
     * @return The width of the map in tiles.
     */
    public int getMapWidth() {
        return (int)this.tiledMap.getProperties().get("width");
    }

    /**
     * Gets the height of the map in tiles.
     *
     * @return The height of the map in tiles.
     */
    public int getMapHeight() {
        return (int)this.tiledMap.getProperties().get("height");
    }
/*
    public void renderTiledMapBackgroundLayers(OrthographicCamera camera) {
        TiledMap tm = this.tiledMap;
        getRenderer().getBatch().begin();
        getRenderer().setView(camera);
        for (MapLayer layer : this.layers) {
            if (!layer.getName().equals("foreground_layer")) {
                getRenderer().renderMapLayer(layer);
            }

        }
        getRenderer().getBatch().end();
    }
*/


    /**
     * Renders the foreground layers of the map.
     */
    public void renderTiledMapForegroundLayers() {
        this.getRenderer().render(foregroundLayersIndex);
    }


    public int getLayerIndex(String layerName) {
        for (int i = 0; i < this.layers.size(); i++) {
            MapLayer layer = this.layers.get(i);
            if (layer.getName().equals(layerName)) {
                return i; // Retourne l'indice si le nom correspond
            }
        }
        return -1; // Retourne -1 si aucun layer ne correspond
    }

    /**
     * Gets the list of items on the map.
     *
     * @return A list of `MapObject` objects representing items on the map.
     */
    public List<MapObject> getItems(){
        List<MapObject> itemsList =  new ArrayList<>();
        for (MapObject object : this.itemsLayer.getObjects()) {
            itemsList.add(object);
        }
        return itemsList;
    }

    /**
     * Gets the list of campfires on the map.
     *
     * @return A list of `MapObject` objects representing campfires on the map.
     */
    public List<MapObject> getCampfires(){
        List<MapObject> campfiresList =  new ArrayList<>();
        for (MapObject object : this.campfiresLayer.getObjects()) {
            campfiresList.add(object);
        }
        return campfiresList;
    }

}
