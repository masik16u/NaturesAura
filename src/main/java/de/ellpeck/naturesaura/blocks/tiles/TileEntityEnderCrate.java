package de.ellpeck.naturesaura.blocks.tiles;

import de.ellpeck.naturesaura.NaturesAura;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.misc.ILevelData;
import de.ellpeck.naturesaura.blocks.BlockEnderCrate;
import de.ellpeck.naturesaura.gui.ContainerEnderCrate;
import de.ellpeck.naturesaura.gui.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityEnderCrate extends BlockEntityImpl implements MenuProvider {

    public String name;
    private final IItemHandlerModifiable wrappedEnderStorage = new IItemHandlerModifiable() {
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            this.getStorage().setStackInSlot(slot, stack);
        }

        @Override
        public int getSlots() {
            return this.getStorage().getSlots();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.getStorage().getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            ItemStack remain = this.getStorage().insertItem(slot, stack, simulate);
            if (!simulate)
                BlockEntityEnderCrate.this.drainAura((stack.getCount() - remain.getCount()) * 20);
            return remain;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack extracted = this.getStorage().extractItem(slot, amount, simulate);
            if (!simulate)
                BlockEntityEnderCrate.this.drainAura(extracted.getCount() * 20);
            return extracted;
        }

        @Override
        public int getSlotLimit(int slot) {
            return this.getStorage().getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return this.getStorage().isItemValid(slot, stack);
        }

        private IItemHandlerModifiable getStorage() {
            return ILevelData.getOverworldData(BlockEntityEnderCrate.this.level).getEnderStorage(BlockEntityEnderCrate.this.name);
        }
    };

    public BlockEntityEnderCrate(BlockPos pos, BlockState state) {
        super(ModTileEntities.ENDER_CRATE, pos, state);
    }

    @Override
    public IItemHandlerModifiable getItemHandler() {
        if (this.canOpen())
            return this.wrappedEnderStorage;
        return null;
    }

    public boolean canOpen() {
        return this.name != null;
    }

    @Override
    public void dropInventory() {
    }

    @Override
    public void modifyDrop(ItemStack regularItem) {
        if (this.name != null) {
            if (!regularItem.hasTag())
                regularItem.setTag(new CompoundTag());
            regularItem.getTag().putString(NaturesAura.MOD_ID + ":ender_name", this.name);
        }
    }

    @Override
    public void loadDataOnPlace(ItemStack stack) {
        super.loadDataOnPlace(stack);
        if (!this.level.isClientSide) {
            String name = BlockEnderCrate.getEnderName(stack);
            if (name != null && !name.isEmpty())
                this.name = name;
        }
    }

    @Override
    public void writeNBT(CompoundTag compound, SaveType type) {
        super.writeNBT(compound, type);
        if (type != SaveType.BLOCK) {
            if (this.name != null)
                compound.putString("name", this.name);
        }
    }

    @Override
    public void readNBT(CompoundTag compound, SaveType type) {
        super.readNBT(compound, type);
        if (type != SaveType.BLOCK) {
            if (compound.contains("name"))
                this.name = compound.getString("name");
        }
    }

    public void drainAura(int amount) {
        if (amount > 0) {
            BlockPos spot = IAuraChunk.getHighestSpot(this.level, this.worldPosition, 35, this.worldPosition);
            IAuraChunk.getAuraChunk(this.level, spot).drainAura(spot, amount);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("info." + NaturesAura.MOD_ID + ".ender_crate", TextFormatting.ITALIC + this.name + TextFormatting.RESET);
    }

    @Nullable
    @Override
    public Container createMenu(int window, PlayerInventory inv, Player player) {
        return new ContainerEnderCrate(ModContainers.ENDER_CRATE, window, player, this.getItemHandler());
    }
}
