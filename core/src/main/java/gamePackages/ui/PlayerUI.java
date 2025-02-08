package gamePackages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import gamePackages.entities.Player;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import gamePackages.items.ConsumableItem;
import gamePackages.items.Item;
import gamePackages.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerUI {

    protected Sprite uiSprite;
    protected Texture uiTexture;
    protected FreeTypeFontGenerator fontGenerator;
    protected FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    protected BitmapFont uiFont;
    protected Sprite uiActiveConsumable;



    protected Sprite fullHealPoint;
    protected Sprite midHealPoint;
    protected Sprite emptyHealPoint;
    protected Sprite fullHealPointPlus;
    protected Sprite midHealPointPlus;

    protected ArrayList<Sprite> healPointsSpritesList;
    protected Texture[] healthBar;

    protected HashMap<String, Sprite> command_images;

    public PlayerUI() {
        this.healPointsSpritesList = new ArrayList<Sprite>();
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bokor-Regular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;
        fontParameter.borderColor = new Color(0.0F, 0.0F, 0.0F, 1.0F);
        fontParameter.borderWidth = 1;
        uiFont = fontGenerator.generateFont(fontParameter);
        uiActiveConsumable = new Sprite(new Texture("ui/active_consumable.png"));
        changeFontSize(24);

        fullHealPoint = new Sprite(new Texture("ui/heal_point.png"));
        midHealPoint = new Sprite(new Texture("ui/half_heal_point.png"));
        emptyHealPoint = new Sprite(new Texture("ui/empty_heal_point.png"));
        fullHealPointPlus = new Sprite(new Texture("ui/heal_point_plus.png"));
        midHealPointPlus = new Sprite(new Texture("ui/half_heal_point_plus.png"));

        command_images = new HashMap<>();
        command_images.put("movements", new Sprite(new Texture("ui/keys/movement_keys.png")));
        /*command_images.put("d", new Sprite(new TextureRegion(new Texture("ui/keys/keys_control.png"), 32, 0, 32,32)));
        command_images.put("q", new Sprite(new TextureRegion(new Texture("ui/keys/keys_control.png"), 64, 0, 32,32)));
        command_images.put("z", new Sprite(new TextureRegion(new Texture("ui/keys/keys_control.png"), 96, 0, 32,32)));*/
    }

    public void UpdateHealthList(Player player) {
        this.healPointsSpritesList.clear();
        float bonusHealthPoints = 0;
        //System.out.println(player.getListOfEffect());
        if (player.getListOfEffect().get("Max Heal Potion") != null) {
            bonusHealthPoints = player.getListOfEffect().get("Max Heal Potion").getValue();
        }
        UpdateNormalLife(player, bonusHealthPoints);
        UpdateExtraLife(player, bonusHealthPoints);
        UpdateEmptyLife(player);


    }
    public void UpdateNormalLife(Player player, float bonusHealthPoints) {
        float normalHP;
        if (player.getHealth() < player.getMaxHealth() - bonusHealthPoints) {
            normalHP = (float) Math.floor(player.getHealth());
        }
        else {
            normalHP = (float) Math.floor(player.getMaxHealth() - bonusHealthPoints);
        }

        for (int i = 0; i < (int) normalHP; i++) {
            this.healPointsSpritesList.add(fullHealPoint);
        }

        if (bonusHealthPoints == 0) {
            if (player.getHealth() - normalHP  > 0) {
                this.healPointsSpritesList.add(midHealPoint);
            }
        }
        else {
            if (player.getHealth() < player.getMaxHealth()-bonusHealthPoints &&  player.getHealth() - normalHP > 0) {
                this.healPointsSpritesList.add(midHealPoint);
            }
        }
    }
    public void UpdateExtraLife(Player player, float bonusHealthPoints) {
        float extraHP = 0;
        if (player.getHealth()> player.getMaxHealth() - bonusHealthPoints) {
            extraHP = (float) Math.floor(player.getHealth() - player.getMaxHealth()+bonusHealthPoints);
        }
        for (int i =0;i< (int)extraHP;i++ ) {
            this.healPointsSpritesList.add(fullHealPointPlus);
        }

        if (player.getHealth() > player.getMaxHealth() - bonusHealthPoints) {

            if (player.getMaxHealth() - player.getHealth() - extraHP > 0) {
                this.healPointsSpritesList.add(midHealPointPlus);
            }
        }
    }
    public void UpdateEmptyLife(Player player) {
        int listSize = healPointsSpritesList.size();
        for (int i = 0; i < (int) Math.ceil(player.getMaxHealth()) - listSize; i++) {
            this.healPointsSpritesList.add(emptyHealPoint);
        }
    }
    public void displayHealthBar(SpriteBatch batch, Player player) {
        int index = 1;
        for(Sprite healPointSprite: this.healPointsSpritesList) {
            batch.draw(healPointSprite, -(Gdx.graphics.getWidth()/2f)+32*index, (Gdx.graphics.getHeight()/2f)-48, 32, 32);
            index++;
        }
    }

    public void displayPlayerEffects(SpriteBatch batch, Player player) {
        if (fontParameter.color != Color.WHITE)
        {
            changeFontColor(Color.WHITE);
        }
        if (player.getListOfEffect() != null) {
            int index = 1;
            for (ConsumableItem effect : player.getListOfEffect().values()) {
                batch.draw(effect.getItemSprite(), -(Gdx.graphics.getWidth()/2f)+32, (Gdx.graphics.getHeight()/2f)-64-48*index, 32,32);
                uiFont.draw(batch, (int) effect.getTiming() + "s", -(Gdx.graphics.getWidth()/2f)+64, (Gdx.graphics.getHeight()/2f)-40-48*index);
                index++;
            }
        }
    }

    public void displayActiveConsumable(SpriteBatch batch, Player player) {
        batch.draw(uiActiveConsumable, -(Gdx.graphics.getWidth()/2f),-(Gdx.graphics.getHeight()/2f));

        uiFont.draw(batch, "USE",-(Gdx.graphics.getWidth()/2f)+42, -(Gdx.graphics.getHeight()/2f)+160);
        uiFont.draw(batch, "NEXT",-(Gdx.graphics.getWidth()/2f)+42, -(Gdx.graphics.getHeight()/2f)+128);
        uiFont.draw(batch, "INVENTORY", -(Gdx.graphics.getWidth()/2f)+150, -(Gdx.graphics.getHeight()/2f)+32);
        batch.draw(command_images.get("movements"), Gdx.graphics.getWidth()/2f-120,-Gdx.graphics.getHeight()/2f+20);
        if (!player.getPlayerInventory().getEquipedConsumableItems().isEmpty()) {
            ConsumableItem item = player.getPlayerInventory().getActiveEquipedConsumableItem();
            /*if (item.getQuantity() == 0 && fontParameter.color != Color.RED) {
                changeFontColor(Color.RED);
            }
            if (item.getQuantity() != 0 && fontParameter.color != Color.WHITE) {
                changeFontColor(Color.WHITE);
            }*/
            Sprite activeItem = item.getItemSprite();
            batch.draw(activeItem,-(Gdx.graphics.getWidth()/2f)+16,-(Gdx.graphics.getHeight()/2f)+16, 64, 64);
            uiFont.draw(batch, "x" + item.getQuantity(), -(Gdx.graphics.getWidth()/2f)+56, -(Gdx.graphics.getHeight()/2f)+48);
        };


    }

    public void changeFontSize(int size) {
        fontParameter.size = size;
        uiFont = fontGenerator.generateFont(fontParameter);
    }

    public void changeFontColor(Color color) {
        fontParameter.color = color;
        uiFont = fontGenerator.generateFont(fontParameter);
    }

    public void uiRenderer(SpriteBatch batch, Player player) {
        UpdateHealthList(player);
        batch.setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);

        displayHealthBar(batch, player);
        displayPlayerEffects(batch, player);
        displayActiveConsumable(batch, player);
        /*if (fontParameter.color != Color.WHITE) {
            changeFontColor(Color.WHITE);
        }*/
        uiFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), (Gdx.graphics.getWidth()/2f)-100, (Gdx.graphics.getHeight()/2f)-30);
    }
}
