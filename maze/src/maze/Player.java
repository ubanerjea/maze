package maze;

import static util.Print.*;
import static util.Loggers.*;
import static maze.References.*;
import static util.Utilities.check;
import static util.Utilities.sign;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import maze.Maze.Room;
import maze.PaperDoll.EquipSlots;
import util.GrammarGuy;
import util.View;

public class Player extends AbstractUnitFighterMover
implements Fighter {
    private Maze maze;
    private Narrator narrator;
    private EncounterTracker tracker;
    private SpellBook spellBook;
    private List<Coordinate> path = new ArrayList<Coordinate>();
    private Inventory inventory = new Inventory();
    private PaperDoll paperDoll = new PaperDoll();

    private Random rand = new Random();

    private int dexterity;
    private int strength;
    private int intelligence;

    public void setMaze(Maze ref) { maze = ref; }

    public Maze maze() { return maze; }

    public Narrator narrator() { return this.narrator; }

    public EncounterTracker tracker() { return this.tracker; }

    public SpellBook getSpellBook() { return this.spellBook; }

    public List<Coordinate> getPath() { return path; }

    public int getDex() { return this.dexterity; }
    public int getStr() { return this.strength; }
    public int getInt() { return this.intelligence; }

    public Room getRoom() { return maze.getRoom(location); }

    public Player(Maze maze) {
        super(maze.center()); //creates Player at location c
        this.maze = maze;
        this.narrator = new Narrator();
        this.tracker = new EncounterTracker();
        this.spellBook = new SpellBook();
        this.path.add(maze.center());
    }

    @Override
    public void setLocation(Coordinate c) {
        log("Setting location to " + c + "...");
        if (location() != c) {
            log("Adding location " + c + " to path...");
            path.add(c);
        }
        super.setLocation(c);
    }

    public boolean skillCheck(References ref,int dmg, int difficulty) {
        int roll;
        int successes = 0;
        int playerSkill = 0;

        switch(ref) {
            case DEX:
                playerSkill = this.dexterity;
                break;
            case STR:
                playerSkill = this.strength;
                break;
            case INT:
                playerSkill = this.intelligence;
                break;
        }

        for (int i=0;i < playerSkill;i++) {
            roll = rand.nextInt(6)+1;
            log("You rolled a " + ((Integer)roll).toString());
            if (roll == 5 || roll == 6) {
                successes++;
            }
        }

        if (successes >= difficulty) {
            return true;
        } else {
            this.loseHP(dmg);
            return false;
        }
    }

    public void skillChange(References ref, int amt) {
        switch(ref) {
        case DEX:
            this.dexterity += amt;
            break;
        case STR:
            this.strength += amt;
            break;
        case INT:
            this.intelligence += amt;
            break;
        }
        if (amt > 0) {
            print("You have gained " + amt + " " + ref.toString() + ".");
        } else if (amt < 0) {
            print("You have lost " + Math.abs(amt) + " " + ref.toString() + ".");
        } else {
            print("Your skills remain the same.");
        }
    }

    public void printAttribs() {
        print("Attributes:");
        print(" Strength: " + strength);
        print(" Dexterity: " + dexterity);
        print(" Intelligence: " + intelligence);
        print(" Base Attack Value: " + attackVal);

        Equippable weapon = paperDoll.getWeapon();
        if (weapon != null) {
            print(" Equipped Weapon Damage: " + weapon.getStats().get(WDLOW) + " - " +
                    weapon.getStats().get(WDHIGH));
        }
    }

    @Override
    public int getDamage() {
        //base damage is 1 to strength value
        int baseDamage = rand.nextInt(this.strength) + 1;
        int damage;

        //retrieve weapon
        Equippable weapon = paperDoll.getWeapon();

        int plusDmg = 0;

        if (weapon != null) {
            HashMap<References, Integer> stats = weapon.getStats();
            plusDmg = rand.nextInt(stats.get(WDHIGH)-stats.get(WDLOW)) + stats.get(WDLOW);
            log(weapon + " increased attack damage by " + plusDmg + "...");
        }

        damage = baseDamage + plusDmg;

        return damage;
    }

    public boolean getCrit() {
        return skillCheck(DEX, 0, 2);
    }

    @Override //overrides AbstractFighterUnit method
    public void defineTypeDefaultValues() {
        super.defineTypeDefaultValues();
        this.defaultHitPoints = 1000;
        this.defaultAttackVal = 2;
        this.defaultDefenseVal = 0;
        this.dexterity = 4;
        this.strength = 4;
        this.intelligence = 4;
        //this.status = "healthy";
    }
    @Override //overrides AbstractFighterUnit method
    public String battlecry() {
        return "Ahhhh!!!!";
    }

    @Override
    public int attack(int enemyDefense, boolean crit) {
        Equippable weapon = paperDoll.getWeapon();
        int plusDmg = 0;
        if (weapon != null) {
            print("You attack with the " + weapon + ".");
            HashMap<References, Integer> stats = weapon.getStats();
            plusDmg = rand.nextInt(stats.get(WDHIGH)-stats.get(WDLOW)) + stats.get(WDLOW);
            log(weapon + " increased attack damage by " + plusDmg + "...");
        } else {
            print("You attack with your bare hands.");
        }

        int damage = this.attackVal + plusDmg;
        if (crit) {
            damage *= 2;
        }
        int result = damage - enemyDefense;
        return result < 0 ? 0 : result;
    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status);
        print("You have been afflicted with " + status);
    }


    @Override //overrides Unit method
    public void addHP(int hp) {
        print("You have gained " + hp + " health.");
        super.addHP(hp);
    }
    @Override //overrides Unit method
    public void loseHP(int hp) {
        if (hp > 0) {
            print("You have taken " + hp + " damage.");
            super.loseHP(hp);
        }
    }
    @Override //overrides Unit method
    public void death() {
        super.death();
        print("You have died.");
    }

    //begin implementation of mover abstract methods
    @Override
    public void whereCanIGo(Maze maze) {
        List<String> list = new ArrayList<String>();
        for (Cardinals d: maze.viewAvailableMoves(location)) {
            //printnb(d + " ");
            list.add(d.name());
        }
        print(GrammarGuy.oxfordCommify(list));
    }
    @Override
    public boolean move(Cardinals move, Maze maze) {
        boolean didMove = false;
        for (Coordinate neighbor : maze.viewNeighborsOf(location)) {
            Cardinals direction = Cardinals.get(location, neighbor);
            if (direction == move) {
                this.setLocation(neighbor);
                didMove = true;
              break;
            }
        }
      return didMove;
    }
    //begin dummy methods for interacter
    @Override
    public boolean matches(String name) {
      return name().equalsIgnoreCase(name);
    }
    @Override
    public boolean canInteract(AbstractUnit unit) {
      return true; //dummy method
    }
    @Override
    public Event interact(Commands trigger) {
      //dummy method, potential future use for player-to-player interaction
      return null;
    }
    @Override
    public boolean isDone(Stage stage) {
      return false; //dummy method
    }

    /*public void pickUp(Item i) {
        String s = i.getClass().getSimpleName();
        if(!inventory.contains(s)) {
            inventory.put(s,1);
        }
        else {
            inventory.put(s,inventory.get(s)+1);
        }
    }*/

    public boolean equip(Equippable item) {
        //checkNullArg(item);
        if (!inventory.contains(item)) {
            print("You do not have " + item + " in your inventory.");
          return false;
        }
        else {
            EquipSlots slot = item.type();
            Equippable oldItem = unequip(slot);
                    log("Removing " + item + " from inventory...");
            inventory.remove(item);
            paperDoll.add(item);

            if (oldItem != null) {
                    log("Returning " + oldItem + " to inventory...");
              inventory.add(oldItem);
            }

            alterSkillsFromItem(item, 1);

          return true;
        }
    }

    public Equippable unequip(Equippable item) {
        if (!paperDoll.contains(item)) return null;
        else {
            paperDoll.remove(item);

            alterSkillsFromItem(item, -1);

            log("Returning " + item + " to inventory...");
            inventory.add(item);
          return item;
        }
    }

    public Equippable unequip(EquipSlots slot) {
        Equippable item = paperDoll.getEquippedForSlot(slot);
        if (item != null) unequip(item);
      return item;
    }

    public void alterSkillsFromItem(Equippable item, int i) {
        HashMap<References, Integer> itemStats = item.getStats();
        int sign = sign(i);
         for (References key : itemStats.keySet()) {
             int changeVal = sign * itemStats.get(key); //positive or negative to reflect taking item on or off
             skillChange(key, changeVal);
         }
    }

    public void addToInventory(Portable item) { inventory.add(item); }

    public boolean deleteFromInventory(Portable item) {
      return (inventory.remove(item) != null);
    }

    public Portable dropFromInventory(Portable item) { return inventory.remove(item); }

    public Inventory getInventory() { return inventory; }

    public PaperDoll getPaperDoll() { return paperDoll; }

    public void printInventory() { print(inventory); }

    public void printPaperDoll() { print(paperDoll); }

    public void printStatus() {
        String statusList;
        statusList = util.GrammarGuy.oxfordCommify(status);
        if (status.isEmpty()) {
            print("You have " + hp() + " hitpoints and are otherwise fine.");
        }
        else {
            print("You have " + hp() + " hitpoints but you are afflicted with " + statusList + ".");
        }
    }
}
