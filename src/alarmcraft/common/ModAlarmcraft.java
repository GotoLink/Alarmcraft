package alarmcraft.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;

/// Alarmcraft Mod by Scott Hather AKA "Satscape"
/// v1.0.0 - 20th June 2013

@Mod(modid="satscapealarmcraft", name="Alarmcraft")
public class ModAlarmcraft {
	
	@SidedProxy(clientSide = "alarmcraft.client.ClientProxy", serverSide = "alarmcraft.common.CommonProxy")
	public static CommonProxy proxy;
	/** Block instances and IDs */
    public static Block alarmBlock;

    public static Logger log;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
        alarmBlock = new BlockAlarm().setStepSound(Block.soundTypeMetal).setHardness(2F).setResistance(1.0F).setBlockName("SatscapeAlarm").setBlockTextureName("satscapealarmcraft:alarm");
        log = evt.getModLog();
        GameRegistry.registerBlock(alarmBlock, "SatscapeAlarm");
        GameRegistry.registerTileEntity(AlarmSettings.class, "SatscapeAlarm");
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(alarmBlock), "III", "IRI", "ILI",
                'I', "ingotIron",
                'R', "dustRedstone",
                'L', Blocks.lever));
        PacketHandler.INSTANCE.register(SettingPacket.Update.class, Side.SERVER);
        PacketHandler.INSTANCE.register(SettingPacket.class, Side.CLIENT);
    }

    public static void log(String text){
        log.debug(text);
    }
}




