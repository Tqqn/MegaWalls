package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.common.classes.ClassSkins;
import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;
import dev.tqqn.megawalls.common.classes.levels.types.kits.object.KitUpgrade;
import dev.tqqn.megawalls.common.utils.ItemBuilder;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.classes.framework.objects.AbstractClass;
import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import dev.tqqn.megawalls.common.classes.ClassOptions;
import dev.tqqn.megawalls.modules.classes.runnables.ZombieStrenghtRunnable;
import dev.tqqn.megawalls.common.utils.cooldown.CooldownUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Zombie class extends AbstractClass and represents the Zombie class in the plugin.
 * It defines the characteristics and abilities specific to the Zombie class.
 */
public final class Zombie extends AbstractClass {

    private static final Set<Material> GATHERING_ACTIVE_BLOCKS = Set.of(Material.ACACIA_LOG, Material.BIRCH_LOG, Material.CHERRY_LOG, Material.JUNGLE_LOG, Material.DARK_OAK_LOG, Material.SPRUCE_LOG, Material.OAK_LOG, Material.STONE, Material.COAL_ORE, Material.IRON_ORE, Material.DIAMOND_ORE);

    private final Map<UUID, Integer> toughnessHits;
    @Getter private final List<UUID> isStrenght;
    @Getter private final Map<UUID, ZombieStrenghtRunnable> strenghtRunnableMap;

    public Zombie() {
        super(ClassDescriptions.ClassType.ZOMBIE, "ZOM", new ClassOptions(ClassDescriptions.ClassEnergy.ZOMBIE, ClassDescriptions.ClassType.ZOMBIE, ClassDescriptions.ClassDifficulty.ZOMBIE, Arrays.asList(ClassDescriptions.ClassStyle.TANK, ClassDescriptions.ClassStyle.SUPPORT), ClassDescriptions.ClassDiamond.ZOMBIE, ClassDescriptions.ClassSkillDescription.ZOMBIE), 5, ClassSkins.ZOMBIE, getClassModule().getClassUpgradeValueFromDB("ZOMBIE"));
        this.toughnessHits = new HashMap<>();
        this.isStrenght = new ArrayList<>();
        this.strenghtRunnableMap = new ConcurrentHashMap<>(); //synchronized map
    }

    @Override
    public void initKitItems() {
        getClassUpgradeValues().getClassKit().addUpgrade(KitUpgrade.getBuilder(ClassUpgrades.UpgradeLevel.I).addValue(ItemBuilder.getBuilder(Material.IRON_SWORD).setDisplayName("<green>Zombie Sword").setGlow()));
//        getKitItems().put(0, ItemBuilder.getBuilder(Material.IRON_SWORD).setDisplayName("<dark_green>Zombie Sword").setLore(getKitAbilityLore()).setGlow().setUnbreakable().addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).addEmptyPDCTag(ClassModule.CLASS_ABILITY_ITEM_KEY).build());
//        getKitItems().put(1, ItemBuilder.getBuilder(Material.BOW).setDisplayName("<dark_green>Zombie Bow").setLore(getKitAbilityLore()).setUnbreakable().addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).addEmptyPDCTag(ClassModule.CLASS_ABILITY_ITEM_KEY).build());
//        getKitItems().put(2, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setColor(Color.RED).setItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS).build()).setDisplayName("<dark_green>Zombie Potion of Health (10 Hearts)").addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).addPDCDoubleTag("heal", 20).build());
//        getKitItems().put(3, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1)).setColor(Color.AQUA).build()).setDisplayName("<dark_green>Zombie Potion of Speed II (0:15s)").setAmount(2).addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).build());
//        getKitItems().put(4, ItemBuilder.getBuilder(Material.DIAMOND_PICKAXE).setDisplayName("<dark_green>Zombie Pickaxe").addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).setUnbreakable().addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).build());
//        getKitItems().put(5, ItemBuilder.getBuilder(getENDER_CHEST()).setDisplayName("<dark_green>Zombie Enderchest").addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).build());
//        getKitItems().put(7, ItemBuilder.getBuilder(Material.COOKED_BEEF).setDisplayName("<dark_green>Zombie Steak").addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).setAmount(3).build());
//        getKitItems().put(8, ItemBuilder.getBuilder(Material.COMPASS).setDisplayName("<dark_green>Zombie Compass").addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).build());
//        getKitArmor().add(new ItemStack(Material.AIR));
//        getKitArmor().add(new ItemStack(Material.AIR));
//        getKitArmor().add(ItemBuilder.getBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("<dark_green>Zombie Chestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 10).addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).setUnbreakable().build());
//        getKitArmor().add(ItemBuilder.getBuilder(Material.IRON_HELMET).setDisplayName("<dark_green>Zombie Helmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 10).addEmptyPDCTag(ClassModule.KIT_ITEM_KEY).setUnbreakable().build());
    }

