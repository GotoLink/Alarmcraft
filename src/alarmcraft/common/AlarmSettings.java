package alarmcraft.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public final class AlarmSettings extends TileEntity {

    public int theType = 0;
    public boolean isPowered = false;
    public String playersUsername = "";  //this is used to remember who right clicked the box so when the packet arrives it opens the UI on the correct users screen

    public AlarmSettings() {
    }

    public AlarmSettings(int type, boolean power, int x, int y, int z, String user) {
        this.theType = type;
        this.isPowered = power;
        this.pos = new BlockPos(x, y, z);
        this.playersUsername = user;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        theType = tagCompound.getInteger("Sound");
        isPowered = tagCompound.getBoolean("Power");
        playersUsername = tagCompound.getString("Owner");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Sound", theType);
        tagCompound.setBoolean("Power", isPowered);
        tagCompound.setString("Owner", playersUsername);
    }

    public SettingPacket toPacket(String type) {
        if (type.equals("update")) {
            return new SettingPacket.Update(this);
        }
        return new SettingPacket(this, type);
    }

    public int xCoord() {
        return getPos().getX();
    }

    public int yCoord() {
        return getPos().getY();
    }

    public int zCoord() {
        return getPos().getZ();
    }
}
