package alarmcraft.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public final class BlockAlarm extends BlockContainer {
	private IIcon icons[];
	
	public BlockAlarm() {
		super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setBlockBounds(0.1875f,0.0f,0.1875f, 0.8125f,0.25f,0.8125f);
        this.setLightLevel(0.2f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons=new IIcon[2];
		icons[0] = iconRegister.registerIcon(this.getTextureName()+"side");
		icons[1] = iconRegister.registerIcon(this.getTextureName()+"top");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side < 2) {
			return icons[1];
		} else {
			return icons[0];
		}
	}

	@Override
	public void setBlockBoundsForItemRender()
    {
		setBlockBounds(0.1875f,0.0f,0.1875f,  0.8125f,0.25f,0.8125f);
    }

    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public boolean canPlaceBlockAt(World theWorld, int x, int y, int z) {
		Block id=theWorld.getBlock(x, y - 1, z);
        return id != Blocks.air && id != ModAlarmcraft.alarmBlock;
	}

    @Override
	public void onNeighborBlockChange(World theWorld, int x, int y, int z, Block neighbourBlockId)
    {
        if (!theWorld.isRemote) {
            AlarmSettings alarm = (AlarmSettings) theWorld.getTileEntity(x,y,z);
            if(alarm == null)
                return;
            if (alarm.isPowered && !theWorld.isBlockIndirectlyGettingPowered(x,y,z))
            {
                alarm.isPowered=false;
            }
            else if (!alarm.isPowered && theWorld.isBlockIndirectlyGettingPowered(x,y,z))
            {
                alarm.isPowered=true;

                if (alarm.theType==26) {  //   it's a b...    *airplane reference :-)
                    EntityPlayer nplayer=theWorld.getClosestPlayer(x,y,z,-1);
                    theWorld.createExplosion(nplayer, x, y, z, 10, true); //server side explosion
                }
                //send packet to client so it can make the sound effects
                PacketHandler.sendPacketToAllAround(alarm, "poweredon", 50);
            }
        }
    }

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer thePlayer, int par6, float par7,float par8, float par9) {

        TileEntity tile = world.getTileEntity(i, j, k);
        if(tile instanceof AlarmSettings) {
            ((AlarmSettings) tile).playersUsername = thePlayer.getCommandSenderName();
            ModAlarmcraft.proxy.openAlarmGui((AlarmSettings) tile, thePlayer);
        }
        world.playSoundEffect(i,j,k,"satscapealarmcraft:carlock",1f,1f);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new AlarmSettings();
    }
}
