package net.dohaw.potionmarker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

        Player pSender = (Player) sender;
        ItemStack itemInHand = pSender.getEquipment().getItemInMainHand();
        if(itemInHand == null){
            sender.sendMessage(ChatColor.RED + "You have no item in your main hand!");
            return false;
        }

        if(){

        }

        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "No potion type was given. Try using the command " + ChatColor.GOLD + "/pmark <potion type>");
            return false;
        }

        String potionTypeArg = args[0];
        PotionType potionType;
        try{
            potionType = PotionType.valueOf(potionTypeArg.toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage(ChatColor.RED + "This is not a valid potion type!");
            return false;
        }




        return false;
    }

}
