package game.objects.general;

import game.objects.items.Portable;
import game.objects.items.Trinkets.*;
import game.objects.items.Useables.*;
import game.objects.items.Weapons.*;

public class Combinations {

    public static Portable combine(Portable firstItem, Portable secondItem) {
        if ((firstItem instanceof RedKey && secondItem instanceof BlueKey) ||
                (firstItem instanceof BlueKey && secondItem instanceof RedKey)) {
            return new PurpleKey();
        } else if ((firstItem instanceof OilyRag && secondItem instanceof WoodenStick ) ||
                (firstItem instanceof WoodenStick && secondItem instanceof OilyRag)) {
            return new UnlitTorch();
        } else if ((firstItem instanceof UnlitTorch && secondItem instanceof Matches ) ||
                (firstItem instanceof Matches && secondItem instanceof UnlitTorch)) {
            return new LitTorch();
        } else return null;
    }

}
