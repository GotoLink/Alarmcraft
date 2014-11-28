package alarmcraft.common;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by Olivier on 28/11/2014.
 */
public class SettingPacket implements IMessage{
    public AlarmSettings settings;
    public String messageType;
    public SettingPacket(){}
    public SettingPacket(AlarmSettings alarmSettings, String type) {
        this.settings = alarmSettings;
        this.messageType = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        settings=new AlarmSettings(buf.readInt(),buf.readBoolean(),
                buf.readInt(), buf.readInt(), buf.readInt(), ByteBufUtils.readUTF8String(buf));
        messageType=ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(settings.theType);
        buf.writeBoolean(settings.isPowered);
        buf.writeInt(settings.xCoord);
        buf.writeInt(settings.yCoord);
        buf.writeInt(settings.zCoord);
        ByteBufUtils.writeUTF8String(buf, settings.playersUsername);
        ByteBufUtils.writeUTF8String(buf, messageType);
    }

    public static class Update extends SettingPacket{
        public Update(){}
        public Update(AlarmSettings alarmSettings){
            super(alarmSettings, "update");
        }
    }
}
