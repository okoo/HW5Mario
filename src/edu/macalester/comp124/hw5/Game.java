package edu.macalester.comp124.hw5;

import java.util.LinkedList;
import java.util.List;
import java.util.*;
import java.util.Scanner;


/**
 * @author baylor
 */
public class Game {
    public Map map;
    public Agent player;    // change this to whatever subclass player is
    public Agent store;
    public Agent bowser;
    public Monster monsters[] = new Monster[5];
    //--- A list of all the agents in the game (player, NPCs, monsters, etc.)
    //--- We need to know this so we know who to draw and so that we can ask
    //---	each turn what they want to do
    public List<Agent> agents = new LinkedList<>();


    public Game() {

        map = new Map("main");

        while (true) {
            Scanner console = new Scanner(System.in);
            player = new Player();
            player.x = 2;
            player.y = 2;
            System.out.println("you have 10 points, use it wisely");
            System.out.println("enter the strength of your character");
            int strength_int = console.nextInt();
            player.strength = strength_int;
            System.out.println("enter the defence of your character");
            int defense_int = console.nextInt();
            player.defence = defense_int;
            System.out.println("enter the HP of your character");
            int hp_int = console.nextInt();
            player.hp = hp_int;
            player.money = player.money();
            int total = player.hp + player.defence + player.strength;
            if (total > 10) {
                System.out.println("you cheater! you use more than 10!");
            } else if (player.hp == 0) {
                System.out.println("you cannot have a player with 0 hp!");
            } else {
                System.out.println("alright! lets start!");
                break;
            }
        }

        System.out.println("you character stats, will reprint if anything change");
        System.out.println("player strength: " + player.strength);
        System.out.println("player defence: " + player.defence);
        System.out.println("player hp: " + player.hp);
        System.out.println("player money: " + player.money());

        for (int i = 0; i < 5; i++) {
            monsters[i] = new Monster();
            spawnMonster(i);
            agents.add(monsters[i]);
        }


        store = new Store();
        store.x = 5;
        store.y = 5;
        store.money();

        bowser = new Bowser();
        bowser.x = 23;
        bowser.y = 16;

        //--- Add the player to the agents list. This list controls
        agents.add(player);
        agents.add(store);
        agents.add(bowser);


    }

    public void movePlayer(int x, int y) {
        //--- Don't do anything if the move is illegal

        String terrain = map.terrain[x][y];
        if (!map.passibility.containsKey(terrain)) {
            //--- Move the player to the new spot
            player.x = x;
            player.y = y;

        }
        //--- Assuming this is the last thing that happens in the round,
        //---	start a new round. This lets the other agents make their moves.

    }

    public void moveMonster(int index, int x, int y) {
        String terrain = map.terrain[x][y];
        if (!map.passibility.containsKey(terrain)) {

            monsters[index].x = x;
            monsters[index].y = y;
        }

    }

    int random_int1;
    int random_int2;

    public void moveMonsters() {
        Random dice = new Random();
        for (int i = 0; i < 5; i++) {
            random_int1 = (dice.nextInt(3) - 1);
            random_int2 = (dice.nextInt(3) - 1);
            moveMonster(i, monsters[i].x + random_int1, monsters[i].y + random_int2);
        }
    }

    public void movePlayer(char direction) {
        encounter();
        switch (direction) {
            case 'n':
                movePlayer(player.x, player.y - 1);
                break;
            case 's':
                movePlayer(player.x, player.y + 1);
                break;
            case 'e':
                movePlayer(player.x + 1, player.y);
                break;
            case 'w':
                movePlayer(player.x - 1, player.y);
                break;
        }
        moveMonsters();

    }

    /**
     * Run a turn. Did the player run into an enemy? An item?
     * What do the other agents (NPCs, monsters, etc.) want to do?
     */


