package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.network.NetworkHandler;
import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.farmable.crops.CropRegistry;
import com.lordmau5.harvest.shared.farmable.crops.CropState;
import com.lordmau5.harvest.shared.farmable.crops.ICrop;
import com.lordmau5.harvest.shared.farmable.seeds.ISeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TomatoSeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TurnipSeeds;
import com.lordmau5.harvest.shared.floor.Farmland;
import com.lordmau5.harvest.shared.network.packet.player.PacketPlayerMovement;
import com.lordmau5.harvest.shared.objects.Entity;
import com.lordmau5.harvest.shared.objects.doubleTile.BigStone;
import com.lordmau5.harvest.shared.objects.singleTile.Grass;
import com.lordmau5.harvest.shared.player.Player;
import com.lordmau5.harvest.shared.player.PlayerFacing;
import com.lordmau5.harvest.shared.util.ImageLoader;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;

import java.util.*;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends BasicGame {

    public static Client instance;

    static int width = 800;
    static int height = 600;

    public Client() {
        super("Harvest Multiplayer");
    }

    //--------------------------------------------------------

    //TiledMap farmLandTest;
    //Rectangle outerRight = new Rectangle(32 * 16 + 1, 0, 1, 32 * 16 + 1);
    //Rectangle outerBottom = new Rectangle(0, 32 * 16 + 1, 32 * 16 + 1, 1);

    String[] pAnims = {"stand", "carryStill", "walkUp", "walkDown", "walkLeft", "runUp", "runDown", "runLeft", "carryUp", "carryDown", "carryLeft", "carryUpRun", "carryDownRun", "carryLeftRun"};

    String[] farmLandNames = {"normal", "tilled", "watered"};
    Map<String, Image> farmLandImages = new HashMap<>();
    Image seeds;
    Image crop, cropWilted;
    Map<String, Image> farmableImages = new HashMap<>();

    Player player;
    static String playerName;

    public static void main(String args[]) {

        //-- Connection --

        playerName = "Test-" + (new Random().nextInt(89999) + 10000);

        String host = "localhost";
        int port = 8075;

        NetworkHandler.connect(host, port);

        //----------------------------------------

    }

    public static void initGLStuff() {
        AppGameContainer game;
        try {
            game = new AppGameContainer(new Client());
            game.setDisplayMode(width, height, false);
            game.setShowFPS(false);
            game.setTargetFrameRate(60);
            game.setUpdateOnlyWhenVisible(true);
            game.setAlwaysRender(true);
            game.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    Entity[] entities = new Entity[] {new Grass(), new BigStone()};
    Map<String, Image> objectTextures = new HashMap<>();
    Map<String, Player> players = new HashMap<>();
    List<Player> aPlayers = new ArrayList<>();

    public void playerJoin(String username) {
        if(!players.containsKey(username)) {
            Player player = new Player(username);
            player.setWorld(this.player.getWorld());
            players.put(username, player);
            aPlayers.add(player);
        }
    }

    public void playerLeave(String username) {
        if(players.containsKey(username)) {
            players.remove(username);

            for(Player player : aPlayers)
                if(player.getUsername().equals(username)) {
                    aPlayers.remove(player);
                    return;
                }
        }
    }

    public Player getPlayer(String username) {
        if(!players.containsKey(username))
            return null;
        return players.get(username);
    }

    public Player getLocalPlayer() {
        return player;
    }

    public static String getPlayerName() {
        return playerName;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        instance = this;

        //-----------

        for(String fland : farmLandNames) {
            farmLandImages.put(fland, ImageLoader.loadImage("textures/farmland/" + fland + ".png"));
        }
        seeds = ImageLoader.loadImage("textures/farmables/seeds.png");
        crop = ImageLoader.loadImage("textures/farmables/crop.png");
        cropWilted = ImageLoader.loadImage("textures/farmables/cropWilted.png");

        for(Map.Entry<String, ICrop> entry : CropRegistry.getCropMap().entrySet()) {
            String seedName = entry.getKey();
            farmableImages.put(seedName, ImageLoader.loadImage("textures/farmables/" + seedName + ".png"));
        }

        //-------------------------------------------------------

        SpriteSheet sheet;

        for(String anim : pAnims) {
            sheet = new SpriteSheet("textures/player/" + anim + ".png", 32, 32);
            int repeat = 250;
            if(anim.toLowerCase().contains("run"))
                repeat = 175;

            if(anim.contains("Left"))
                Player.playerAnims.put(anim.replace("Left", "Right"), new Animation(new SpriteSheet(sheet.getFlippedCopy(true, false), 32, 32), repeat));
            Player.playerAnims.put(anim, new Animation(sheet, repeat));
        }

        //-------------------------------------------------------

        objectTextures.put("grass", ImageLoader.loadImage("textures/grassTest.png"));
        objectTextures.put("bigStone", ImageLoader.loadImage("textures/bigStoneTest.png"));

        /*addGrass(8, 8);
        addGrass(8, 9);
        addGrass(16, 8);
        addGrass(18, 8);
        addGrass(14, 6);

        addBigStone(4, 4);
        addBigStone(16, 16);*/

        player = new Player(playerName);
        players.put(playerName, player);
        aPlayers.add(player);
        player.setWorld(new World());
    }

    void movement(GameContainer container, int delta) {
        boolean walkingSomewhere = false;
        boolean walkBlocked = false;

        int oldX = player.playerTile.getX();
        int oldY = player.playerTile.getY();

        boolean running = false;

        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_LSHIFT)) // Running
            running = true;

        if (input.isKeyDown(Input.KEY_UP)) { // Walk Up
            walkingSomewhere = true;
            walkBlocked = player.walk(PlayerFacing.UP, delta, running);
        }
        else if (input.isKeyDown(Input.KEY_DOWN)) {
            walkingSomewhere = true;
            walkBlocked = player.walk(PlayerFacing.DOWN, delta, running);
        }
        else if (input.isKeyDown(Input.KEY_LEFT)) {
            walkingSomewhere = true;
            walkBlocked = player.walk(PlayerFacing.LEFT, delta, running);
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)) {
            walkingSomewhere = true;
            walkBlocked = player.walk(PlayerFacing.RIGHT, delta, running);
        }

        if(!walkingSomewhere) {
            player.playerAnim = player.holding != null ? new Animation(new Image[]{Player.playerAnims.get("carryStill").getImage(player.pFacing.ordinal())}, 1000) : new Animation(new Image[]{Player.playerAnims.get("stand").getImage(player.pFacing.ordinal())}, 1000);
            NetworkHandler.sendPacket(new PacketPlayerMovement(player, false, true));
        }

        if(walkBlocked)
            player.playerTile.updatePos(oldX, oldY);
        else {
            player.updatePos(player.pX, player.pY);
            NetworkHandler.sendPacket(new PacketPlayerMovement(player, running, false));
        }
    }

    void otherActions(GameContainer container) {
        Input input = container.getInput();

        /*if(input.isKeyPressed(Keyboard.KEY_R)) { // Random Objects
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
        }*/
        if(input.isKeyPressed(Keyboard.KEY_C)) { // Pickup items | Temporary
            player.doAction(Keyboard.KEY_C);
        }
        if(input.isKeyPressed(Keyboard.KEY_B)) { // Till / Un-Till Farmland
            player.doAction(Keyboard.KEY_B);
        }
        if(input.isKeyPressed(Keyboard.KEY_N)) { // Water / Un-Water Farmland
            player.doAction(Keyboard.KEY_N);
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
            player.doAction(Keyboard.KEY_P);
        }

        if(input.isKeyPressed(Keyboard.KEY_I)) { // Farmland seed analyzer
            player.doAction(Keyboard.KEY_I);
        }

        if(input.isKeyPressed(Keyboard.KEY_L)) { // Simulate Crop Growth
            player.doAction(Keyboard.KEY_L);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        movement(container, delta);
        otherActions(container);
    }

    private void reArrangePlayers() {
        Collections.sort(aPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.pY >= p2.pY ? 1 : -1;
            }
        });
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.translate(width / 2 - player.pX, height / 2 - player.pY);

        for(Map.Entry<Tile, Farmland> pos : player.getWorld().getFarmland().entrySet()) {
            Tile tile = pos.getKey();
            Farmland land = pos.getValue();
            farmLandImages.get(land.isWatered() ? "watered" : (land.isTilled() ? "tilled" : "normal")).draw(tile.getX() * 16, tile.getY() * 16);
            if(land.hasCrop()) {
                ICrop crop_ = land.getCrop();
                CropState cropState = crop_.getCropState();
                if(cropState == CropState.SEEDS)
                    seeds.draw(tile.getX() * 16, tile.getY() * 16);
                else if(cropState == CropState.GROWING)
                    crop.draw(tile.getX() * 16, tile.getY() * 16);
                else if(cropState == CropState.HARVESTABLE || cropState == CropState.WILTED)
                    crop_.getTexture().getSubImage(0, 0, 16, 13).draw(tile.getX() * 16, tile.getY() * 16 - 1);
                else if(cropState == CropState.GROWING_WILTED) {
                    cropWilted.draw(tile.getX() * 16, tile.getY() * 16);
                }
            }
        }

        for(Map.Entry<Tile, Entity> pos : player.getWorld().getObjects().entrySet()) {
            Tile tile = pos.getKey();
            objectTextures.get(pos.getValue().texture).draw(tile.getX() * 16, tile.getY() * 16);
        }
        g.setColor(new Color(0f, 0f, 0f, 0.75f));

        g.drawRect(player.playerFacingTile.getX() * 16, player.playerFacingTile.getY() * 16, 15, 15);

        reArrangePlayers();
        for(Player player : aPlayers) {
            g.setColor(new Color(1f, 1f, 1f, 1f));
            player.playerAnim.draw(player.pX, player.pY);

            if(player.holding != null) {
                objectTextures.get(player.holding.texture).draw(player.pX + player.holding.spriteSize / 2, player.pY - player.holding.spriteSize / 2);
            }

            g.setColor(new Color(0f, 0f, 0f, 0.5f));

            String username = player.getUsername();
            int usernameWidth = g.getFont().getWidth(username);
            Image sprite = player.playerAnim.getCurrentFrame();
            g.fillRect(player.pX - usernameWidth / 2 + sprite.getWidth() / 2 - 4, player.pY - 25, usernameWidth + 8, 22);
            g.setColor(new Color(1f, 1f, 1f, 1f));
            g.drawString(username.replace("_", " "), player.pX - usernameWidth / 2 + sprite.getWidth() / 2, player.pY - 25);
        }

        //-----
    }


}