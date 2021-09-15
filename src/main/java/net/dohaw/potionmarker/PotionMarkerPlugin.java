package net.dohaw.potionmarker;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

public final class PotionMarkerPlugin extends JavaPlugin {

    private static NamespacedKey potionMarkerKey;

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        potionMarkerKey = new NamespacedKey(this, "potion-effects-on-equip");
        JPUtils.registerCommand("nightvision", new PotionMarkerCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void markItem(ItemStack stack, PotionType potionType){

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        String potionEffectsOnEquip = "";
        if(pdc.has(potionMarkerKey, PersistentDataType.STRING)){
            potionEffectsOnEquip = pdc.get(potionMarkerKey, PersistentDataType.STRING);
        }

        potionEffectsOnEquip += potionType + ";";

        pdc.set(potionMarkerKey, PersistentDataType.STRING, potionEffectsOnEquip);
        stack.setItemMeta(meta);

    }

    public boolean isMarkedItem(ItemStack stack){
        if(stack == null){
            return false;
        }
        return stack.getItemMeta().getPersistentDataContainer().has(potionMarkerKey, PersistentDataType.STRING);
    }

    public PotionType[] getPotionsAppliedOnEquip(ItemStack stack){

        PersistentDataContainer pdc = stack.getItemMeta().getPersistentDataContainer();
        String rawPotionEffectsOnEquip = pdc.get(potionMarkerKey, PersistentDataType.STRING);

        return rawPotionEffectsOnEquip.split(";");
    }

    public

}