    public void spawnMonster(int index) {
        while (true) {
            Random dice = new Random();
            int randomx = dice.nextInt(map.getWidth() - 2) + 1;
            int randomy = dice.nextInt(map.getHeight() - 2) + 1;

            if ((map.items[randomx][randomy] == null) && (map.terrain[randomx][randomy].equals("."))) {
                monsters[index].x = randomx;
                monsters[index].y = randomy;
                break;
            }
        }
    }





    public void encounter() {
        //check if encounter store
        if ((player.x == store.x) && (player.y == store.y)) {
            inStore();
        }
        //check if run into monster
        for (int i = 0; i < 5; i++) {
            if ((monsters[i].x == player.x) && (monsters[i].y == player.y)) {
                onTouchMonster(i);
            }
        }
        //check if run into gold
        if (map.items[player.x][player.y] != null) {


            if (map.items[player.x][player.y].equals("g")) {
                onGOld(player.x, player.y);
            }
        }
        //check if run iinto bowser
        if ((player.x == bowser.x) && (player.y == bowser.y)) {
            onTouchBowser();
        }
        //check if step on star
        if (map.items[player.x][player.y] != null) {
            if (map.items[player.x][player.y].equals("s")) {
                win();
            }
        }

    }

    public void onGOld(int x, int y) {
        System.out.println("you step on gold! earned 5 gold coins");
        player.money += 5;
        map.items[x][y] = null;
        System.out.println("player money: " + player.money);
    }

    public void onTouchMonster(int index) {
        Scanner console = new Scanner(System.in);
        System.out.println("you step on a monster!");
        System.out.println("--------monster's------");
        System.out.println("hp: " + monsters[index].hp + "strength: " + monsters[index].strength + "defence: " + monsters[index].defence + "money:" + monsters[index].money);
        System.out.println("player current stats");
        System.out.println("player strength: " + player.strength);
        System.out.println("player defence: " + player.defence);
        System.out.println("player hp: " + player.hp);
        System.out.println("player money: " + player.money());
        System.out.println("1.battle 2.RUNNNNN");
        int userChoice = console.nextInt();
        if (userChoice == 1) {
            //choose to battle
            System.out.println("battle now");
            while (true) {
                System.out.println("player's move");
                System.out.println("1.attack");
                int userMove = console.nextInt();
                if (userMove == 1) {
                    monsters[index].hp = monsters[index].hp - player.strength;
                    System.out.println("--------monster's------");
                    System.out.println("hp: " + monsters[index].hp);
                    if (monsters[index].hp <= 0) {
                        System.out.println("you win the battle");
                        player.money += monsters[index].money;
                        System.out.println("player hp: " + player.hp);
                        System.out.println("player money: " + player.money);
                        spawnMonster(index);
                        break;
                    }
                    System.out.println("monster's turn. It choose to attack you");
                    player.hp = player.hp - monsters[index].strength;
                    System.out.println("player current stats");
                    System.out.println("player hp: " + player.hp);
                    if (player.hp <= 0) {
                        System.out.println("oh no you died");
                        quit();
                        break;
                    }
                }
            }
        }
        if (userChoice == 2) {
            //choose to run
            System.out.println("player choose to run away");
            if (player.defence > monsters[index].defence) {
                System.out.println("you successful ran away!");
            } else {
                System.out.println("sorry, Mario but your defense is not high enough to let you run away");
                System.out.println("you have to battle now");
                while (true) {
                    System.out.println("player's move");
                    System.out.println("1.attack");
                    int userMove = console.nextInt();
                    if (userMove == 1) {
                        monsters[index].hp = monsters[index].hp - player.strength;
                        System.out.println("--------monster's------");
                        System.out.println("hp: " + monsters[index].hp);
                        if (monsters[index].hp <= 0) {
                            System.out.println("you win the battle");
                            player.money += monsters[index].money;
                            System.out.println("player hp: " + player.hp);
                            System.out.println("player money: " + player.money);
                            spawnMonster(index);
                            break;
                        }
                        System.out.println("monster's turn. It choose to attack you");
                        player.hp = player.hp - monsters[index].strength;
                        System.out.println("player current stats");
                        System.out.println("player hp: " + player.hp);
                        if (player.hp <= 0) {
                            System.out.println("oh no you died");
                            quit();
                            break;
                        }
                    }
                }
            }


            //--- Time to fight
//		CombatForm form = new CombatForm();
//		form.game = this;	// let them know about us so they can talk to us
//		form.enemies = ???;
//		form.run();
        }
    }

