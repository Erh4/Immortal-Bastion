package gamePackages.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import gamePackages.controls.Command;
import gamePackages.controls.ControlsMap;
import gamePackages.entities.*;
import gamePackages.items.ConsumableItem;
import gamePackages.map.Campfire;
import gamePackages.map.Map;
import gamePackages.toolsManager.CheckOverlapping;
import gamePackages.toolsManager.Memento;
import gamePackages.toolsManager.PathfindingManager;
import gamePackages.ui.DebugUI;
import gamePackages.ui.InventoryUI;
import gamePackages.ui.PlayerUI;

import java.io.Serializable;
import java.util.*;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

/**
 * Main game class that manages the game screen.
 * Implements {@link com.badlogic.gdx.Screen}, {@link com.badlogic.gdx.InputProcessor}, and {@link java.io.Serializable}.
 * Responsible for initializing, rendering, and updating game elements.
 */
public class GameScreen implements Screen, InputProcessor, Serializable {

    protected SpriteBatch batch;
    protected Map map;

    protected ShapeRenderer shapeRenderer;
    protected BitmapFont font;

    protected OrthographicCamera camera;
    protected ExtendViewport viewport;
    protected float screenWidth;
    protected float screenHeight;
    protected float screenCenterX;
    protected float screenCenterY;
    protected Player player;
    protected float elapsedTime;
    protected float tmpElapsedTime;
    protected static ControlsMap keyMap;
    protected PlayerUI myUi;
    protected DebugUI myDebugger;

    protected List<Monster> monsters;

    protected List<Monster> loadedEntityList;
    protected List<Monster> backgroundEntityList;
    protected List<Monster> foregroundEntityList;

    protected List<Campfire> campfireList;

    protected List<ConsumableItem> allConsumableItems;
    protected List<ConsumableItem> loadedConsumableItems;

    protected PathfindingManager myPathfindingManager;
    protected List<List<Integer>> pathfindingMapPlayer;

    protected List<Rectangle> rectangleWallsList;
    protected InventoryUI playerInventoryUI;

    protected Memento playerMemento;
    protected List<Memento> monstersMemento;
    protected List<Memento> consumableItemsMemento;

