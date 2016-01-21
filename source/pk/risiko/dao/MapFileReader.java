package pk.risiko.dao;

import pk.risiko.pojo.Capital;
import pk.risiko.pojo.Continent;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Territory;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class does read the given Mapfiles for the Game.
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 27.12.2015
 */
public class MapFileReader {

    /*
    * TODO: Implement MapFileReader
    *   > store assets directory / map file directory
    *   > public GameMap readMap(String name) throws MapFileCorruptedException {} ?
    * */
    private String directory;
    private Map<String, Territory> territories = new HashMap<String,Territory>();
    private ArrayList<Continent> continentList = new ArrayList<Continent>();

    public MapFileReader(String d)
    {
        directory=d;
    }

    private String getName(String[] arr)
    {
        String s="";
        for (int i = 1; i < arr.length; i++) {
            if (isInteger(arr[i]))
                break;
            s+=arr[i]+" ";
        }
        return s.trim();
    }
    private boolean isInteger(String s)
    {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
    private Polygon createPolygon(String[] arr)
    {
        Polygon p = new Polygon();

        for (int i = 2; i < arr.length;) {
            if (!isInteger(arr[i]))
                i++;
            else {
                p.addPoint(Integer.parseInt(arr[i]), Integer.parseInt(arr[i + 1]));
                i+=2;
            }
        }

        return p;
    }
    public GameMap readMap(String name)
    {
        /*BUG: multiple map loads produce funny maps, clear map: (why save them?) */
        this.territories.clear();
        this.continentList.clear();

        String line="";
        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(directory+name));
            while ((line=br.readLine()) != null) {
                //System.out.println(line);

                String[] arr=line.split(" ");
                String tname=getName(arr);


                switch(arr[0])
                {
                    case "patch-of":
                        if(territories.get(tname)==null)
                            territories.put(tname,new Territory(tname, new Area(createPolygon(arr))));
                        else
                        {
                            Territory t = territories.get(tname);
                            t.getArea().add(new Area(createPolygon(arr)));
                        }
                        break;
                    case "capital-of":
                        Territory t = territories.get(tname);
                        t.setCapital(new Capital(Integer.parseInt(arr[arr.length-2]),Integer.parseInt(arr[arr.length-1])));
                        break;
                    case "neighbors-of":
                        String[] neighborsinfo = tname.split(":");
                        Territory main = territories.get(neighborsinfo[0].trim());


                        String[] neighbors = neighborsinfo[1].split("-");

                        for (int i = 0; i < neighbors.length; i++) {
                            Territory neighbor = territories.get(neighbors[i].trim());
                            main.getNeighbours().add(neighbor);
                            neighbor.getNeighbours().add(main);
                        }
                        break;
                    case "continent":
                        // tname is now name of the continent
                        String[] continentinfo=line.split(":");
                        String[] a =continentinfo[0].split(" ");
                        int continentvalue= Integer.parseInt(arr[a.length-1]);
                        String[] continentterritories = continentinfo[1].split("-");

                        Continent continent = new Continent(tname, continentvalue);

                        for (int i = 0; i < continentterritories.length; i++) {
                            continent.addTerritory(territories.get(continentterritories[i].trim()));
                        }
                        continentList.add(continent);
                        break;
                }
                //if(tname.equals("GreatBritain"))
                //    break;
            }
        }
        catch (IOException e) {
            assert false : "IOException while loading ("+name+") mapfile, failing fast in debug mode!";

            System.err.println("Error: " + e);
            e.printStackTrace();
        }
        finally
        {
            if (br!=null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if (continentList.size()==0)
        {
            Continent c = new Continent("continent", 0);
            for (int i = 0; i < territories.size(); i++) {
                c.addTerritory(territories.values());
            }
            continentList.add(c);
        }
        return new GameMap(name, new ArrayList<>(territories.values()), continentList);
    }

    public List<String> getAvailableMapFiles() {
        List<String> mapFileNames = new LinkedList<>();
        try {
            Files.list(Paths.get(this.directory)).forEach(p -> mapFileNames.add(p.getFileName().toString()));

        } catch (IOException e) {
            System.err.println("Unable to list directory content");
            e.printStackTrace();

            assert false : "Unable to list directory content, abort in debug mode!";
        }



        return mapFileNames;
    }

}
