package alarmcraft.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public final class BlockAlarm extends BlockContainer {
    public BlockAlarm() {
        super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setBlockBounds(0.1875f, 0.0f, 0.1875f, 0.8125f, 0.25f, 0.8125f);
        this.setLightLevel(0.2f);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.1875f, 0.0f, 0.1875f, 0.8125f, 0.25f, 0.8125f);
    }

    @Override
    public int getRenderType(){
        return 3;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World theWorld, BlockPos pos) {
        Block id = theWorld.getBlockState(pos.down()).getBlock();
        return id != Blocks.air && id != ModAlarmcraft.alarmBlock;
    }

    @Override
    public void onNeighborBlockChange(World theWorld, BlockPos pos, IBlockState state, Block neighbourBlockId) {
        if (!theWorld.isRemote) {
            AlarmSettings alarm = (AlarmSettings) theWorld.getTileEntity(pos);
            if (alarm == null)
                return;
            if (alarm.isPowered && theWorld.isBlockIndirectlyGettingPowered(pos) == 0) {
                alarm.isPowered = false;
            } else if (!alarm.isPowered && theWorld.isBlockIndirectlyGettingPowered(pos) > 0) {
                alarm.isPowered = true;

                if (alarm.theType == 26) {  //   it's a b...    *airplane reference :-)
                    double x = pos.getX();
                    double y = pos.getY();
                    double z = pos.getZ();
                    EntityPlayer nplayer = theWorld.getClosestPlayer(x, y, z, -1);
                    theWorld.createExplosion(nplayer, x, y, z, 10, true); //server side explosion
                }
                //send packet to client so it can make the sound effects
                PacketHandler.sendPacketToAllAround(alarm, "poweredon", 50);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer thePlayer, EnumFacing par6, float par7, float par8, float par9) {

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AlarmSettings) {
            ((AlarmSettings) tile).playersUsername = thePlayer.getCommandSenderName();
            ModAlarmcraft.proxy.openAlarmGui((AlarmSettings) tile, thePlayer);
        }
        world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "satscapealarmcraft:carlock", 1f, 1f);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new AlarmSettings();
    }
}
