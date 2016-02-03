package pk.risiko.dao;

import pk.risiko.pojo.AI;
import pk.risiko.pojo.ContinentBasedAI;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.RandomAI;
import pk.risiko.pojo.SaveGameContent;
import pk.risiko.util.SettingsProvider;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This Class is responsible for Loading, parsing and saving the
 * GameSave files
 *
 * @author Raphael Ludwig
 * @version 31.01.2016
 */
public class SaveGameDAO {

    /** Regex for the Filenames **/
    private static final String SAVE_FILE_REGEX = "^slot[0-9]\\.sav$";

    /** root directory in which all the saves are stored **/
    private final File rootDirectory;
    /** a map in which slots the parse game data is stored **/
    private final Map<Integer,SaveGameContent> saveGameMapping = new HashMap<>();

    /**
     * On initalization the directory is read, but no GameMap Data is created!
     *
     * @param directory save folder
     */
    public SaveGameDAO(String directory) {
        this.rootDirectory = new File(directory);
        if( !this.rootDirectory.exists() )
            if( !this.rootDirectory.mkdirs() )
                throw new RuntimeException("Unable to create Directory for Savegames!");

        //load the data
        this.populateSaveGameMapping();
    }

    /**
     * This Method does loop over all files and does parse the meta data to the
     * SaveGameContent objects
     *
     * @see SaveGameContent
     */
    private void populateSaveGameMapping() {
        final File[] currentSaveFiles = this.rootDirectory.listFiles(file -> file.getName().matches(SAVE_FILE_REGEX));

        //Loop over all files:
        Properties saveGame = new Properties();
        for(File file:currentSaveFiles) {
            try {
                saveGame.load(new FileReader(file)); //load data
                //parse to content object
                this.saveGameMapping.put( Integer.valueOf(file.getName().charAt(4)-'0')
                                        , new SaveGameContent(file
                                                             ,saveGame.getProperty("name")
                                                             ,saveGame.getProperty("mapName")
                                                             ,this.parsePlayers(saveGame)
                                                             ,Integer.valueOf(saveGame.getProperty("rounds"))
                                                             ,Integer.valueOf(file.getName().charAt(4)-'0')));
            } catch (IOException e) {
                //only fail hard in debug mode, in production the save file is corrupted and can not be loaded
                assert false : "Unable to load SaveGame File! ("+file.getName()+")";
                e.printStackTrace();
            }
        }
    }

    /**
     * This Method does parse the contents of the Players form the SaveGameFile to the actual
     * list of players which can be inserted to any GameMap (GameMap must be set in the Player!)
     *
     * @param saveGame saveGame File
     * @return list of players
     */
    private List<Player> parsePlayers(Properties saveGame) {
        List<Player> players = new ArrayList<>(4);

        Player p = parsePlayerData(saveGame.getProperty("player1"));
        if( p != null ) players.add(p);

        p = parsePlayerData(saveGame.getProperty("player2"));
        if( p != null ) players.add(p);

        p = parsePlayerData(saveGame.getProperty("player3"));
        if( p != null ) players.add(p);

        p = parsePlayerData(saveGame.getProperty("player4"));
        if( p != null ) players.add(p);

        return players;
    }

    /**
     * This Method does parse one line of the PlayerData and returns a Player by it
     *
     * @param data the line content of a player
     * @return Player from that data
     */
    private Player parsePlayerData(String data) {
        if (data.startsWith("-")) return null;

        String[] dataBlocks = data.split(" ");
        final String name = dataBlocks[0].replace("_"," ");
        final Color color = new Color(Integer.valueOf(dataBlocks[1])
                                     ,Integer.valueOf(dataBlocks[2])
                                     ,Integer.valueOf(dataBlocks[3])
                                     ,Integer.valueOf(dataBlocks[4]));

        //AI
        if (dataBlocks.length > 5) {
            if (dataBlocks[5].equals(RandomAI.class.getSimpleName())) return  new RandomAI(name, color, null);
            if (dataBlocks[5].equals(ContinentBasedAI.class.getSimpleName())) return new ContinentBasedAI(name, color, null);

            //in production mode, this player must be played by Human
            assert false : "Unknown AI type! >" + dataBlocks[3] + "<";
        }

        return new Player(name, color, null);
    }