    public void onTouchBowser() {
        System.out.println("you run into Bowser");
        System.out.println("--------Bowser's------");
        System.out.println("hp: " + bowser.hp + "strength: " + bowser.strength + "defence: " + bowser.defence);
        System.out.println("player current stats");
        System.out.println("player strength: " + player.strength);
        System.out.println("player defence: " + player.defence);
        System.out.println("player hp: " + player.hp);
        System.out.println("1.battle 2.RUNNNNN");
        Scanner console = new Scanner(System.in);
        int userChoice = console.nextInt();
        if (userChoice == 1) {
            //choose to battle
            System.out.println("battle now");
            while (true) {
                System.out.println("player's move");
                System.out.println("1.attack");
                int userMove = console.nextInt();
                if (userMove == 1) {
                    bowser.hp = bowser.hp - player.strength;
                    System.out.println("--------Bowser's------");
                    System.out.println("hp: " + bowser.hp);
                    if (bowser.hp <= 0) {
                        System.out.println("you win the battle");
                        player.money += bowser.money;
                        System.out.println("player hp: " + player.hp);
                        System.out.println("player money: " + player.money);
                        break;
                    }
                    System.out.println("Bowser's turn. It choose to attack you");
                    player.hp = player.hp - bowser.strength;
                    System.out.println("player current stats");
                    System.out.println("player hp: " + player.hp);
                    if (player.hp <= 0) {
                        System.out.println("oh no you died");
                        quit();
                        break;
                    }
                }
            }
        }
        if (userChoice == 2) {
            //choose to run
            System.out.println("player choose to run away");
            if (player.defence > bowser.defence) {
                System.out.println("you successful ran away!");
                player.x = 23;
                player.y = 15;
            }
        }
    }

    public void inStore() {
        Scanner console = new Scanner(System.in);
        System.out.println("welcome to Mario Store");
        System.out.println("you currently have: " + player.money + " gold coins");
        System.out.println("1.buy strength 2.buy defense 3.restore hp");
        int strength_int = console.nextInt();
        if (strength_int == 1) {
            System.out.println("every strength point cost a gold coins, how many points do you want? ");
            int howMany = console.nextInt();
            if (howMany >= player.money()) {
                player.strength = player.strength + howMany;
                player.money = player.money - howMany;
                System.out.println("player current stats");
                System.out.println("player strength: " + player.strength);
            } else {
                System.out.println("sorry Mario, but you don't have enough gold coins");
            }
        }
        if (strength_int == 2) {
            System.out.println("every defence point cost a gold coins, how many points do you want? ");
            int howMany = console.nextInt();
            if (howMany >= player.money()) {
                player.defence = player.defence + howMany;
                player.money = player.money - howMany;
                System.out.println("player current stats");
                System.out.println("player defence: " + player.defence);
            } else {
                System.out.println("sorry Mario, but you don't have enough gold coins");
            }
        }
        if (strength_int == 3) {
            System.out.println("every hp point cost a gold coins, how many points do you want? ");
            int howMany = console.nextInt();
            if (howMany >= player.money()) {
                player.hp = player.hp + howMany;
                player.money = player.money - howMany;
                System.out.println("player current stats");
                System.out.println("player hp: " + player.hp);
            } else {
                System.out.println("sorry Mario, but you don't have enough gold coins");
            }
        }

    }

    public void win() {
        //make player win
        System.out.println(" you got the golden star! you win the game!");
        quit();

    }

    public void quit() {
        System.exit(0);
    }


}
