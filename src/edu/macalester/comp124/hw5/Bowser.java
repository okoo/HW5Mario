package edu.macalester.comp124.hw5;

/**
 * Created by oliverkoo on 3/26/14.
 */
public class Bowser extends Agent {
    public Bowser() {
        super("bowser");
        strength = 7;
        defence = 0;
        hp = 50;
        money = 100;
    }


    @Override
    public int money() {
        return 0;
    }
}
