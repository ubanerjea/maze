package maze;

import static util.Print.*;
import static maze.References.*;
import static maze.Priority.*;
import static maze.Commands.*;
import static maze.Events.*;
import static util.Loggers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Bestiary {
    public static List<Monster> monsterParty = new ArrayList<Monster>();

    public static abstract class Monster extends AbstractUnitFighter {
        Inventory inventory = new Inventory();
        private Random rand = new Random();

        public Monster() {
            super();
            fillInventory();
        }
        public Monster(Coordinate c) { super(c); }

        private void fillInventory() {
            int x = rand.nextInt(4);
            for (int i = 0; i < x; i++) {
                this.inventory.add(new Trinkets.BronzeCoin());
            }
        }

        public Inventory getInventory() {
            return inventory;
        }

        @Override
        public boolean canInteract(AbstractUnit unit) {
          return unit instanceof Fighter ? true : false;
        }
        @Override
        public boolean isDone(Stage stage) {
          return isAlive ? false : true;
        }

        public void buff() { }

        @Override
        public Event interact(Commands trigger) {
            if (!isAlive) return null;

            if (trigger == FIGHT) {
                monsterParty.add(this);
                log("Adding " + this + " to monster party..");
                log(monsterParty.toString());

                Monster partyRep = new PartyRep();
                return Events.fightAll(monsterParty, partyRep, HIGH);
            }

            /*
            return trigger == FIGHT ? fight(this, HIGH)
                : (trigger == APPROACH ? announce(this, LOW, inspect())
                :  null);
                */

            return trigger == APPROACH ? announce(this, LOW, inspect())
                    :  null;
        }
    }

    public static class PartyRep extends Monster {

        @Override
        public boolean matches(String name) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String battlecry() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isDone(Stage stage) {
          if (monsterParty.size() == 0)
              return true;
          return false;
        }
    }

    public static class Skeleton extends Monster {
        public static final int classId = SKELETON.classId;
        public static final References ref = SKELETON;
        //public static final String battlecry = "The skeleton flashes you a toothy grin as it slowly raises a rusty sword.";
        public Skeleton() {
            super();
            statusList.add("undead");
        }

        public Skeleton(Coordinate c) {
            super(c);
            statusList.add("undead");
        }

        private void selfPrint(String input) { print(name + ">> " + input); }

        @Override //overrides AbstractFighterUnit method
        public void defineTypeDefaultValues() {
            this.defaultHitPoints = 8;
            this.defaultAttackVal = 1;
            this.defaultDefenseVal = 0;
            this.damageLow = 2;
            this.damageHigh = 4;
        }
        @Override //overrides AbstractFighterUnit method
        public String battlecry() {
          return "The skeleton flashes you a toothy grin as it slowly raises a rusty sword.";
        }

        @Override
        public String inspect() {
            return ("This reanimated skeleton is poorly constructed.  You are confident you could defeat it in battle.");
        }

        @Override //overrides Unit method
        public void death() {
            if (isAlive) {
              selfPrint("crrk...crrrrk.... ");
              print("The skeleton crumbles before you.");
            }
            super.death();
        }
        //begin implementation of Interacter methods defined as abstract in FighterUnit
        @Override
        public boolean matches(String name) {
          return matchRef(SKELETON, name);
        }

        @Override
        public Event interact(Commands trigger) {
            Event result = super.interact(trigger);

            if (result == null && !isAlive) {
                if (trigger == MOVE) result = announce(this, DEFAULT, "You hear a rattle of bones");
            }
            return result;
        }
    }

    public static class Revanton extends Monster {
        public static final int classId = REVANTON.classId;
        public static final References ref = REVANTON;
        //public static final String battlecry = "The skeleton flashes you a toothy grin as it slowly raises a rusty sword.";
        public Revanton() {
            super();
        }

        public Revanton(Coordinate c) {
            super(c);
        }

        private void selfPrint(String input) { print(name + ">> " + input); }

        @Override //overrides AbstractFighterUnit method
        public void defineTypeDefaultValues() {
            this.defaultHitPoints = 60;
            this.defaultAttackVal = 8;
            this.defaultDefenseVal = 8;
            this.damageLow = 8;
            this.damageHigh = 12;
        }
        @Override //overrides AbstractFighterUnit method
        public String battlecry() {
          return "The elf rogue stealthily waddles toward you.";
        }

        @Override
        public String inspect() {
            return ("All you see are shadows.");
        }

        @Override //overrides Unit method
        public void death() {
            if (isAlive) {
              selfPrint("Revanton will return!");
              print("You spit on the elf's corpse.");
            }
            super.death();
        }
        //begin implementation of Interacter methods defined as abstract in FighterUnit
        @Override
        public boolean matches(String name) {
          return matchRef(REVANTON, name);
        }

        @Override
        public Event interact(Commands trigger) {
            Event result = super.interact(trigger);

            if (result == null && !isAlive) {
                if (trigger == MOVE) result = announce(this, DEFAULT, "You see something moving in "
                        + "the "
                        + "shadows.");
            }
            return result;
        }
    }

    public static class Rat extends Monster {
        public static final int classId = RAT.classId;

        public Rat() {
            super();
            statusList.add("beast");
        }

        public Rat(Coordinate c) {
            super(c);
            statusList.add("beast");
        }

        private void selfPrint(String input) { print(name + ">> " + input); }

        @Override //overrides AbstractFighterUnit method
        public void defineTypeDefaultValues() {
            super.defineTypeDefaultValues();
            this.defaultHitPoints = 4;
            this.defaultAttackVal = 1;
            this.defaultDefenseVal = 0;
            this.damageLow = 1;
            this.damageHigh = 2;
        }
        @Override //overrides AbstractFighterUnit method
        public String battlecry() {
          return "The rat squeaks in alarm.";
        }

        @Override
        public String inspect() {
            return ("It's one of those rats you first encounter when you're just starting out in a dungeon.");
        }

        @Override //overrides Unit method
        public void death() {
            if (isAlive) {
              selfPrint("The rat king will have his revenge!");
              print("The tiny rat collapses.  You feel bad.");
            }
            super.death();
        }
        //begin implementation of Interacter methods defined as abstract in FighterUnit
        @Override
        public boolean matches(String name) {
          return matchRef(RAT, name);
        }

        @Override
        public Event interact(Commands trigger) {
            Event result = super.interact(trigger);

            if (result == null && !isAlive) {
                if (trigger == MOVE) result = announce(this, DEFAULT, "You hear squeaking of a "
                        + "non-mechanical nature");
            }
            return result;
        }
    }

    public static class RabidRat extends Rat {
        public static final int classId = RABIDRAT.classId;

        public RabidRat() {
            super();
            this.name = "Rabid Rat";
        }

        public RabidRat(Coordinate c) {
            super(c);
            this.name = "Rabid Rat";
        }

        private void selfPrint(String input) { print(name + ">> " + input); }

        @Override //overrides AbstractFighterUnit method
        public void defineTypeDefaultValues() {
            super.defineTypeDefaultValues();
            this.defaultAttackVal = 2;
            this.statusEffect = "rabies";
            this.damageLow = 1;
            this.damageHigh = 3;
        }
        @Override //overrides AbstractFighterUnit method
        public String battlecry() {
          return "The rabid rat squeaks in frothy alarm.";
        }

        @Override
        public String inspect() {
            return ("The rat is foaming at the mouth.");
        }

        @Override //overrides Unit method
        public void death() {
            if (isAlive) {
              selfPrint("The rat king will have his revenge!");
              print("The tiny rat collapses. You feel bad.");
            }
            this.isAlive = false;
        }
        //begin implementation of Interacter methods defined as abstract in FighterUnit
        @Override
        public boolean matches(String name) {
          return matchRef(RABIDRAT, name);
        }

        @Override
        public Event interact(Commands trigger) {
            Event result = super.interact(trigger);

            if (result == null && !isAlive) {
                if (trigger == MOVE) result = announce(this, DEFAULT, "You hear squeaking of a "
                        + "non-mechanical nature.");
            }
            return result;
        }
    }

    public static class RatKing extends Rat {
        public static final int classId = RABIDRAT.classId;
        private int ratBuff;

        public RatKing() { //sets values to default
            super();
            this.name = "Rat King";
            this.ratBuff = 2 * Statistics.globalGet("ratKillCount"); //initially 0
        }

        public RatKing(Coordinate c) {
            super(c);
            this.name = "Rat King";
        }

        private void selfPrint(String input) { print(name + ">> " + input); }

        @Override
        public void buff() { //buffs stats based on # rats dead
            int x = 2 * Statistics.globalGet("ratKillCount"); //2 * # of rats killed
            if (x > this.ratBuff) { //if greater than previous buff amount
                this.ratBuff = x;

                this.resetAttack();
                this.resetDefense();
                this.resetHP();

                this.attackVal = this.defaultAttackVal + this.ratBuff;
                this.defenseVal = this.defaultDefenseVal + this.ratBuff;
                this.hitPoints = this.defaultHitPoints + this.ratBuff;

                this.defineTypeDefaultValues();
                this.damageLow += this.ratBuff;
                this.damageHigh += this.ratBuff;
            }
        }

        @Override //overrides AbstractFighterUnit method
        public void defineTypeDefaultValues() {
            super.defineTypeDefaultValues();
            this.defaultAttackVal = 10;
            this.defaultHitPoints = 10;
            this.damageLow = 8;
            this.damageHigh = 10;
        }
        @Override //overrides AbstractFighterUnit method
        public String battlecry() {
          return ("A multitude of rat voices squeak in unison. \nSomehow, the combined cries "
                    + "of the rats is able to form a voice understandable to you.\n"
                    + "'You have killed " + Statistics.globalGet("ratKillCount") + " of our "
                    + "brethren. Prepare to receive your comeuppance!'");
        }

        @Override
        public String inspect() {
            return ("A disgusting mass of rats, both alive and dead, writhes in front of you. The "
                    + "shape constantly changes but there is a uniformity in movement that makes "
                    + "you believe that the thing acts as one unit.");
        }

        @Override //overrides Unit method
        public void death() {
            if (isAlive) {
              print("Countless rat corpses lie at your feet.");
              print("You have earned the title, 'The Exterminator.'");
            }
            this.isAlive = false;
        }
        //begin implementation of Interacter methods defined as abstract in FighterUnit
        @Override
        public boolean matches(String name) {
          return matchRef(RATKING, name);
        }

        @Override
        public Event interact(Commands trigger) {
            Event result = super.interact(trigger);

            if (result == null && !isAlive) {
                if (trigger == MOVE) result = announce(this, DEFAULT, "You hear a chorus of "
                        + "squeaking, "
                        + "spanning a spectrum from bass to soprano.");
            }
            return result;
        }
    }
}
