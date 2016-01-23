package pk.risiko.pojo;

/**
 * This Entity does store 3 Generic values (x,y,z)
 * Each of these values can have another type
 *
 * @author Raphael Ludwig
 * @version 20.01.2016
 */
public class Tripel<T,K,J> {

    public T x;
    public K y;
    public J z;
    
    public Tripel(T x, K y,J z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
