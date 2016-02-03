package pk.risiko.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class is able to store all GameTextures and handles
 * Lazy-Loading part. Since all Textures should only exist
 * once in the Game LiveCycle this Class should only be
 * idolized once, otherwise Memory would be wasted!
 *
 * @author Raphael Ludwig
 * @version 30.01.2016
 */
public class ImageStore {
    /** statically save the instance **/
    private static ImageStore instance = null;

    /** save all the textures as key/value pair into a map for fast lookup **/
    private Map<String,BufferedImage> images = new HashMap<>();
    /** The directory from which every file can be loaded **/
    private String rootDirectory;

    /**
     * @param directory the root directory from which all files are loaded
     * @param preload of true all files are loaded on initialization otherwise they are lazy loaded
     */
    private ImageStore(String directory,boolean preload) {
        this.rootDirectory = directory;
        if( preload ) this.loadFolder(new File(directory));
    }

    /**
     * This Method should only be called once for each directory which should be
     * once for Game LiveCycle
     *
     * @param root directory which contains the resources
     * @param preload true if all should be loaded now or lazy initialized
     */
    public static void initialize(String root, boolean preload) {
        instance = new ImageStore(root,preload);
    }

    /**
     * @return the current instance of the the ImageStore
     */
    public static ImageStore getInstance() {
        assert instance != null : "Error: ImageStore must be initialized!";

        return instance;
    }

    /**
     * This Method des load all the Files in the the Folder
     *
     * @param f target folder
     */
    private void loadFolder(File f) {
        File[] files = f.listFiles();
        if( files != null )
            Arrays.stream(files).filter(File::isFile).forEach(this::loadImage);
        else
            System.err.println("No resources found!");
    }

    /**
     * This Method does load the file if it's a .ping or a .jpg otherwise it will be ignored
     *
     * @param f target file
     */
    private void loadImage(File f) {
        try {
            if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg"))
                images.put(f.getName().split("\\.")[0], ImageIO.read(f));
        } catch (IOException ex) {
            assert false : "Unable to read File: " + f.getName();
            System.err.println("Unable to read File: " + f.getName());
            ex.printStackTrace();
        }
    }

    /**
     * This Method does return the Image by the name (without file extension)
     * If at the initialization phase preload was set to true it is queried from
     * the internal cache otherwise if not loaded from a call before it will be
     * loaded from the disk and stored in the cache.
     *
     * @param name name of the Texture
     * @return the texture as BufferedImage
     */
    public BufferedImage getImage(String name) {
        if( !this.images.containsKey(name) ) {
            File target = new File(this.rootDirectory + name);
            if (target.exists()) loadImage(target);

            if( !this.images.containsKey(name) )
                throw new RuntimeException("Unable to load Resource due IO Errors! ("+name+")");
        }

        return this.images.get(name);
    }

}
