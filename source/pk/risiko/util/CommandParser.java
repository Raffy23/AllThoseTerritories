package pk.risiko.util;

/**
 * Simply a utility which parses the commandline and does
 * save the state of it
 *
 * @author Raphael Ludwig
 * @version 12.01.2016
 */
public class CommandParser {

    /** Command Parameter for the MAP **/
    private final static String MAP_FILE_KEY   = "--map";
    /** Command Parameter for the skip menu flag **/
    private final static String SKIP_MENU_FLAG = "--skip-menu";

    /** stored map file name if the map file flag was given **/
    private String mapFile;
    /** true if the skip menu flag was given otherwise false **/
    private boolean skipMenu;
    /** true if there was any not known flag in the commandline **/
    private boolean invalid;

    /**
     * This Parser does parse all Elements of the given String array assuming
     * its the Array from the commandline
     *
     * @param argvs commandline
     */
    public CommandParser(String[] argvs) {
        //interate over all elements
        for(int i=0;i<argvs.length;i++) {
            //do actual parsing
            switch (argvs[i]) {
                case MAP_FILE_KEY:
                    if( argvs.length < i+1 ) invalid = true; //rangecheck
                    else { mapFile = argvs[i+1]; this.skipMenu = true;} break;
                case SKIP_MENU_FLAG: this.skipMenu = true; break;
                default: invalid = true;
            }
        }
    }

    /**
     * @return the map file name if the flag was given otherwise null
     */
    public String getMapFile() {
        return mapFile;
    }

    /**
     * @return true if the skip menu flag was given otherwise false
     */
    public boolean isSkipMenu() {
        return skipMenu;
    }

    /**
     * @return true if ther was any non known flag in the commandline
     */
    public boolean isInvalid() {
        return invalid;
    }
}
