package alarmcraft.client;

import alarmcraft.common.AlarmSettings;
import alarmcraft.common.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAlarmSettings extends GuiScreen {
	private static final ResourceLocation myBackgroundTexture = new ResourceLocation("satscapealarmcraft","textures/gui/guiBack.png");
	private EntityPlayer thePlayer;
	private AlarmSettings alarm;
	private int mouseCount=0;
	private int tempMeta=0;
	private String alertText="";
	
	public GuiAlarmSettings(AlarmSettings alarm,EntityPlayer player) {
		this.alarm=alarm;
		this.thePlayer=player;
	}

    @Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		showPage();
		Mouse.setGrabbed(false);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		
		if (mouseCount<10) {
			mouseCount++;
			Mouse.setGrabbed(false);
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(myBackgroundTexture);
		
		int posX = (this.width - 256) / 2;
		drawTexturedModalRect(posX, 5, 0, 0, 256, 230);

		drawCenteredString(fontRendererObj, "Alarmcraft", width / 2, 10, 0xFF1010);
        int textBorder = 5;
        fontRendererObj.drawSplitString(I18n.format("gui.alarm.fulltext"), posX+textBorder, 30, 256-textBorder*2, 0xFFFFFF);
		drawCenteredString(fontRendererObj, I18n.format(alertText), width/2, 150, 0x00FF00);
		
		super.drawScreen(i, j, f);
	}

	private void showPage() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, this.width/2-25, 210, 50, 20, I18n.format("gui.done")));
		
		tempMeta=alarm.theType;
		if (tempMeta<26) {
            char ch=(char) (65+tempMeta);
            buttonList.add(new GuiButton(1, this.width/2-50,100,100,20,I18n.format("gui.alarm.name", ch)));
		}else{
            buttonList.add(new GuiButton(1, this.width/2-50,100,100,20,I18n.format("gui.alert.name")));
        }
		buttonList.add(new GuiButton(2, this.width/2-25,120,50,20,I18n.format("gui.alarm.test")));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 0) // SET alarm button
		{
            this.alarm.theType = tempMeta;
            this.alarm.isPowered = false;
            this.alarm.playersUsername = "";
			PacketHandler.sendPacketToServer(this.alarm, "update");

            thePlayer.worldObj.playSoundAtEntity(thePlayer, "satscapealarmcraft:carlock", 1f, 1f);
			
			mc.currentScreen = null;
			mc.setIngameFocus();
		
		} else if (guibutton.id == 1) { //change alarm type button
			tempMeta++;
			if (tempMeta>12 && tempMeta<26) {
				tempMeta=26;
			} else if (tempMeta>26) {
				tempMeta=0;
			}
			if (tempMeta<26) {
                char ch=(char) (65+tempMeta);
				guibutton.displayString=I18n.format("gui.alarm.name",ch);
				alertText="";
			} else {
				guibutton.displayString=I18n.format("gui.alert.name");
				alertText="gui.alert.desc";
			}
		
		} else if (guibutton.id == 2) { //test the alarm button
			if (tempMeta <26) {
                char ch=(char) (65+tempMeta);
				thePlayer.worldObj.playSoundAtEntity(thePlayer, "satscapealarmcraft:alarm" + ch, 1f, 1f);
				alertText="";
			} else {
				alertText="gui.alert.test";
			}
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (i == 1) { // escape and dont save
			mc.currentScreen = null;
			mc.setIngameFocus();
		}
	}
}
