package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.classes.framework.Skins;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.classes.framework.AbstractClass;
import dev.tqqn.kireiwalls.modules.classes.framework.ClassDescriptions;
import dev.tqqn.kireiwalls.modules.classes.framework.ClassOptions;
import dev.tqqn.kireiwalls.modules.classes.runnables.ZombieStrenghtRunnable;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import dev.tqqn.kireiwalls.utils.PotionBuilder;
import dev.tqqn.kireiwalls.utils.cooldown.CooldownUtil;
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

    private final Map<UUID, Integer> toughnessHits;
    private final Set<Material> blocksThatCanBeBroken;
    @Getter private final List<UUID> isStrenght;
    @Getter private final Map<UUID, ZombieStrenghtRunnable> strenghtRunnableMap;

    public Zombie() {
        super("Zombie", "ZOM", new ClassOptions(ClassDescriptions.ClassEnergy.ZOMBIE, ClassDescriptions.ClassType.ZOMBIE, ClassDescriptions.ClassDifficulty.ZOMBIE, Arrays.asList(ClassDescriptions.ClassStyle.TANK, ClassDescriptions.ClassStyle.SUPPORT), ClassDescriptions.ClassDiamond.ZOMBIE, ClassDescriptions.ClassSkillDescription.ZOMBIE), 5, Skins.ZOMBIE);
        this.toughnessHits = new HashMap<>();
        this.blocksThatCanBeBroken = new HashSet<>();
        this.isStrenght = new ArrayList<>();
        this.strenghtRunnableMap = new Hashtable<>();

        Collections.addAll(blocksThatCanBeBroken, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.CHERRY_LOG, Material.JUNGLE_LOG, Material.DARK_OAK_LOG, Material.SPRUCE_LOG, Material.OAK_LOG, Material.STONE, Material.COAL_ORE, Material.IRON_ORE, Material.DIAMOND_ORE);
    }

    @Override
    public void initKitItems() {
        getKitItems().put(0, ItemBuilder.getBuilder(Material.IRON_SWORD).setDisplayName("&2Zombie Sword").setLore(getKitAbilityLore()).setGlow().setUnbreakable().setLocalizedName("kit").build());
        getKitItems().put(1, ItemBuilder.getBuilder(Material.BOW).setDisplayName("&2Zombie Bow").setLore(getKitAbilityLore()).setUnbreakable().setLocalizedName("kit").build());
        getKitItems().put(2, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setColor(Color.RED).build()).setDisplayName("&2Zombie Potion of Health (10 Hearts)").setLocalizedName("kit").addPDCTag("heal" + getName(), "20").build());
        getKitItems().put(3, ItemBuilder.getBuilder(PotionBuilder.getBuilder().setPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1)).setColor(Color.AQUA).build()).setDisplayName("&2Zombie Potion of Speed II (0:15s)").setAmount(2).setLocalizedName("kit").build());
        getKitItems().put(4, ItemBuilder.getBuilder(Material.DIAMOND_PICKAXE).setDisplayName("&2Zombie Pickaxe").addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).setLocalizedName("kit").build());
        getKitItems().put(5, ItemBuilder.getBuilder(Material.ENDER_CHEST).setDisplayName("&2Zombie Enderchest").setLocalizedName("kit").build());
        getKitItems().put(7, ItemBuilder.getBuilder(Material.COOKED_BEEF).setDisplayName("&2Zombie Steak").setLocalizedName("kit").setAmount(3).build());
        getKitItems().put(8, ItemBuilder.getBuilder(Material.COMPASS).setDisplayName("&2Zombie Compass").setLocalizedName("kit").build());
        getKitArmor().add(new ItemStack(Material.AIR));
        getKitArmor().add(new ItemStack(Material.AIR));
        getKitArmor().add(ItemBuilder.getBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("&2Zombie Chestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 10).setLocalizedName("kit").setUnbreakable().build());
        getKitArmor().add(ItemBuilder.getBuilder(Material.IRON_HELMET).setDisplayName("&2Zombie Helmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 10).setLocalizedName("kit").setUnbreakable().build());
    }

    @Override
    public void executeAbility(PlayerModel playerModel) {
        double newExecuterHealth = playerModel.getPlayer().getHealth() + 8;
        if (newExecuterHealth >= playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
            newExecuterHealth = playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        }

        playerModel.getPlayer().setHealth(newExecuterHealth);
        playerModel.resetEnergy();
        for (Player player : playerModel.getPlayer().getLocation().getNearbyPlayers(5)) {
            double teamHealth = player.getHealth() + 5;
            if (player == playerModel.getPlayer()) continue;
            if (teamHealth >= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                teamHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            }
            player.setHealth(teamHealth);
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

        if (playerModel.getPlayer() == null) return "";

        String toughnessPlaceHolder;
        String berserkPlaceHolder;
        String gatheringPlaceHolder = "§a✓";

        if (toughnessHits.containsKey(playerModel.getUuid())) {
            toughnessPlaceHolder = "§7" + (toughnessHits.get(playerModel.getUuid()) == 3 ? "§a✓" : toughnessHits.get(playerModel.getUuid()) + "§a/3");
        } else {
            toughnessPlaceHolder = "§70§a/3";
        }

        if (strenghtRunnableMap.containsKey(playerModel.getUuid())) {
            berserkPlaceHolder = "§c§l75% " + strenghtRunnableMap.get(playerModel.getUuid()).getCounter() + "s";
        } else {
            if (CooldownUtil.checkCooldown(playerModel.getPlayer(), "zombiestrenght") || CooldownUtil.getRemainder(playerModel.getPlayer(), "zombiestrenght") == 0) {
                berserkPlaceHolder = "§a✓";
            } else {
                berserkPlaceHolder = "§c§l" + CooldownUtil.getRemainder(playerModel.getPlayer(), "zombiestrenght") + "s";
            }
        }

        return String.format("§2§lTOUGHNESS§r %s  §r§2§lBERSERK§r %s  §2§lGATHERING§r %s", toughnessPlaceHolder, berserkPlaceHolder, gatheringPlaceHolder);
    }

    private void handleToughnessAbility(PlayerModel playerModel) {
        playerModel.increaseEnergy(getClassOptions().getClassEnergy().getEnergyPerGettingHit());

        if (toughnessHits.containsKey(playerModel.getUuid())) {
            int hitsTaken = toughnessHits.get(playerModel.getUuid());
            if (hitsTaken + 1 == 4) {
                playerModel.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
                toughnessHits.remove(playerModel.getUuid());
                return;
            }
            toughnessHits.replace(playerModel.getUuid(), hitsTaken+1);
            return;
        }
        toughnessHits.put(playerModel.getUuid(), 1);
    }

    private void handleBerserkAbility(PlayerModel playerModel) {
        if (CooldownUtil.checkCooldown(playerModel.getPlayer(), "zombiestrenght")) {
            ZombieStrenghtRunnable zombieStrenghtRunnable = new ZombieStrenghtRunnable(playerModel, this);
            zombieStrenghtRunnable.runTaskTimerAsynchronously(KireiWalls.getInstance(), 0, 20L);
            strenghtRunnableMap.put(playerModel.getUuid(), zombieStrenghtRunnable);
        }
    }

    private void handleGatheringAbility(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (blocksThatCanBeBroken.contains(event.getBlock().getType())) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
        }
    }
}
