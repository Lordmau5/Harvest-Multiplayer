package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.farmable.crops.CropRegistry;
import com.lordmau5.harvest.client.farmable.crops.CropState;
import com.lordmau5.harvest.client.farmable.crops.ICrop;
import com.lordmau5.harvest.client.farmable.seeds.ISeeds;
import com.lordmau5.harvest.client.farmable.seeds.TomatoSeeds;
import com.lordmau5.harvest.client.farmable.seeds.TurnipSeeds;
import com.lordmau5.harvest.client.floor.Farmland;
import com.lordmau5.harvest.client.objects.Entity;
import com.lordmau5.harvest.client.objects.IPickupable;
import com.lordmau5.harvest.client.objects.doubleTile.BigStone;
import com.lordmau5.harvest.client.objects.singleTile.Grass;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.*;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends BasicGame {

    public Client() {
        super("Harvest Multiplayer");
    }

    //--------------------------------------------------------

    //TiledMap farmLandTest;
    Rectangle outerRight = new Rectangle(32 * 16 + 1, 0, 1, 32 * 16 + 1);
    Rectangle outerBottom = new Rectangle(0, 32 * 16 + 1, 32 * 16 + 1, 1);

    String[] pAnims = {"stand", "carryStill", "walkUp", "walkDown", "walkLeft", "runUp", "runDown", "runLeft", "carryUp", "carryDown", "carryLeft", "carryUpRun", "carryDownRun", "carryLeftRun"};
    Map<String, Animation> playerAnims = new HashMap<>();

    String[] farmLandNames = {"normal", "tilled", "watered"};
    Map<String, Image> farmLandImages = new HashMap<>();
    Image seeds;
    Image crop, cropWilted;
    Map<String, Image> farmableImages = new HashMap<>();

    int farmWidth = 32;
    int farmHeight = 32;
    Map<Tile, Farmland> farmLand = new HashMap<>();

    public static void main(String args[]) {
        AppGameContainer game;
        try {
            game = new AppGameContainer(new Client());
            game.setDisplayMode(800, 600, false);
            game.setShowFPS(false);
            game.setUpdateOnlyWhenVisible(false);
            game.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    Entity[] entities = new Entity[] {new Grass(), new BigStone()};
    Map<String, Image> objectTextures = new HashMap<>();
    Map<Tile, Entity> objects = new HashMap<>();
    //Map<Tile, ICrop> crops = new HashMap<>();

    Player player;

    public static Image loadImage(String location) {
        try {
            return new Image(location);
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Client instance;

    @Override
    public void init(GameContainer container) throws SlickException {
        instance = this;

        for(String fland : farmLandNames) {
            farmLandImages.put(fland, new Image("textures/farmland/" + fland + ".png"));
        }
        seeds = new Image("textures/farmables/seeds.png");
        crop = new Image("textures/farmables/crop.png");
        cropWilted = new Image("textures/farmables/cropWilted.png");

        for(Map.Entry<String, ICrop> entry : CropRegistry.getCropMap().entrySet()) {
            String seedName = entry.getKey();
            farmableImages.put(seedName, loadImage("textures/farmables/" + seedName + ".png"));
        }

        for(int x=0; x<farmWidth; x++) {
            for(int y=0; y<farmHeight; y++) {
                farmLand.put(new Tile(x, y), new Farmland(x, y));
            }
        }

        player = new Player();

        //-------------------------------------------------------

        SpriteSheet sheet;

        for(String anim : pAnims) {
            sheet = new SpriteSheet("textures/player/" + anim + ".png", 32, 32);
            int repeat = 250;
            if(anim.toLowerCase().contains("run"))
                repeat = 175;

            if(anim.contains("Left"))
                playerAnims.put(anim.replace("Left", "Right"), new Animation(new SpriteSheet(sheet.getFlippedCopy(true, false), 32, 32), repeat));
            playerAnims.put(anim, new Animation(sheet, repeat));
        }

        //-------------------------------------------------------

        objectTextures.put("grass", new Image("textures/grassTest.png"));
        objectTextures.put("bigStone", new Image("textures/bigStoneTest.png"));

        addGrass(8, 8);
        addGrass(8, 9);
        addGrass(16, 8);
        addGrass(18, 8);
        addGrass(14, 6);

        addBigStone(4, 4);
        addBigStone(16, 16);

        player.playerAnim = new Animation(new SpriteSheet(playerAnims.get("stand").getImage(player.pFacing.ordinal()), 32, 32), 1000);
    }

    void movement(GameContainer container, int delta) {
        boolean walkingSomewhere = false;
        boolean walkBlocked = false;

        int oldX = player.playerTile.getX();
        int oldY = player.playerTile.getY();

        boolean running = false;
        boolean carrying = player.holding != null;
        float modifier = 0.1f;

        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_LSHIFT)) { // Running
            running = true;
            modifier = 0.2f;
        }

        if (input.isKeyDown(Input.KEY_UP)) { // Walk Up
            player.playerAnim = carrying ? (running ? playerAnims.get("carryUpRun") : playerAnims.get("carryUp")) : (running ? playerAnims.get("runUp") : playerAnims.get("walkUp"));
            player.playerTile.updatePos((int) Math.floor((player.pX + 15) / 16), (int) Math.ceil((player.pY - 20 - delta * 0.1f) / 16));

            if(isSurroundingOk() && player.pY > 0)
                player.pY -= delta * modifier;
            else
                walkBlocked = true;

            player.pFacing = PlayerFacing.UP;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_DOWN)) {
            player.playerAnim = carrying ? (running ? playerAnims.get("carryDownRun") : playerAnims.get("carryDown")) : (running ? playerAnims.get("runDown") : playerAnims.get("walkDown"));
            player.playerTile.updatePos((int) Math.floor((player.pX + 15) / 16), (int) Math.ceil((player.pY - 20 + delta * 0.1f) / 16));

            if(isSurroundingOk() && player.pY + player.playerAnim.getHeight() < farmHeight * 16)
                player.pY += delta * modifier;
            else
                walkBlocked = true;

            player.pFacing = PlayerFacing.DOWN;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_LEFT)) {
            player.playerAnim = carrying ? (running ? playerAnims.get("carryLeftRun") : playerAnims.get("carryLeft")) : (running ? playerAnims.get("runLeft") : playerAnims.get("walkLeft"));
            player.playerTile.updatePos((int) Math.ceil((player.pX - delta * 0.1f) / 16), (int) Math.floor((player.pY + 8) / 16));

            if(isSurroundingOk() && player.pX > 0)
                player.pX -= delta * modifier;
            else
                walkBlocked = true;

            player.pFacing = PlayerFacing.LEFT;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)) {
            player.playerAnim = carrying ? (running ? playerAnims.get("carryRightRun") : playerAnims.get("carryRight")) : (running ? playerAnims.get("runRight") : playerAnims.get("walkRight"));
            player.playerTile.updatePos((int) Math.ceil((player.pX + delta * 0.1f) / 16), (int) Math.floor((player.pY + 8) / 16));

            if(isSurroundingOk() && player.pX + player.playerAnim.getWidth() < farmWidth * 16)
                player.pX += delta * modifier;
            else
                walkBlocked = true;

            player.pFacing = PlayerFacing.RIGHT;
            walkingSomewhere = true;
        }

        if(!walkingSomewhere) {
            player.playerAnim = player.holding != null ? new Animation(new Image[]{playerAnims.get("carryStill").getImage(player.pFacing.ordinal())}, 1000) : new Animation(new Image[]{playerAnims.get("stand").getImage(player.pFacing.ordinal())}, 1000);
        }

        if(walkBlocked)
            player.playerTile.updatePos(oldX, oldY);
        setPlayerTileBasedOnPosition();
    }

    void otherActions(GameContainer container) {
        Input input = container.getInput();

        Tile facingTile = new Tile(player.playerFacingTile.getX(), player.playerFacingTile.getY());

        if(input.isKeyPressed(Keyboard.KEY_G)) {
            if(!removeObject(facingTile))
                addGrass(facingTile.x, facingTile.y);
        }
        if(input.isKeyPressed(Keyboard.KEY_R)) { // Random Objects
            objects.clear();

            Random random = new Random();
            for(int i=0; i<32; i++) {
                int tX = random.nextInt(32);
                int tY = random.nextInt(32);

                if(getObjectAtPosition(tX, tY) != null)
                    while(getObjectAtPosition(tX, tY) != null) {
                        tX = random.nextInt(32);
                        tY = random.nextInt(32);
                    }

                Entity ent = entities[random.nextInt(entities.length)].clone();
                ent.updatePos(tX, tY);

                if(intersectsWithAny(ent) || ent.getBoundingBox().intersects(outerRight) || ent.getBoundingBox().intersects(outerBottom))
                    i -= 1;
                else {
                    fixFarmlands(ent.getBoundingBox());
                    objects.put(new Tile(tX, tY), ent);
                }
            }
        }
        if(input.isKeyPressed(Keyboard.KEY_C)) { // Pickup items | Temporary
            if(player.holding != null) {
                if(isEntityInRectangle(new Rectangle(facingTile.x * 16 + 4, facingTile.y * 16 + 4, 7, 7)))
                    return;

                addGrass(facingTile.x, facingTile.y);
                player.holding = null;
                return;
            }

            Entity ent = getObjectAtPosition(facingTile.x, facingTile.y);
            if(ent == null || !(ent instanceof IPickupable))
                return;

            removeObject(player.playerFacingTile);
            player.holding = ent;
        }
        if(input.isKeyPressed(Keyboard.KEY_B)) { // Till / Un-Till Farmland
            Entity ent = getObjectAtPosition(facingTile.x, facingTile.y);
            if(ent != null)
                return;

            Farmland land = farmLand.get(facingTile);

            land.setTilled(!land.isTilled());
        }
        if(input.isKeyPressed(Keyboard.KEY_N)) { // Water / Un-Water Farmland
            Entity ent = getObjectAtPosition(facingTile.x, facingTile.y);
            if(ent != null)
                return;

            Farmland land = farmLand.get(facingTile);
            if(!land.isTilled())
                return;

            land.setWatered(!land.isWatered());
        }

        if(input.isKeyPressed(Keyboard.KEY_NUMPAD1)) { // Demo Seeds Selection
            player.useable = new TurnipSeeds();
            System.out.println("Selected " + ((ISeeds)player.useable).getSeedName());
        }
        if(input.isKeyPressed(Keyboard.KEY_NUMPAD2)) { // Demo Seeds Selection 2
            player.useable = new TomatoSeeds();
            System.out.println("Selected " + ((ISeeds)player.useable).getSeedName());
        }
        if(input.isKeyPressed(Keyboard.KEY_P)) { // Demo Seeds Planting
            Entity ent = getObjectAtPosition(facingTile.x, facingTile.y);
            if(ent != null)
                return;

            Farmland land = farmLand.get(facingTile);
            if(land == null || !land.isTilled() || land.hasCrop())
                return;

            if(!(player.useable instanceof ISeeds))
                return;

            Tile cropTile = new Tile(facingTile.x, facingTile.y);
            addCrop(cropTile, CropRegistry.buildCrop(CropRegistry.getCrop((ISeeds) player.useable)));
        }

        if(input.isKeyPressed(Keyboard.KEY_I)) { // Farmland seed analyzer
            Entity ent = getObjectAtPosition(facingTile.x, facingTile.y);
            if(ent != null)
                return;

            Farmland land = farmLand.get(facingTile);
            if(land == null || !land.isTilled() || !land.hasCrop())
                return;

            ICrop crop = land.getCrop();
            System.out.println(seeds != null ? crop.getSeeds().getSeedName() : "None");
        }

        if(input.isKeyPressed(Keyboard.KEY_L)) { // Simulate Crop Growth
            if(farmLand.isEmpty())
                return;

            List<Tile> remove = new ArrayList<>();
            for(Map.Entry<Tile, Farmland> entry : farmLand.entrySet()) {
                entry.getValue().operate();

                if(entry.getValue().hasCrop())
                    if(entry.getValue().getCrop().getCropState() == CropState.GRASS_MORPH)
                        remove.add(entry.getKey());
            }
            if(!remove.isEmpty())
                for(Tile cropTile : remove) {
                    removeCrops(cropTile);
                    addGrass(cropTile.getX(), cropTile.getY());
                }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        movement(container, delta);
        otherActions(container);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        for(Map.Entry<Tile, Farmland> pos : farmLand.entrySet()) {
            Tile tile = pos.getKey();
            Farmland land = pos.getValue();
            farmLandImages.get(land.isWatered() ? "watered" : (land.isTilled() ? "tilled" : "normal")).draw(tile.x * 16, tile.y * 16);
            if(land.hasCrop()) {
                ICrop crop_ = land.getCrop();
                CropState cropState = crop_.getCropState();
                if(cropState == CropState.SEEDS)
                    seeds.draw(tile.x * 16, tile.y * 16);
                else if(cropState == CropState.GROWING)
                    crop.draw(tile.x * 16, tile.y * 16);
                else if(cropState == CropState.HARVESTABLE || cropState == CropState.WILTED)
                    crop_.getTexture().getSubImage(0, 0, 16, 13).draw(tile.x * 16, tile.y * 16 - 1);
                else if(cropState == CropState.GROWING_WILTED) {
                    cropWilted.draw(tile.x * 16, tile.y * 16);
                }
            }
        }

        for(Map.Entry<Tile, Entity> pos : objects.entrySet()) {
            Tile tile = pos.getKey();
            objectTextures.get(pos.getValue().texture).draw(tile.x * 16, tile.y * 16);
        }
        g.setColor(new Color(1f, 1f, 1f, 1f));

        player.playerAnim.draw(player.pX, player.pY);


        g.setColor(new Color(0f, 0f, 0f, 0.75f));

        g.drawRect(player.playerFacingTile.x * 16, player.playerFacingTile.y * 16, 15, 15);

        if(player.holding != null) {
            objectTextures.get(player.holding.texture).draw(player.pX + player.holding.spriteSize / 2, player.pY - player.holding.spriteSize / 2);
        }
    }

    boolean isSurroundingOk() {
        boolean ret = true;
        int[] inter = intersectsWithAny();
        int[] interLand = intersectsWithFarmlandCrops();
        if(inter[0] != 0 || inter[1] != 0 || interLand[0] != 0 || interLand[1] != 0)
            ret = false;

        if(!ret) {
            player.updatePos(player.pX + inter[0] * 0.1f + interLand[0] * 0.1f, player.pY + inter[1] * 0.1f + interLand[1] * 0.1f);
            return false;
        }
        return true;
    }

    public void addGrass(int tX, int tY) {
        objects.put(new Tile(tX, tY), new Grass(tX, tY));
        fixFarmlands(new Rectangle(tX * 16 + 4, tY * 16 + 4, 8, 8));
    }

    void addBigStone(int tX, int tY) {
        objects.put(new Tile(tX, tY), new BigStone(tX, tY));
        fixFarmlands(new Rectangle(tX * 16 + 4, tY * 16 + 4, 24, 24));
    }

    boolean removeObject(Tile facingTile) {
        Rectangle faceRect = new Rectangle(facingTile.x * 16 + 4, facingTile.y * 16 + 4, 7, 7);
        for (Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Entity ent = entry.getValue();
            if (faceRect.intersects(ent.getBoundingBox())) {
                objects.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    void setPlayerTileBasedOnPosition() {
        int x = (int) Math.ceil(player.pX / 16);
        int y = (int) Math.ceil((player.pY + 10) / 16);
        player.playerTile.updatePos(x, y);
        player.updatePos(player.pX, player.pY);
    }

    boolean intersectsWithAny(Entity toCheck) {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Entity ent = entry.getValue();
            if(toCheck.getBoundingBox().intersects(ent.getBoundingBox()))
                return true;
        }
        return false;
    }

    int[] intersectsWithAny() {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Entity ent = entry.getValue();
            if(player.intersects(ent))
                return player.calculateIntersection(ent.getBoundingBox());
        }
        return new int[]{0, 0};
    }

    Entity getObjectAtPosition(int tX, int tY) {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Tile tile = entry.getKey();
            if(tX == tile.x && tY == tile.y)
                return entry.getValue();
        }
        return null;
    }

    int[] intersectsWithFarmlandCrops() {
        for(Map.Entry<Tile, Farmland> entry : farmLand.entrySet()) {
            Farmland land = entry.getValue();
            if(!land.hasCrop() || land.getCrop().getCropState() == CropState.SEEDS)
                continue;

            if(player.intersects(land.boundingBox))
                return player.calculateIntersection(land.boundingBox);
        }
        return new int[]{0, 0};
    }

    boolean isEntityInRectangle(Shape shape) {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet())
            if(entry.getValue().getBoundingBox().intersects(shape))
                return true;
        return false;
    }

    void fixFarmlands(Shape shape) {
        Rectangle landRect = new Rectangle(0, 0, 8, 8);
        for(Map.Entry<Tile, Farmland> entry : farmLand.entrySet()) {
            Farmland land = entry.getValue();
            Tile tile = entry.getKey();
            landRect.setLocation(tile.x * 16 + 4, tile.y * 16 + 4);
            if(landRect.intersects(shape))
                land.setTilled(false);
        }
    }

    void addCrop(Tile tile, ICrop crop) {
        if(farmLand.containsKey(tile)) {
            Farmland land = farmLand.get(tile);
            if(!land.hasCrop())
                land.setCrop(crop);
        }
    }

    public void removeCrops(Tile tile) {
        if(farmLand.containsKey(tile)) {
            Farmland land = farmLand.get(tile);
            land.setCrop(null);
        }
    }
}