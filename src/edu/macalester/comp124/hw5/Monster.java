package edu.macalester.comp124.hw5;

/**
 * Created by oliverkoo on 3/25/14.
 */
public class Monster extends Agent {
    public Monster() {
        super("monster");
        strength = 2;
        defence = 1;
        hp = 3;
        money = 2;
    }


    @Override
    public int money() {
        return 0;
    }
}