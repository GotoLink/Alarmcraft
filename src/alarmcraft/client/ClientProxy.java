package alarmcraft.client;

import alarmcraft.common.AlarmSettings;
import alarmcraft.common.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;

public final class ClientProxy extends CommonProxy {

    @Override
    public void openAlarmGui(AlarmSettings alarm, EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiAlarmSettings(alarm, player));
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx) {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void registerBlock(final Block block, final String name) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(name, "inventory"));
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().registerBlockWithStateMapper(block, new IStateMapper(){
            private final Map internal = Maps.newLinkedHashMap();
            @Override
            public Map putStateModelLocations(Block block) {
                internal.put(block.getDefaultState(), new ModelResourceLocation(name, "normal"));
                return internal;
            }
        });
    }
}
