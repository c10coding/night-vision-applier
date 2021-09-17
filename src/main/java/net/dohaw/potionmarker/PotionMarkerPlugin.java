package net.dohaw.potionmarker;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class PotionMarkerPlugin extends JavaPlugin {

    private final String POTION_ON_EQUIP_LEVEL_KEY_NAME = "potion-on-equip-level";
    private final String POTION_ON_EQUIP_DURATION_KEY_NAME = "potion-on-equip-duration";

    private NamespacedKey potionMarkerKey;

    private Map<String, List<String>> allRestrictedItemKeywords, allApplicableItemKeywords;

    private BaseConfig baseConfig;

    @Override
    public void onEnable() {

        CoreLib.setInstance(this);

        JPUtils.validateFiles("config.yml");
        this.baseConfig = new BaseConfig();
        this.allApplicableItemKeywords = baseConfig.getApplicableItemKeywords();
        this.allRestrictedItemKeywords = baseConfig.getRestrictedItemKeywords();

        this.potionMarkerKey = new NamespacedKey(this, "potion-marker");

        JPUtils.registerCommand("potionmarker", new PotionMarkerCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void markItem(ItemStack stack, PotionEffectType potionType, int level, int duration){

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String keyAddition = potionType.getName().toLowerCase().replace("_", "-");

        NamespacedKey potionLevelKey = new NamespacedKey(this, POTION_ON_EQUIP_LEVEL_KEY_NAME + keyAddition);
        NamespacedKey potionDurationKey = new NamespacedKey(this, POTION_ON_EQUIP_DURATION_KEY_NAME + keyAddition);

        pdc.set(potionLevelKey, PersistentDataType.INTEGER, level);
        pdc.set(potionDurationKey, PersistentDataType.INTEGER, duration);
        pdc.set(potionMarkerKey, PersistentDataType.STRING, "marker");

        stack.setItemMeta(meta);

    }

    public boolean isMarkedItem(ItemStack stack){
        if(stack == null){
            return false;
        }
        return stack.getItemMeta().getPersistentDataContainer().has(potionMarkerKey, PersistentDataType.STRING);
    }

    private List<PotionEffect> getPotionsAppliedOnEquip(ItemStack stack){

        List<PotionEffect> potions = new ArrayList<>();
        PersistentDataContainer pdc = stack.getItemMeta().getPersistentDataContainer();
        for(PotionEffectType potionType : PotionEffectType.values()){

            String keyAddition = potionType.getName().toLowerCase().replace("_", "-");
            NamespacedKey potionLevelKey = new NamespacedKey(this, POTION_ON_EQUIP_LEVEL_KEY_NAME + keyAddition);
            NamespacedKey potionDurationKey = new NamespacedKey(this, POTION_ON_EQUIP_DURATION_KEY_NAME + keyAddition);

            if(pdc.has(potionLevelKey, PersistentDataType.INTEGER)){
                int potionLevel = pdc.get(potionLevelKey, PersistentDataType.INTEGER);
                int potionDuration = pdc.get(potionDurationKey, PersistentDataType.INTEGER);
                potions.add(new PotionEffect(potionType, potionDuration, potionLevel));
            }

        }

        return potions;

    }

    public void applyEffects(Player player, ItemStack stack){
        List<PotionEffect> potionsApplied = getPotionsAppliedOnEquip(stack);
        for(PotionEffect potion : potionsApplied){
            player.addPotionEffect(potion);
        }
    }

    public boolean canBeAppliedToItem(ItemStack stack, PotionEffectType potionType){

        ItemMeta meta = stack.getItemMeta();
        if(meta == null){
            return false;
        }

        String comparedName = meta.hasDisplayName() ? meta.getDisplayName().toLowerCase() : stack.getType().toString().toLowerCase();

        System.out.println("COMPARED NAME: " + comparedName);
        if(!isApplicableItem(comparedName, potionType)){
            return false;
        }

        return !isRestrictedItem(comparedName, potionType);

    }

    private boolean isApplicableItem(String itemName, PotionEffectType potionType){

        List<String> applicableItemKeywords = allApplicableItemKeywords.get(potionType.getName().toLowerCase());
        if(applicableItemKeywords == null){
            getLogger().severe("The potion type " + potionType.getName() + " doesn't have a valid key within the \"Applicable Potions Section\" of your config");
            return false;
        }

        for(String keyword : applicableItemKeywords){
            if(itemName.contains(keyword)){
                return true;
            }
        }

        return false;

    }

    private boolean isRestrictedItem(String itemName, PotionEffectType potionType){

        List<String> restrictedItemKeywords = allRestrictedItemKeywords.get(potionType.getName().toLowerCase());
        if(restrictedItemKeywords == null){
            getLogger().severe("The potion type " + potionType.getName() + " doesn't have a valid key within the \"Restricted Potions Section\" of your config");
            return false;
        }

        for(String keyword : restrictedItemKeywords){
            if(itemName.contains(keyword)){
                return true;
            }
        }

        return false;

    }

}
