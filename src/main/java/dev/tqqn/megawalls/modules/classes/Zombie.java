package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.classes.framework.Skins;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.classes.framework.AbstractClass;
import dev.tqqn.megawalls.modules.classes.framework.ClassDescriptions;
import dev.tqqn.megawalls.modules.classes.framework.ClassOptions;
import dev.tqqn.megawalls.modules.classes.runnables.ZombieStrenghtRunnable;
import dev.tqqn.megawalls.utils.ItemBuilder;
import dev.tqqn.megawalls.utils.PotionBuilder;
import dev.tqqn.megawalls.utils.cooldown.CooldownUtil;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

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
        super("Zombie", "ZOM", new ClassOptions(ClassDescriptions.ClassEnergy.ZOMBIE, ClassDescriptions.ClassType.ZOMBIE, ClassDescriptions.ClassDifficulty.ZOMBIE, Arrays.asList(ClassDescriptions.ClassStyle.TANK, ClassDescriptions.ClassStyle.SUPPORT), ClassDescriptions.ClassDiamond.ZOMBIE, ClassDescriptions.ClassSkillDescription.ZOMBIE), 5, Skins.ZOMBIE);
        this.toughnessHits = new HashMap<>();
        this.isStrenght = new ArrayList<>();
        this.strenghtRunnableMap = new Hashtable<>();
    }

    @Override
    public void initKitItems() {
        getKitItems().put(0, ItemBuilder.getBuilder(Material.IRON_SWORD).setDisplayName("&2Zombie Sword").setLore(getKitAbilityLore()).setGlow().setUnbreakable().setLocalizedName("kit").build());
        getKitItems().put(1, ItemBuilder.getBuilder(Material.BOW).setDisplayName("&2Zombie Bow").setLore(getKitAbilityLore()).setUnbreakable().setLocalizedName("kit").build());
        getKitItems().put(2, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setColor(Color.RED).build()).setDisplayName("&2Zombie Potion of Health (10 Hearts)").setLocalizedName("kit").addPDCDoubleTag("heal", 20).build());
        getKitItems().put(3, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1)).setColor(Color.AQUA).build()).setDisplayName("&2Zombie Potion of Speed II (0:15s)").setAmount(2).setLocalizedName("kit").build());
        getKitItems().put(4, ItemBuilder.getBuilder(Material.DIAMOND_PICKAXE).setDisplayName("&2Zombie Pickaxe").addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).setUnbreakable().setLocalizedName("kit").build());
        getKitItems().put(5, ItemBuilder.getBuilder(getENDER_CHEST()).setDisplayName("&2Zombie Enderchest").build());
        getKitItems().put(7, ItemBuilder.getBuilder(Material.COOKED_BEEF).setDisplayName("&2Zombie Steak").setLocalizedName("kit").setAmount(3).build());
        getKitItems().put(8, ItemBuilder.getBuilder(Material.COMPASS).setDisplayName("&2Zombie Compass").setLocalizedName("kit").build());
        getKitArmor().add(new ItemStack(Material.AIR));
        getKitArmor().add(new ItemStack(Material.AIR));
        getKitArmor().add(ItemBuilder.getBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("&2Zombie Chestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 10).setLocalizedName("kit").setUnbreakable().build());
        getKitArmor().add(ItemBuilder.getBuilder(Material.IRON_HELMET).setDisplayName("&2Zombie Helmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 10).setLocalizedName("kit").setUnbreakable().build());
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
