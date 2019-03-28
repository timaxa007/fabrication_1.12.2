package timaxa007.fabrication.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import timaxa007.fabrication.FabricationContiner;
import timaxa007.fabrication.TileEntityFabrication;

@SideOnly(Side.CLIENT)
public class FabricationGui extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
	private final IInventory upperChestInventory;
	private final IInventory lowerChestInventory;

	public FabricationGui(EntityPlayer player, TileEntityFabrication tile) {
		super(new FabricationContiner(player, tile));
		upperChestInventory = player.inventory;
		lowerChestInventory = tile;
		//ySize = 114 + 1 * 18;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//fontRenderer.drawString(lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		//fontRenderer.drawString(upperChestInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
