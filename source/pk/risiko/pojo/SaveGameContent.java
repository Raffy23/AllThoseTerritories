package pk.risiko.pojo;

import java.io.File;
import java.util.List;

/**
 * This class holds information about the save games on the disk
 *
 * @author Raphael Ludwig
 * @version 31.01.2016
 */
public class SaveGameContent {

    private final File savFile;
    private final String name;
    private final String mapName;
    private final List<Player> playerList;
    private final int rounds;
    private final int slot;

    public SaveGameContent(File savFile, String name, String mapName, List<Player> playerList,int rounds,int slot) {
        this.savFile = savFile;
        this.name = name;
        this.mapName = mapName;
        this.playerList = playerList;
        this.rounds = rounds;
        this.slot = slot;
    }

    public File getSavFile() {
        return savFile;
    }

    public String getName() {
        return name;
    }

    public String getMapName() {
        return mapName;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public int getRounds() {
        return rounds;
    }

    public int getSlot() {
        return slot;
    }
}
