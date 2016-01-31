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
 * Created by:
 *
 * @author Raphael Ludwig
 * @version 31.01.2016
 */
public class SaveGameDAO {

    private static final String SAVE_FILE_REGEX = "^slot[0-9]\\.sav$";

    private final File rootDirectory;
    private final Map<Integer,SaveGameContent> saveGameMapping = new HashMap<>();

    public SaveGameDAO(String directory) {
        this.rootDirectory = new File(directory);
        if( !this.rootDirectory.exists() )
            if( !this.rootDirectory.mkdirs() )
                throw new RuntimeException("Unable to create Directory for Savegames!");


        this.populateSaveGameMapping();
    }

    private void populateSaveGameMapping() {
        final File[] currentSaveFiles = this.rootDirectory.listFiles(file -> file.getName().matches(SAVE_FILE_REGEX));

        Properties saveGame = new Properties();
        for(File file:currentSaveFiles) {
            try {
                saveGame.load(new FileReader(file));
                this.saveGameMapping.put( Integer.valueOf(file.getName().charAt(4)-'0')
                                        , new SaveGameContent(file
                                                             ,saveGame.getProperty("name")
                                                             ,saveGame.getProperty("mapName")
                                                             ,this.parsePlayers(saveGame)
                                                             ,Integer.valueOf(saveGame.getProperty("rounds"))));
            } catch (IOException e) {
                assert false : "Unable to load SaveGame File! ("+file.getName()+")";
                e.printStackTrace();
            }
        }
    }

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

            assert false : "Unknown AI type! >" + dataBlocks[3] + "<";
        }

        return new Player(name, color, null);
    }


    public Map<Integer,SaveGameContent> getSaveGames() {
        return this.saveGameMapping;
    }

    public boolean saveGameToSlot(GameMap map,List<Player> players,String name,int rounds,int slot) {
        final File target = new File(rootDirectory.getAbsolutePath()+File.separator+"slot"+slot+".sav");
        final Properties saveGame = new Properties();

        saveGame.setProperty("name",name);
        saveGame.setProperty("mapName",map.getMapName());
        saveGame.setProperty("rounds",String.valueOf(rounds));

        for( int i=0;i<4;i++ ) {
            if( players.size() <= i )
                saveGame.setProperty("player"+(i+1),"-");
            else
                saveGame.setProperty("player"+(i+1),this.parsePlayerToString(players.get(i)));
        }

        map.getTerritories().forEach(t ->
            saveGame.setProperty(t.getName(),t.getOwner().getName().replace(" ","_")+ " " + t.getStationedArmies())
        );

        try {
            saveGame.store(new FileWriter(target),"SaveTime: " + new Date().toString());
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Unable to save slot "+slot+"!";
            return false;
        }

        return true;
    }

    private String parsePlayerToString(Player player) {
        String data = player.getName().replace(" ","_") + " " + player.getColor().getRed()
                                                        + " " + player.getColor().getGreen()
                                                        + " " + player.getColor().getBlue()
                                                        + " " + player.getColor().getAlpha();

        if( player instanceof AI)
            data += " " + player.getClass().getSimpleName();

        return data;
    }

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

            assert false : "Unable to load SaveGame File on slot: " + slot + "!";
        }


        return null;
    }

    private Player findPlayer(List<Player> players,String name) {
        for(Player p:players) if( p.getName().equals(name) ) return p;
        return null;
    }

}
