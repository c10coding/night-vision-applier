package net.dohaw.potionmarker;

import net.dohaw.corelib.helpers.MathHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionMarkerCommand implements CommandExecutor {

    private PotionMarkerPlugin plugin;

    public PotionMarkerCommand(PotionMarkerPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        //pmark <potion type> <level> <duration>

        Player pSender = (Player) sender;
        ItemStack itemInHand = pSender.getEquipment().getItemInMainHand();
        if(itemInHand.getType() == Material.AIR){
            sender.sendMessage(ChatColor.RED + "You have no item in your main hand!");
            return false;
        }

        if(args.length < 3){
            sender.sendMessage(ChatColor.RED + "Incomplete command! Try using the command " + ChatColor.GOLD + "/pmark <potion type> <level> <duration>");
            return false;
        }

        String potionTypeArg = args[0];
        PotionEffectType potionType = PotionEffectType.getByName(potionTypeArg);;
        if(potionType == null){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion type!");
            return false;
        }

        String potionLevelArg = args[1];
        if(!MathHelper.isInt(potionLevelArg)){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion level!");
            return false;
        }else if(Integer.parseInt(potionLevelArg) < 0){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion level!");
            return false;
        }

        String potionDurationArg = args[2];
        if(!MathHelper.isInt(potionDurationArg)){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion duration!");
            return false;
        }else if(Integer.parseInt(potionDurationArg) < 0){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion duration!");
            return false;
        }

        if(!plugin.canBeAppliedToItem(itemInHand, potionType)){
            sender.sendMessage(ChatColor.RED + "This potion can't be applied to this item!");
            return false;
        }

        plugin.markItem(itemInHand, potionType, Integer.parseInt(potionLevelArg), Integer.parseInt(potionDurationArg));
        sender.sendMessage(ChatColor.BLUE + "This item has been marked!");

        return true;

    }

}
