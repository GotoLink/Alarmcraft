package alarmcraft.common;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;

/// Alarmcraft Mod by Scott Hather AKA "Satscape"
/// v1.0.0 - 20th June 2013

@Mod(modid = "satscapealarmcraft", name = "Alarmcraft")
public final class ModAlarmcraft {

    @SidedProxy(clientSide = "alarmcraft.client.ClientProxy", serverSide = "alarmcraft.common.CommonProxy")
    public static CommonProxy proxy;
    /**
     * Block instances and IDs
     */
    public static Block alarmBlock;

    public static Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        alarmBlock = new BlockAlarm().setStepSound(Block.soundTypeMetal).setHardness(2F).setResistance(1.0F).setUnlocalizedName("alarm");
        log = evt.getModLog();
        GameRegistry.registerBlock(alarmBlock, "SatscapeAlarm");
        GameRegistry.registerTileEntity(AlarmSettings.class, "SatscapeAlarm");
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        proxy.registerBlock(alarmBlock, "satscapealarmcraft:SatscapeAlarm");
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(alarmBlock), "III", "IRI", "ILI",
                'I', "ingotIron",
                'R', "dustRedstone",
                'L', Blocks.lever));
        PacketHandler.INSTANCE.register(SettingPacket.Update.class, Side.SERVER);
        PacketHandler.INSTANCE.register(SettingPacket.class, Side.CLIENT);
    }

    public static void log(String text) {
        log.debug(text);
    }
}




