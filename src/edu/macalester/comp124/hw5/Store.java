package edu.macalester.comp124.hw5;

/**
 * Created by oliverkoo on 3/25/14.
 */
public class Store extends Agent {
    public Store() {
        super("store");
    }

    @Override
    public int money() {
        return 10000000;
    }
}