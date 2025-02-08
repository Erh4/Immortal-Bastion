package gamePackages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gamePackages.entities.Player;
import gamePackages.items.ConsumableItem;
import gamePackages.items.Inventory;
import gamePackages.items.Item;
import gamePackages.screens.GameScreen;

public class InventoryUI {

    protected Player player;
    protected Inventory inventory;
    protected Table table;
    protected Table inventoryTable;
    protected Stage stage;
    protected Drawable backgroundSprite;

    protected FreeTypeFontGenerator fontGenerator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    protected BitmapFont uiFont;

    protected boolean isInvDrawn = false;
    protected Inventory inv;

    public InventoryUI(Player player, GameScreen gameScreen) {
        this.player = player;
        this.inventory = player.getPlayerInventory();
        this.inventoryTable = new Table();
        this.stage = new Stage(new ScreenViewport());
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameScreen);
        Gdx.input.setInputProcessor(multiplexer);

        this.backgroundSprite = new TextureRegionDrawable(new TextureRegion(new Texture("ui/inventory_background.png")));

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bokor-Regular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;
        fontParameter.borderColor = new Color(0.0F, 0.0F, 0.0F, 1.0F);
        fontParameter.borderWidth = 1;
        uiFont = fontGenerator.generateFont(fontParameter);

    }

    protected void itemClick(Item i) throws Exception{
        System.out.println("itemClick "+i.getName());
        if (i.getType().equals("ConsumableItem")){

            ConsumableItem tempItem = (ConsumableItem) i;

            tempItem.consumItem(player);

            //System.out.println("Quantity :"+((ConsumableItem) i).getQuantity());
        }

        else if (this.inventory.isEquipped(i)){
            this.inventory.removeEquipment(i.getName(),player);
        }

        else { this.inventory.setEquipment(i,player); }
        updateInvTable();
        //refreshInventory();
    }

    protected void updateInvTable() {

        inventoryTable.clear(); // on supprime bien tous les éléments avant pour éviter les problèmes
        inventoryTable.left().top();

        int ITEMS_ROW = 6;
        int rowProgression = 0;
/*
        for (Item e : this.inventory.getEquipment().values()) {
            Texture texture = e.getItemTexture();
            Image image = new Image(texture);
            image.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        try {
                            itemClick(e);
                        } catch (Exception _) {
                        }
                    }
                    return true;
                }
            });
            inventoryTable.add(image).size(48, 48).pad(10);
        }

        inventoryTable.row().top();*/

        for (Item v : this.inventory.getItems().values()) {
            if (!this.inventory.isEquipped(v)) {
                Texture texture = v.getItemTexture();
                Image image = new Image(texture);

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = uiFont;

                int itemQuantity = ((ConsumableItem) v).getQuantity();
                Label quantityLabel = new Label("x"+itemQuantity, labelStyle);

                // Utilisation d'une Table pour positionner le texte
                Table textTable = new Table();
                textTable.add(quantityLabel).expand().bottom().right();
                textTable.setFillParent(true);

                image.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (button == Input.Buttons.LEFT) {
                            try {
                                itemClick(v);
                            } catch (Exception _) {
                            }
                        }
                        return true;
                    }
                });

                Stack itemStack = new Stack();
                itemStack.add(image); // Ajout de l'image en bas
                itemStack.add(textTable); // Ajout du tableau avec le label

                // Ajout du Stack à la table
                inventoryTable.add(itemStack).size(96, 96).pad(0);

                //inventoryTable.add(itemStack).size(96, 96).pad(0);
                rowProgression++;
                if (rowProgression % ITEMS_ROW == 0) {
                    inventoryTable.row();
                }
            }
        }
    }

    public void drawInventory() {

        inventoryTable.setFillParent(false); //on ne veut pas que l'inventaire prenne toute la place
        inventoryTable.setDebug(false);

        inventoryTable.setBackground(this.backgroundSprite);
        inventoryTable.setSize(6*96, 3*96);
        inventoryTable.setPosition(Gdx.graphics.getWidth() - inventoryTable.getWidth() - 20,
                Gdx.graphics.getHeight() - inventoryTable.getHeight() - 20);
        //inventoryTable.pad(5);

        updateInvTable();
        if (stage.getActors().size == 0) {
            stage.addActor(inventoryTable);
        }
        inventoryTable.setZIndex(1000); //pour être bien sûr qu'il apparaît tout en haut

        inventoryTable.validate();

    }

    public void displayInventory(SpriteBatch batch) {
        if (isInvDrawn){

            batch.begin();

            //batch.draw(backgroundSprite, Gdx.graphics.getWidth() - inventoryTable.getWidth() - 20,Gdx.graphics.getHeight()-backgroundSprite.getHeight()*2-20, backgroundSprite.getWidth()*2, backgroundSprite.getHeight()*2);
            drawInventory();
            stage.act();
            stage.draw();

            batch.end();
            //System.out.println(inv.getItems().keySet());
        }
    }


    public void setInvDrawn(boolean bool) {
        isInvDrawn = bool;
    }

    public boolean getIsInvDrawn() {return isInvDrawn;}

    public Viewport getStageViewport() {return this.stage.getViewport();}

}
