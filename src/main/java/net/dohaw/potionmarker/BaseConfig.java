package net.dohaw.potionmarker;

import net.dohaw.corelib.Config;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseConfig extends Config {

    public BaseConfig() {
        super("config.yml");
    }

    public Map<String, List<String>> getRestrictedItemKeywords(){
        Map<String, List<String>> restrictedItemKeywords = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("Restricted Item Keywords");
        for(String potionName : section.getKeys(false)){
            restrictedItemKeywords.put(potionName, section.getStringList(potionName));
        }
        return restrictedItemKeywords;
    }

    public Map<String, List<String>> getApplicableItemKeywords(){
        Map<String, List<String>> applicableItemKeywords = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("Applicable Item Keywords");
        for(String potionName : section.getKeys(false)){
            applicableItemKeywords.put(potionName, section.getStringList(potionName));
        }
        return applicableItemKeywords;
    }

}