    @Override
    public void executeAbility(PlayerModel playerModel) {
        final Player player = playerModel.getPlayer();
        if (player == null) return;

        double newExecuterHealth = player.getHealth() + 8;
        final double executorNewHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (newExecuterHealth >= executorNewHealth) {
            newExecuterHealth = executorNewHealth;
        }

        player.setHealth(newExecuterHealth);
        playerModel.getTempPlayerData().resetEnergy();
        for (Player players : playerModel.getPlayer().getLocation().getNearbyPlayers(5)) {
            double teamHealth = players.getHealth() + 5;
            if (players == player) continue;

            double playerNewHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

            if (teamHealth >= playerNewHealth) {
                teamHealth = playerNewHealth;
            }
            players.setHealth(teamHealth);
        }
    }

    @Override
    public void onTakenBowHit(PlayerModel playerModel) {
        handleBerserkAbility(playerModel);
        handleToughnessAbility(playerModel);
    }

    @Override
    public void onTakenHit(PlayerModel playerModel) {
        handleToughnessAbility(playerModel);
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        handleGatheringAbility(event);
    }

    @Override
    public String getActionBar(PlayerModel playerModel) {
        final Player player = playerModel.getPlayer();
        final UUID uuid = playerModel.getUuid();

        if (player == null) return "";

        String toughnessPlaceHolder;
        String berserkPlaceHolder;
        String gatheringPlaceHolder = "§a✓";

        if (toughnessHits.containsKey(uuid)) {
            toughnessPlaceHolder = "§7" + (toughnessHits.get(uuid) == 3 ? "§a✓" : toughnessHits.get(uuid) + "§a/3");
        } else {
            toughnessPlaceHolder = "§70§a/3";
        }

        if (strenghtRunnableMap.containsKey(uuid)) {
            berserkPlaceHolder = "§c§l75% " + strenghtRunnableMap.get(uuid).getCounter() + "s";
        } else {
            if (CooldownUtil.checkCooldown(player, "zombiestrenght") || CooldownUtil.getRemainder(player, "zombiestrenght") == 0) {
                berserkPlaceHolder = "§a✓";
            } else {
                berserkPlaceHolder = "§c§l" + CooldownUtil.getRemainder(player, "zombiestrenght") + "s";
            }
        }

        return String.format("§2§lTOUGHNESS§r %s  §r§2§lBERSERK§r %s  §2§lGATHERING§r %s", toughnessPlaceHolder, berserkPlaceHolder, gatheringPlaceHolder);
    }

    private void handleToughnessAbility(PlayerModel playerModel) {
        playerModel.getTempPlayerData().increaseEnergy(getClassOptions().getClassEnergy().getEnergyPerGettingHit());

        if (toughnessHits.containsKey(playerModel.getUuid())) {
            int hitsTaken = toughnessHits.get(playerModel.getUuid());
            if (hitsTaken + 1 == 4) {
                final Player player = playerModel.getPlayer();
                if (player == null) return;

                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
                toughnessHits.remove(playerModel.getUuid());
                return;
            }
            toughnessHits.replace(playerModel.getUuid(), hitsTaken+1);
            return;
        }
        toughnessHits.put(playerModel.getUuid(), 1);
    }

    private void handleBerserkAbility(PlayerModel playerModel) {
        final Player player = playerModel.getPlayer();
        if (player == null) return;

        if (CooldownUtil.checkCooldown(player, "zombiestrenght")) {
            ZombieStrenghtRunnable zombieStrenghtRunnable = new ZombieStrenghtRunnable(playerModel, this);
            zombieStrenghtRunnable.runTaskTimerAsynchronously(MegaWalls.getInstance(), 0, 20L);
            strenghtRunnableMap.put(playerModel.getUuid(), zombieStrenghtRunnable);
        }
    }

    private void handleGatheringAbility(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (GATHERING_ACTIVE_BLOCKS.contains(event.getBlock().getType())) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
        }
    }
}
