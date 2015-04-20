package alarmcraft.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

    public void openAlarmGui(AlarmSettings alarm, EntityPlayer player) {
        //nothing on server
    }

    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public void registerBlock(Block block, String name) {

    }
}
