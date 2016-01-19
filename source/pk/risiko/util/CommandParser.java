package pk.risiko.util;

/**
 * Simply a utility which parses the commandline and does
 * save the state of it
 *
 * @author Raphael
 * @version 12.01.2016
 */
public class CommandParser {

    private final static String MAP_FILE_KEY   = "--map";
    private final static String SKIP_MENU_FLAG = "--skip-menu";

    private String mapFile;
    private boolean skipMenu;
    private boolean invalid;

    public CommandParser(String[] argvs) {

        for(int i=0;i<argvs.length;i++) {
            switch (argvs[i]) {
                case MAP_FILE_KEY:
                    if( argvs.length < i+1 ) invalid = true;
                    else { mapFile = argvs[i+1]; this.skipMenu = true;} break;
                case SKIP_MENU_FLAG: this.skipMenu = true; break;
                default: invalid = true;
            }
        }
    }

    public String getMapFile() {
        return mapFile;
    }

    public boolean isSkipMenu() {
        return skipMenu;
    }

    public boolean isInvalid() {
        return invalid;
    }
}
