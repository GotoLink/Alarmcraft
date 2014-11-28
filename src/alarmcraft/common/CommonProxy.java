package alarmcraft.common;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {

	public void openAlarmGui(AlarmSettings alarm, EntityPlayer player) {
		//nothing on server
	}

    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}
