package net.dohaw.nvapplier;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class NVApplierPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        JPUtils.registerCommand("nightvision", new NVApplierCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
