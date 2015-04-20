package alarmcraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketHandler implements IMessageHandler<SettingPacket, IMessage> {

    private int discr;
    private SimpleNetworkWrapper mainChannel;
    public static PacketHandler INSTANCE = new PacketHandler();

    private PacketHandler() {
        mainChannel = NetworkRegistry.INSTANCE.newSimpleChannel("AlarmcraftMain");
    }

    public void register(Class<? extends SettingPacket> clazz, Side side) {
        mainChannel.registerMessage(this, clazz, ++discr, side);
    }

    public void onPacketData(SettingPacket packet, EntityPlayer receiver) {
        if (packet.messageType.equals("update")) {   // client > server
            if (!receiver.worldObj.isRemote) {
                TileEntity tile = receiver.worldObj.getTileEntity(packet.settings.getPos());
                if (tile instanceof AlarmSettings) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    packet.settings.writeToNBT(tagCompound);
                    tile.readFromNBT(tagCompound);
                }
            }

        } else if (packet.messageType.equals("poweredon")) {// server > client
            if (receiver == null || !receiver.worldObj.isRemote)
                return;
            if (packet.settings.theType < 26) {
                char ch = (char) (65 + packet.settings.theType);
                receiver.playSound("satscapealarmcraft:alarm" + ch, 1f, 1f);
            } else {
                receiver.playSound("random.explode", 4f, 1f);
            }
        }
    }

    public static void sendPacketToServer(AlarmSettings alarm, String msgType) {
        INSTANCE.mainChannel.sendToServer(alarm.toPacket(msgType));
    }

    public static void sendPacketToAllAround(AlarmSettings alarm, String msgType, int range) {
        INSTANCE.mainChannel.sendToAllAround(alarm.toPacket(msgType), new NetworkRegistry.TargetPoint(alarm.getWorld().provider.getDimensionId(), alarm.xCoord(), alarm.yCoord(), alarm.zCoord(), range));
    }

    @Override
    public IMessage onMessage(SettingPacket message, MessageContext ctx) {
        onPacketData(message, ModAlarmcraft.proxy.getPlayer(ctx));
        return null;
    }
}
