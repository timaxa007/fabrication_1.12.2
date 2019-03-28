package timaxa007.fabrication;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFabrication extends TileEntity implements ITickable, ISidedInventory {

	private static final int[]
			SLOTS_TOP = new int[] {0},
			SLOTS_BOTTOM = new int[] {2, 1},
			SLOTS_SIDES = new int[] {1};

	private NonNullList<ItemStack> tileItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);

	private String customName;

	public TileEntityFabrication() {

	}

	@Override
	public void update() {
		ItemStack slot1 = getStackInSlot(0);
		ItemStack slot2 = getStackInSlot(1);
		if ((!slot1.isEmpty() && slot1.getItem() == Item.getItemFromBlock(Blocks.PLANKS)) &&
				(slot2.isEmpty() || (slot2.getItem() == Items.STICK && slot2.getCount() + 4 <= slot2.getMaxStackSize()))) {

			slot1.shrink(1);
			if (slot1.getCount() <= 0)
				setInventorySlotContents(0, ItemStack.EMPTY);
			else
				setInventorySlotContents(0, slot1);

			if (slot2.isEmpty())
				setInventorySlotContents(1, new ItemStack(Items.STICK, 4));
			else {
				slot2.grow(4);
				setInventorySlotContents(1, slot2);
			}

		}
	}

	@Override
	public int getSizeInventory() {
		return tileItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : tileItemStacks) {
			if (!itemstack.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return tileItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(tileItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(tileItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		tileItemStacks.set(index, stack);

		if (stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());

	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (world.getTileEntity(pos) != this)
			return false;
		else
			return player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		switch(index) {
		case 1:
			return false;
		default:
			return true;
		}
	}

	@Override
	public int getField(int id) {
		/*
		switch (id) {
		case 0:
			return tileBurnTime;
		case 1:
			return currentItemBurnTime;
		case 2:
			return cookTime;
		case 3:
			return totalCookTime;
		default:
			return 0;
		}*/
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		/*
		switch (id) {
		case 0:
			tileBurnTime = value;
			break;
		case 1:
			currentItemBurnTime = value;
			break;
		case 2:
			cookTime = value;
			break;
		case 3:
			totalCookTime = value;
		}*/
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		tileItemStacks.clear();
	}

	@Override
	public String getName() {
		return hasCustomName() ? customName : "container.tile";
	}

	@Override
	public boolean hasCustomName() {
		return customName != null && !customName.isEmpty();
	}

	public void setCustomInventoryName(String customName) {
		this.customName = customName;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		switch (side) {
		case DOWN:
			return SLOTS_BOTTOM;
		case UP:
			return SLOTS_TOP;
		default:
			return SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET)
				return false;
		}

		return true;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		tileItemStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, tileItemStacks);

		if (compound.hasKey("CustomName", NBT.TAG_STRING))
			customName = compound.getString("CustomName");

	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, tileItemStacks);

		if (hasCustomName())
			compound.setString("CustomName", customName);

		return compound;
	}

}
