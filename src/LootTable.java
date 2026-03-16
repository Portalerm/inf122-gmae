import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTable {
    private static final Random random = new Random();

    private static final String[][] ITEM_NAMES = {
        {"Rusty Sword", "Worn Shield", "Tattered Cloak", "Simple Ring"},
        {"Steel Blade", "Iron Buckler", "Traveler's Cloak", "Amulet of Focus"},
        {"Flamebrand Sword", "Guardian Shield", "Cloak of Shadows", "Ring of Power"},
        {"Dragonslayer Blade", "Aegis of Light", "Mantle of Stars", "Crown of Wisdom"},
        {"Godsbane", "Eternal Aegis", "Veil of Infinity", "Scepter of Ages"}
    };

    public static List<Item> generateVictoryLoot() {
        List<Item> loot = new ArrayList<>();
        loot.add(createRandomItem(Rarity.RARE));
        loot.add(createRandomItem(Rarity.EPIC));
        return loot;
    }

    public static Item generateMonsterDrop() {
        if (random.nextDouble() < 0.5) {
            Rarity rarity = random.nextBoolean() ? Rarity.COMMON : Rarity.UNCOMMON;
            return createRandomItem(rarity);
        }
        return null;
    }

    public static Item generateReward() {
        return createRandomItem(Rarity.UNCOMMON);
    }

    private static Item createRandomItem(Rarity rarity) {
        int tier = rarity.ordinal();
        String name = ITEM_NAMES[tier][random.nextInt(ITEM_NAMES[tier].length)];
        String description = "A " + rarity.name().toLowerCase() + " item found during a raid.";
        return new Item(new ItemInfo(name, description, rarity));
    }

    public static int[] lootTableLookup(String itemName) {
        for (int i = 0; i < ITEM_NAMES.length; i++) {
            for (int j = 0; j < ITEM_NAMES[i].length; j++) {
                if (ITEM_NAMES[i][j].equals(itemName)) {
                    return new int[]{i+1, j+1};
                }
            }
        }
        return null;
    }
}