    /**
     * Constructs a new GameScreen instance and initializes the game environment, including
     * screen settings, map loading, entities, and systems.
     *
     * @throws Exception if an error occurs during initialization.
     *
     * This constructor performs the following tasks:
     * - Initializes screen dimensions and center coordinates.
     * - Sets up rendering tools such as ShapeRenderer and BitmapFont.
     * - Loads the map and extracts walls, player spawn point, and monster spawn points.
     * - Validates monster spawn positions to avoid overlaps with walls.
     * - Initializes the player, monsters, campfires, and consumable items based on the map data.
     * - Configures the camera and viewport for the game world.
     * - Sets up the player's controls and user interface elements.
     * - Initializes debugging tools and pathfinding systems.
     */
    public GameScreen() throws Exception{
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenCenterX = screenWidth / 2;
        this.screenCenterY = screenHeight / 2;

        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        font.setColor(Color.WHITE);

        this.map = new Map("maps/level1.tmx");
        this.rectangleWallsList = new ArrayList<>();
        this.rectangleWallsList = this.map.getWallsRectangleList();
        MapObject playerSpawn = this.map.getPlayerSpawnList().getFirst();
        float coPlayerX = (float)playerSpawn.getProperties().get("x");
        float coPlayerY = (float)playerSpawn.getProperties().get("y");
        this.player = new Player("character/stickman_spritesheet.png",coPlayerX, coPlayerY, 24, 40, 20f);

        this.monsters = new ArrayList<>();
        this.monstersMemento = new ArrayList<>();
        for (MapObject obj : this.map.getMonsterSpawnList())
        {
            float coMonsterX = (float)obj.getProperties().get("x");
            float coMonsterY = (float)obj.getProperties().get("y");

            Rectangle tempRect = new Rectangle(coMonsterX, coMonsterY, 24, 40);
            boolean spawn_autorisation = true;
            for (Rectangle wall : rectangleWallsList) {
                if (tempRect.overlaps(wall)) {
                    System.out.println("Erreur emplacement monstre");
                    spawn_autorisation = false;
                }
            }
            if (spawn_autorisation)
            {
                //Monster temp = new Monster("default.png",coMonsterX,coMonsterY,32,32,1);

                if (obj.getName().equals("Clam_Monster")) {
                    Clam_Monster temp = new Clam_Monster(coMonsterX, coMonsterY);
                    this.monsters.add(temp);
                    this.monstersMemento.add(SaveToMemento(temp));
                }

                if (obj.getName().equals("Clam_Boss")) {
                    Clam_Boss temp = new Clam_Boss(coMonsterX, coMonsterY);
                    this.monsters.add(temp);
                    this.monstersMemento.add(SaveToMemento(temp));
                }

            }
        }
        this.campfireList = new ArrayList<>();
        for (MapObject obj : this.map.getCampfires()) {
            this.campfireList.add(new Campfire((float)obj.getProperties().get("x"), (float)obj.getProperties().get("y")));
        }

        this.loadedEntityList = new ArrayList<>();
        this.backgroundEntityList = new ArrayList<>();
        this.foregroundEntityList = new ArrayList<>();

        this.loadedConsumableItems = new ArrayList<>();
        this.allConsumableItems = new ArrayList<>();

        this.playerMemento = new Memento(player);
        playerMemento = SaveToMemento(player);

        this.consumableItemsMemento = new ArrayList<>();
        for (MapObject item : this.map.getItems()) {

            if (item.getProperties().get("type").equals("ConsumableItem")) {
                String name = item.getName();
                float x = (float)item.getProperties().get("x");
                float y = (float)item.getProperties().get("y");
                String path = (String)item.getProperties().get("spritePath");
                String statType = (String)item.getProperties().get("statType");
                float value = (float)item.getProperties().get("value");
                int quantity = (int)item.getProperties().get("quantity");
                float timing = (float)item.getProperties().get("timing");

                if (quantity > 0) {
                    ConsumableItem temp = new ConsumableItem(name, x, y, path, statType,value, quantity, timing);
                    //this.loadedConsumableItems.add(temp);
                    this.allConsumableItems.add(temp);
                    this.consumableItemsMemento.add(SaveToMemento(temp));
                }
            }
        }

        //this.loadedConsumableItems.add(new ConsumableItem("Heal Potion", 800,600, "items/heal_potion.png", "HP", 2f, 2,0f));

        this.tmpElapsedTime = 0;
        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false,0,0);
        this.camera.zoom = 5f;


        float TILE_SIZE = 32;
        float VIRTUAL_WIDTH = 60 * TILE_SIZE;
        float VIRTUAL_HEIGHT = 34 * TILE_SIZE;

        this.viewport = new ExtendViewport(VIRTUAL_WIDTH / TILE_SIZE, VIRTUAL_HEIGHT / TILE_SIZE, camera);
        this.viewport.apply();

        keyMap = new ControlsMap(this.player);
        this.myUi = new PlayerUI();

        myDebugger = new DebugUI();
        pathfindingMapPlayer = new ArrayList<>();

        for (int i = 0; i <= 80; i++) {
            List<Integer> column = new ArrayList<>();
            for (int j = 0; j <= 46; j++) {
                column.add(0);
            }
            pathfindingMapPlayer.add(column);
        }
        myPathfindingManager = new PathfindingManager(pathfindingMapPlayer);
        myPathfindingManager.setPathMap();

