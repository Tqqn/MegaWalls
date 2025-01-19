package dev.tqqn.megawalls.modules.classes.framework;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * The AbstractClass abstract class defines the basic structure and behavior of a player class in the game.
 * It provides methods for handling main abilities, secondary abilities, gathering abilities, and other class-related actions.
 * Classes extending this abstract class must implement specific abilities and behaviors.
 */
@Getter
public abstract class AbstractClass implements Listener {

    @Getter private static final ItemStack ENDER_CHEST = ItemBuilder.getBuilder(Material.ENDER_CHEST).setLocalizedName("kit").addPDCTag("enderchest", "dummy").build();

    private final String name;
    private final String tag;
    private final ClassOptions classOptions;
    private final Skins skins;
    private final int inventorySlot;
    private final Map<Integer, ItemStack> kitItems;
    private final Collection<ItemStack> kitArmor;

    @Setter private boolean isPrestigeOne;
    @Setter private boolean isPrestigeTwo;
    @Setter private boolean isPrestigeThree;
    @Setter private boolean isPrestigeFour;

    /**
     * Constructs an AbstractClass object with the specified name, tag, class options, and inventory slot.
     *
     * @param name The name of the class.
     * @param tag The tag representing the class.
     * @param classOptions The options associated with the class.
     * @param inventorySlot The inventory slot for the class icon.
     */
    public AbstractClass(String name, String tag, ClassOptions classOptions, int inventorySlot, Skins skins) {
        this.name = name;
        this.tag = "[" + tag + "]";
        this.classOptions = classOptions;
        this.skins = skins;
        this.inventorySlot = inventorySlot;
        this.kitItems = new HashMap<>();
        this.kitArmor = new ArrayList<>();
        this.isPrestigeOne = false;
        this.isPrestigeTwo = false;
        this.isPrestigeThree = false;
        this.isPrestigeFour = false;
    }

    public abstract void initKitItems();
    public abstract void executeAbility(PlayerModel playerModel);
    public abstract String getActionBar(PlayerModel playerModel);

    public void applyKit(PlayerModel playerModel) {
        final Player player = playerModel.getPlayer();
        if (player == null) return;

        for (Map.Entry<Integer, ItemStack> entry : kitItems.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }
        player.getInventory().setArmorContents(kitArmor.toArray(new ItemStack[0]));
    }

    public void applySkin(PlayerModel playerModel) {
        MegaWalls.getReflectionLayer().changeSkin(skins, playerModel);
    }

    /**
     * Checks if the player can use an ability based on energy level.
     *
     * @param playerModel The PlayerModel object representing the player.
     * @return {@code true} if the player can use the ability, {@code false} otherwise.
     */
    public boolean canUseAbility(PlayerModel playerModel) {
        return playerModel.getTempPlayerData().getEnergy() >= 100;
    }

    public List<String> getKitAbilityLore() {
        final List<String> lore = new ArrayList<>();
        lore.add("&7Ability: &c" + classOptions.getClassSkillDescription().getName());
        lore.add(" ");
        lore.addAll(Arrays.asList(classOptions.getClassSkillDescription().getDescription()));

        return lore;
    }

    /**
     * Retrieves the ItemStack representing the class icon for the specified player.
     *
     * @param playerModel The PlayerModel object representing the player.
     * @return The ItemStack representing the class icon.
     */
    public ItemStack getKitIcon(PlayerModel playerModel) {
        final List<String> lore = new ArrayList<>();
        final AbstractClass currentClass = playerModel.getTempPlayerData().getCurrentClass();

        lore.add(classOptions.getClassType().getType());
        lore.add("&7Play Styles:");
        for (ClassDescriptions.ClassStyle classStyle : classOptions.getClassStyles()) {
            lore.add("&7- " + classStyle.getStyle());
        }
        lore.add("&7Difficulty: " + classOptions.getClassDifficulty().getDifficulty());
        lore.add("&7Diamond: " + classOptions.getClassDiamond().getDiamond());
        lore.add(" ");
        lore.add("&eSkill &7- " + classOptions.getClassSkillDescription().getName());
        lore.addAll(Arrays.asList(classOptions.getClassSkillDescription().getDescription()));
        lore.add(" ");
        lore.add("&7Cooldown: &a&l1s");
        lore.add(" ");
        lore.add("&7Upgrades: &a&l100%");
        lore.add("&7Ender Chest: &a&l5 rows");
        lore.add(" ");
        if (currentClass != null && currentClass.getClass() == this.getClass()) {
            lore.add("&a&lSelected!");
        } else {
            lore.add("&eClick to select!");
        }

        return ItemBuilder.getBuilder(classOptions.getClassType().getIcon()).setDisplayName("&a" + name).setLore(lore).hideAttributes().build();
    }

    /**
     * Retrieves the tag representing the class for the specified player.
     *
     * @param playerModel The PlayerModel object representing the player.
     * @return The tag representing the class.
     */
    public String getTag(PlayerModel playerModel) {
        String tagColor = "§7";

        if (playerModel.getTempPlayerData().getCurrentClass().isPrestigeFour) tagColor = "§6";

        return tagColor + tag;
    }

    /**
     * Handles the event when a player hits another player with a bow.
     *
     * @param playerModel The PlayerModel object representing the player.
     */
    public void onPlayerHitBow(PlayerModel playerModel) {
        playerModel.getTempPlayerData().increaseEnergy(getClassOptions().getClassEnergy().getEnergyPerBowShot());
    }

    /**
     * Handles the event when a player hits another player.
     *
     * @param damager The PlayerModel object representing the player causing the damage.
     */
    public void onChargedPlayerHit(PlayerModel damager) {
        damager.getTempPlayerData().increaseEnergy(getClassOptions().getClassEnergy().getEnergyPerHit());
    }

    public void onNonChargedPlayerHit(PlayerModel damager) {
        damager.getTempPlayerData().increaseEnergy(getClassOptions().getClassEnergy().getEnergyPerHit() / 3);
    }

    public void onTakenHit(PlayerModel playerModel) {
        //Empty Method to override!
    }

    public void onTakenBowHit(PlayerModel playerModel) {
        // Empty Method to override!
    }

    /**
     * Handles the event when a player interacts with the environment.
     *
     * @param event The PlayerInteractEvent representing the interaction event.
     */
    public void onInteract(PlayerInteractEvent event) {
        // Empty Method to override!
    }

    /**
     * Handles the event when a player places a block.
     *
     * @param event The BlockPlaceEvent representing the block placement event.
     */
    public void onBuild(BlockPlaceEvent event) {
        //Empty Method to override!
    }

    public void onBreak(BlockBreakEvent event) {
        //Empty Method to override!
    }

    public void onPotionConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) return;
        final Player player = event.getPlayer();
        final ItemMeta itemMeta = event.getItem().getItemMeta();
        if (!itemMeta.getPersistentDataContainer().has(ClassModule.POTION_HEAL_KEY, PersistentDataType.DOUBLE)) return;
        double heal = itemMeta.getPersistentDataContainer().get(ClassModule.POTION_HEAL_KEY, PersistentDataType.DOUBLE);
        if (heal == 0) return;

        double newHealth = player.getHealth() + heal;
        final double currentHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (newHealth >= currentHealth) {
            newHealth = currentHealth;
        }

        player.setHealth(newHealth);
        extraPotionConsume(event);
    }

    public void extraPotionConsume(PlayerItemConsumeEvent event) {
        // Empty Method to override!
    }
}
