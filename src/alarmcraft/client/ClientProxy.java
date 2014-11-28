package alarmcraft.client;

import alarmcraft.common.AlarmSettings;
import alarmcraft.common.CommonProxy;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {

    @Override
    public void openAlarmGui(AlarmSettings alarm, EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiAlarmSettings(alarm, player));
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx){
        return Minecraft.getMinecraft().thePlayer;
    }
}
