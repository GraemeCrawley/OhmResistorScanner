package ca.ryanmarks.ohm;

/**
 * Created by Ryan on 11/20/2016.
 */
public class Pair<T, T1> {
    private final T key;
    private final T1 val;

    public Pair(T key, T1 val){
        this.key = key;
        this.val = val;
    }

    public T getKey(){
        return key;
    }

    public T1 getValue(){
        return val;
    }
}
