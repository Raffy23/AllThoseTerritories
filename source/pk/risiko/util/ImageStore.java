package pk.risiko.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:
 *
 * @author Raphael Ludwig
 * @version 30.01.2016
 */
public class ImageStore {
    private static ImageStore instance = null;

    private Map<String,BufferedImage> images = new HashMap<>();
    private String rootDirectory;

    private ImageStore(String directory,boolean preload) {
        this.rootDirectory = directory;
        if( preload ) this.loadFolder(new File(directory));
    }

    public static void initialize(String root, boolean preload) {
        instance = new ImageStore(root,preload);
    }

    public static ImageStore getInstance() {
        assert instance != null : "Error: ImageStore must be initialized!";

        return instance;
    }

    private void loadFolder(File f) {
        File[] files = f.listFiles();
        if( files != null )
            Arrays.stream(files).filter(File::isFile).forEach(this::loadImage);
        else
            System.err.println("No resources found!");
    }

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