    /**
     * @return all the loaded SaveGameContents mapped to their slots
     */
    public Map<Integer,SaveGameContent> getSaveGames() {
        return this.saveGameMapping;
    }

    /**
     * Saves a GameMap with Players and a custom name to a save game slot
     *
     * @param map game map
     * @param players list of players on the game map
     * @param name name of the save file (shown in game)
     * @param rounds how many rounds the players have played
     * @param slot the slot where the data is saved
     * @return true if saving was successful otherwise false
     */
    public boolean saveGameToSlot(GameMap map,List<Player> players,String name,int rounds,int slot) {
        final File target = new File(rootDirectory.getAbsolutePath()+File.separator+"slot"+slot+".sav");
        final Properties saveGame = new Properties();

        //save meta data
        saveGame.setProperty("name",name);
        saveGame.setProperty("mapName",map.getMapName());
        saveGame.setProperty("rounds",String.valueOf(rounds));

        //save all the players
        for( int i=0;i<4;i++ ) {
            if( players.size() <= i )
                saveGame.setProperty("player"+(i+1),"-");
            else
                saveGame.setProperty("player"+(i+1),this.parsePlayerToString(players.get(i)));
        }

        //convert all the spaces to '_'
        map.getTerritories().forEach(t ->
            saveGame.setProperty(t.getName(),t.getOwner().getName().replace(" ","_")+ " " + t.getStationedArmies())
        );

        //try to save it
        try {
            saveGame.store(new FileWriter(target),"SaveTime: " + new Date().toString());
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Unable to save slot "+slot+"!";
            return false; //fail hard in debug code, in production error should be displayed to the user
        }

        return true;
    }

    /**
     * Converts a Player object to a String
     * (Next time Serialization is uesed)
     *
     * @param player a player object
     * @return data which represents the player
     */
    private String parsePlayerToString(Player player) {
        String data = player.getName().replace(" ","_") + " " + player.getColor().getRed()
                                                        + " " + player.getColor().getGreen()
                                                        + " " + player.getColor().getBlue()
                                                        + " " + player.getColor().getAlpha();

        if( player instanceof AI)
            data += " " + player.getClass().getSimpleName();

        return data;
    }

    /**
     * This Method does load the GameMap from a slot. All the Players in the SaveGameConent Mapping
     * are also updated with this MapData!
     *
     * @param slot a lost which should be loaded
     * @return GameMap if load was successful otherwise null
     */
    public GameMap loadSaveGame(int slot) {
        final SaveGameContent content = this.saveGameMapping.get(slot);
        final Properties saveGame = new Properties();

        try {
            saveGame.load(new FileReader(content.getSavFile()));

            GameMap map = new MapFileReader(SettingsProvider.getInstance()
                                                            .getMapDirectoryPath())
                                            .readMap(saveGame.getProperty("mapName"));

            content.getPlayerList().forEach(p -> p.setGameMap(map));

            map.getTerritories().forEach(t -> {
                String[] data = saveGame.getProperty(t.getName()).split(" ");
                t.setOwner( this.findPlayer(content.getPlayerList(),data[0].replace("_"," ")) );
                t.increaseArmy( Integer.valueOf(data[1]));
            });

            return map;
        } catch (IOException e) {
            e.printStackTrace();
            //failing very hard in debug mode
            assert false : "Unable to load SaveGame File on slot: " + slot + "!";
        }

        //Well not the best error Handling
        return null;
    }

    /**
     * Finds a player with the given name in the given list players
     * @param players list of players
     * @param name the name which is searched for
     * @return the player or null if not found
     */
    private Player findPlayer(List<Player> players,String name) {
        for(Player p:players) if( p.getName().equals(name) ) return p;
        return null;
    }

}