        this.playerInventoryUI = new InventoryUI(player, this);
    }

    @Override
    public void show(){

    }


    /**
     * Renders the game screen, including the player, monsters, items, and user interface,
     * while updating game logic and handling interactions.
     *
     * @param delta Time elapsed since the last frame, used for updating logic and animations.
     *
     * This method performs the following tasks:
     * - Clears the screen with a specified color.
     * - Updates game logic, including player effects and entity positions.
     * - Loads and sorts game objects based on their Y-index for proper rendering order.
     * - Renders the map layers, game entities, and UI elements using the SpriteBatch.
     * - Updates the player's position and handles collision detection.
     * - Updates the pathfinding map and calculates paths for monsters.
     * - Displays debugging information, such as hitboxes and pathfinding data.
     * - Renders the player's inventory UI.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f);

        this.logic(delta);
        this.loadEveryObjects();
        this.checkYIndexEntities();
        this.batch.setProjectionMatrix(camera.combined);


        this.player.updateEffects(delta);
        this.batch.begin();
        this.map.getRenderer().setView(camera);
        this.map.getRenderer().render();

        for (ConsumableItem item: loadedConsumableItems) {
            item.renderItem(batch, camera);
        }

        for (Campfire campfire: campfireList) {
            campfire.renderCampfire(batch, camera);
            campfire.renderInteraction(batch, player);
        }
        for (Monster monster : backgroundEntityList) {
            monster.monsterRender(batch, delta, camera);
        }
        this.player.playerRender(batch, delta);
        for (Monster monster: foregroundEntityList) {
            monster.monsterRender(batch, delta, camera);
        }

        this.batch.end();
        this.batch.begin();
        this.map.renderTiledMapForegroundLayers();
        this.myUi.uiRenderer(batch, player);
        this.batch.end();
        //myPathfindingManager.updatePathMap(monster, player);
        this.player.updatePosition(rectangleWallsList);
        this.myPathfindingManager.updatePathMap(player, player, rectangleWallsList);
        for (Campfire campfire: campfireList) {
            //campfire.renderCampfireHitbox(camera);
        }
        for (Monster monster : loadedEntityList) {

            try {
                myPathfindingManager.search(monster, player, rectangleWallsList);
            }
            catch (Exception e) {}
            if (!monster.getHitDetected()) {
                monster.UpdateToPlayerPosition(delta, player, myPathfindingManager);
                myDebugger.entityHitboxRenderer(camera, monster, player, myPathfindingManager);
            }
        }
        myDebugger.entityHitboxRenderer(camera, player, myPathfindingManager);


        playerInventoryUI.displayInventory(batch);


    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        this.playerInventoryUI.getStageViewport().update(width, height, true);
        this.screenCenterX = width / 2f;
        this.screenCenterY = height / 2f;
    }


    /**
     * Handles scroll input to adjust the camera's zoom level.
     *
     * @param amountX The horizontal scroll amount (not used in this method).
     * @param amountY The vertical scroll amount, indicating the scroll direction:
     *                negative for scrolling up and positive for scrolling down.
     * @return true indicating that the scroll input was handled.
     *
     * This method performs the following actions:
     * - Decreases the camera zoom level if scrolling up (amountY &lt; 0) and the zoom is greater than 2.
     * - Increases the camera zoom level if scrolling down (amountY &gt; 0) and the zoom is less than 30.
     */
    public boolean scrolled(float amountX, float amountY) {
        // amountY donne la direction du défilement : positif pour vers le bas, négatif pour vers le haut
        if (amountY < 0 && this.camera.zoom>2f) { this.camera.zoom -= 1f; }
        else if (amountY > 0 && this.camera.zoom<30f) {this.camera.zoom += 1f; }
        return true;
    }

    /**
     * Updates the camera's position to follow the player.
     *
     * This method adjusts the camera's position based on the player's current coordinates,
     * adding offsets to ensure the camera is centered appropriately relative to the player.
     * After updating the position, the camera state is refreshed.
     */
    public void movCamera() {
        this.camera.position.x = this.player.getPosX()+12;
        this.camera.position.y = this.player.getPosY()+21;
        this.camera.update();
    }


    /**
     * Processes player input and executes corresponding commands.
     *
     * @param delta Time elapsed since the last frame, used for timing-sensitive commands.
     *
     * This method performs the following tasks:
     * - Detects key presses for specific actions (e.g., movement, inventory, interactions).
     * - Maps pressed keys to commands using a key-command mapping system.
     * - Executes the appropriate command based on the input:
     *   - If only the inventory key is pressed, executes the command on the inventory UI.
     *   - Otherwise, executes the command affecting the player and monsters, passing the delta for timing adjustments.
     */
    public void input(float delta) {
        movementInput(delta);
        {
            Set<Integer> pressedKeys= new HashSet<>();
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) pressedKeys.add(Input.Keys.RIGHT);
            if (Gdx.input.isKeyJustPressed(Input.Keys.H)) pressedKeys.add(Input.Keys.H);
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) pressedKeys.add(Input.Keys.R);
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) pressedKeys.add(Input.Keys.I);
            Command command = keyMap.getCommand(pressedKeys);
            if (command != null) {

                if (pressedKeys.size() == 1 && pressedKeys.contains(Input.Keys.I)) {
                    command.execute(this.playerInventoryUI);
                }
                else {
                    keyMap.setDelta(delta);
                    command.execute(this.player, this.monsters);
                }
            }
        }

    }


    /**
     * Processes movement and action inputs from the player and updates the game state accordingly.
     *
     * @param delta Time elapsed since the last frame, used for timing-sensitive actions.
     *
     * This method performs the following tasks:
     * - Detects key presses related to movement (e.g., W, A, S, D) and specific actions (e.g., T, L, SPACE).
     * - Retrieves the corresponding command based on the pressed keys using the key-command mapping system.
     * - Handles the player's dodge state:
     *   - If the player is in a dodge state, executes the dodge logic.
     * - Executes the command if one is mapped to the current input, updating the player and monsters.
     * - Sets the player's animation to "Idle" if no movement-related input is detected.
     * - Updates the camera position to follow the player.
     */
    public void movementInput(float delta) {
        Set<Integer> pressedKeys= new HashSet<>();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) pressedKeys.add(Input.Keys.W);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) pressedKeys.add(Input.Keys.A);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) pressedKeys.add(Input.Keys.S);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) pressedKeys.add(Input.Keys.D);
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) pressedKeys.add(Input.Keys.T);
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) pressedKeys.add(Input.Keys.L);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON)) pressedKeys.add(Input.Keys.SEMICOLON);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) pressedKeys.add(Input.Keys.SPACE);
        Command command = keyMap.getCommand(pressedKeys);
        if (player.isDodgeState()) {
            player.dodge(delta);
        }
        else {
            if (command != null) {
                keyMap.setDelta(delta);
                command.execute(this.player, this.monsters);
            }
            else {
                this.player.setLastMovementAction("Idle");
                this.player.setAnimationName(this.player.getLastMovementAction());
            }
        }
        movCamera();
    }

    /**
     * Updates the game logic, including interactions, combat, and inventory management.
     *
     * @param delta Time elapsed since the last frame, used for timing-sensitive updates.
     *
     * This method performs the following tasks:
     * - Handles interactions with campfires:
     *   - Saves the current state of the player, monsters, and consumable items when interacting with a campfire.
     * - Updates monsters:
     *   - Checks for player detection.
     *   - Processes monster attacks if the player is within the attack range.
     *   - Removes monsters from the game if their health drops to zero and disposes of their resources.
     * - Checks the player's health:
     *   - Restores the game state from the last save if the player dies.
     * - Processes player input if the player is not currently attacking.
     * - Handles interactions with consumable items:
     *   - Adds items to the player's inventory if they overlap and space is available.
     *   - Removes items from the game world after they are picked up.
     */
    public void logic(float delta){


        //System.out.println(player.getPlayerInventory().getNumberOfItems());
        for (Campfire campfire: campfireList) {
            if (player.getInteractionRectangle().overlaps(campfire.getInteractionRectangle())) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    System.out.println("Sauvegarde");
                    playerMemento = SaveToMemento(player);
                    monstersMemento.clear();
                    for (Monster monster: monsters) {
                        monstersMemento.add(SaveToMemento(monster));
                    }

                    consumableItemsMemento.clear();
                    for (ConsumableItem item: allConsumableItems) {
                        consumableItemsMemento.add(SaveToMemento(item));
                    }
                }
            }
        }

        Iterator<Monster> monstersIterator = monsters.iterator();
        while (monstersIterator.hasNext()) {
            Monster monster = monstersIterator.next();
            monster.checkPlayerDetection(this.player);
            monster.setElapsedTimeAttack(monster.getElapsedTimeAttack()+delta);

            if (CheckOverlapping.overlapsRectCircle(this.player.getCollisionRectangle(), monster.getAttackRangeCircle())){
                if (!monster.attack && monster.getElapsedTimeAttack() > 1f) {

                    String direction = calculPunchDirectionForMonster(monster);
                    monster.punch(direction);
                    monster.updateAttackRectanglePosition(direction);
                    monster.attack(player);
                }
            }
            if (monster.getHealth() <= 0) {
                System.out.println("Monstre mort");
                monstersIterator.remove();
                monster.getShapeRenderer().dispose();
            }
        }


        if (player.getHealth() <= 0 ) {
            RestoreSave();
        }
        if (!this.player.attack) {
            this.input(delta);
        }

        Iterator<ConsumableItem> consumableItemIterator = allConsumableItems.iterator();
        while (consumableItemIterator.hasNext()) {
            ConsumableItem item = consumableItemIterator.next();
            if (player.getInteractionRectangle().overlaps(item.getRectangle())) {

                if (!player.getPlayerInventory().isInEquipedConsumableItems(item.getName()) && player.getPlayerInventory().getNumberOfItems() < 16) {
                    player.getPlayerInventory().addNewEquipedConsumableItem(item);
                    //System.out.println(item.getName());
                }

                if (player.getPlayerInventory().getNumberOfItems() < 16) {
                    player.getPlayerInventory().addItem(item);
                    consumableItemIterator.remove();
                }
            }
        }

    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }
    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }
    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    /**
     * Releases resources used by the game screen to free memory.
     *
     * This method performs the following tasks:
     * - Disposes of the SpriteBatch used for rendering.
     * - Disposes of the ShapeRenderer for each monster in the game.
     */
    @Override
    public void dispose() {
        batch.dispose();
        for (Entity monster : monsters) {
            monster.getShapeRenderer().dispose();
        }
    }


    /**
     * Updates and loads all game objects required for the current frame.
     *
     * This method performs the following tasks:
     * - Updates the player's loading rectangle to determine nearby objects.
     * - Loads game entities, such as monsters or NPCs, within the player's range.
     * - Loads consumable items within the player's range.
     */
    public void loadEveryObjects() {
        player.updateLoadingRectangle();
        loadEntity();
        loadConsumable();
    }


    /**
     * Loads consumable items that are within the player's loading range.
     *
     * This method performs the following tasks:
     * - Clears the list of currently loaded consumable items.
     * - Checks each consumable item to see if it overlaps with the player's loading rectangle.
     * - Adds consumable items within range to the loaded list.
     */
    public void loadConsumable() {
        loadedConsumableItems.clear();
        for (ConsumableItem item: allConsumableItems) {
            if (player.getLoadingRectangle().overlaps(item.getRectangle())) {
                loadedConsumableItems.add(item);
            }
        }
    }


    /**
     * Loads entities (monsters) that are within the player's loading range.
     *
     * This method performs the following tasks:
     * - Clears the list of currently loaded entities.
     * - Checks each monster to see if it overlaps with the player's loading rectangle.
     * - Adds monsters within range to the loaded entity list.
     */
    public void loadEntity() {
        loadedEntityList.clear();
        for (Monster monster : monsters) {
            if (player.getLoadingRectangle().overlaps(monster.getCollisionRectangle())) {
                loadedEntityList.add(monster);
            }
        }
    }

    /**
     * Sorts loaded entities (monsters) into foreground and background lists based on their Y position relative to the player.
     *
     * This method performs the following tasks:
     * - Clears the foreground and background entity lists.
     * - Compares each monster's Y position to the player's Y position.
     * - Adds monsters below the player to the foreground list and those above to the background list.
     */
    public void checkYIndexEntities() {
        this.backgroundEntityList.clear();
        this.foregroundEntityList.clear();
        for (Monster monster : loadedEntityList) {
            if (monster.getPosY() < player.getPosY()) {
                this.foregroundEntityList.add(monster);
            }
            else {
                this.backgroundEntityList.add(monster);
            }
        }
    }

    @Override public boolean keyDown(int keycode) {
        return false;
    }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }

    /**
     * Handles touch or click input events to perform actions such as attacking.
     *
     * @param screenX The x-coordinate of the touch or click on the screen.
     * @param screenY The y-coordinate of the touch or click on the screen.
     * @param pointer The pointer for the event (used for multi-touch, if applicable).
     * @param button The mouse or touch button that was pressed (e.g., left button).
     * @return true to indicate that the input was handled.
     *
     * This method performs the following tasks:
     * - Checks if the left mouse button is clicked.
     * - If the player is not attacking or dodging:
     *   - Calculates the direction of the attack based on the input coordinates.
     *   - Executes the player's punch and updates the attack rectangle's position.
     *   - Applies the attack logic to all monsters.
     */
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if (!player.attack && !player.isDodgeState()) {
                String direction = calculPunchDirection(screenX, screenY);
                this.player.punch(direction);
                player.updateAttackRectanglePosition(direction);
                player.attack(monsters);
            }
        }
        return true; }


    /**
     * Calculates the angle in degrees between two points in a 2D plane.
     *
     * @param x0 The x-coordinate of the first point.
     * @param y0 The y-coordinate of the first point.
     * @param x1 The x-coordinate of the second point.
     * @param y1 The y-coordinate of the second point.
     * @return The angle in degrees between the line connecting the two points and the x-axis.
     *
     * This method uses the arctangent function to compute the angle based on the difference
     * in coordinates between the two points.
     */
    public double getAngle(float x0, float y0, float x1, float y1) {
        double invMyTan = Math.atan((y1-y0)/(x1-x0));
        return Math.toDegrees(invMyTan);
    }


    /**
     * Determines the direction of a punch based on the screen coordinates of the input.
     *
     * @param screenX The x-coordinate of the input on the screen.
     * @param screenY The y-coordinate of the input on the screen.
     * @return A string representing the direction of the punch:
     *         - "UpAttack" for an upward attack.
     *         - "DownAttack" for a downward attack.
     *         - "LeftAttack" for a leftward attack.
     *         - "RightAttack" for a rightward attack.
     *
     * This method calculates the angle between the input point and the screen center,
     * then determines the punch direction based on the quadrant and the angle.
     */
    public String calculPunchDirection(float screenX, float screenY) {
        String direction = "";

        double deg = getAngle(this.screenCenterX, this.screenCenterY, screenX, screenY);

        if (screenX<this.screenCenterX) {  //Left side
            direction = "UpAttack";
            if (deg < -45){
                direction = "DownAttack";
            }
            else if (deg < 45) {
                direction = "LeftAttack";
            }
        }

        else {  //Right side
            direction = "DownAttack";
            if (deg < -45){
                direction = "UpAttack";
            }
            else if (deg < 45) {
                direction = "RightAttack";
            }
        }
        return direction;
    }

    /**
     * Determines the direction of a punch for a monster based on its position relative to the player.
     *
     * @param monster The monster entity whose position is being evaluated.
     * @return A string representing the direction of the punch:
     *         - "UpAttack" if the player is above the monster.
     *         - "DownAttack" if the player is below the monster.
     *         - "RightAttack" if the player is to the right of the monster.
     *         - "LeftAttack" if the player is to the left of the monster.
     *
     * The method compares the player's position to the monster's center coordinates to decide
     * the appropriate attack direction. Defaults to "UpAttack" if no other condition is met.
     */
    public String calculPunchDirectionForMonster(Entity monster) {

        if (player.getPosY() > monster.getCenterY()) {
            return "UpAttack";
        }
        if (player.getCenterY() < monster.getPosY()) {
            return "DownAttack";
        }
        if (player.getPosX()> monster.getCenterX()) {
            return "RightAttack";
        }
        if (player.getPosX()<=monster.getCenterX()) {
            return "LeftAttack";
        }
        return "UpAttack";

    }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

    /**
     * Restores the game state from previously saved mementos.
     *
     * This method performs the following tasks:
     * - Restores the player's state from the player memento and saves the updated state back to a new memento.
     * - Clears the current list of monsters and restores them from their respective mementos.
     * - Updates the monsters' mementos after restoration.
     * - Clears all consumable items and reloads them from their respective mementos.
     * - Updates the consumable items' mementos after restoration.
     *
     * The method ensures that the game state, including the player, monsters, and items,
     * is reverted to the last saved state while maintaining the integrity of the memento system.
     */
    public void RestoreSave() {
        RestoreFromMemento(playerMemento, player);
        playerMemento = SaveToMemento(player);

        monsters.clear();
        List<Memento> tempMonstersList = new ArrayList<>();
        for (Memento mem : monstersMemento) {
            Monster temp = RestoreFromMemento(mem, new Monster());
            monsters.add(temp);
            tempMonstersList.add(SaveToMemento(temp));
        }
        monstersMemento = tempMonstersList;

        allConsumableItems.clear();
        loadedConsumableItems.clear();
        List<Memento> tempItemList = new ArrayList<>();
        for (Memento mem : consumableItemsMemento) {
            ConsumableItem temp = RestoreFromMemento(mem, new ConsumableItem());
            loadedConsumableItems.add(temp);
            allConsumableItems.add(temp);
            tempItemList.add(SaveToMemento(temp));
        }

        consumableItemsMemento = tempItemList;
    }

    /**
     * Saves the player's state into a memento.
     *
     * @param player The player whose state is to be saved.
     * @return A memento containing a deep copy of the player's state.
     */
    public Memento SaveToMemento(Player player){
        return new Memento(player.deepCopy());
    }

    /**
     * Saves the monster's state into a memento.
     *
     * @param monster The monster whose state is to be saved.
     * @return A memento containing a deep copy of the monster's state.
     */
    public Memento SaveToMemento(Monster monster) {
        return new Memento(monster.deepCopy());
    }

    /**
     * Saves the consumable item's state into a memento.
     *
     * @param item The consumable item whose state is to be saved.
     * @return A memento containing a deep copy of the item's state.
     */
    public Memento SaveToMemento(ConsumableItem item) {
        return new Memento(item.deepCopy());
    }

    /**
     * Restores a consumable item's state from a memento.
     *
     * @param memento The memento containing the saved state of the consumable item.
     * @param item The consumable item to be restored.
     * @return The restored consumable item.
     */
    public ConsumableItem RestoreFromMemento(Memento memento, ConsumableItem item) {
        return memento.getConsumableMemento();
    }

    /**
     * Restores a monster's state from a memento.
     *
     * @param memento The memento containing the saved state of the monster.
     * @param monster The monster to be restored.
     * @return The restored monster.
     */
    public Monster RestoreFromMemento(Memento memento, Monster monster) {
        return memento.getClamMemento();
    }

    /**
     * Restores the player's state from a memento.
     *
     * @param memento The memento containing the saved state of the player.
     * @param player The player whose state is to be restored.
     *
     * This method also updates the player's inventory UI to reflect the restored state.
     */
    public void RestoreFromMemento(Memento memento, Player player) {
        this.player = memento.getPlayerMemento();
        this.playerInventoryUI = new InventoryUI(player, this);

    }
}
