package de.ellpeck.naturesaura.items;

import de.ellpeck.naturesaura.entities.EntityStructureFinder;
import de.ellpeck.naturesaura.entities.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public class ItemStructureFinder extends ItemImpl {

    private final StructureFeature<?> structureName;
    private final int color;
    private final int radius;

    public ItemStructureFinder(String baseName, StructureFeature<?> structureName, int color, int radius) {
        super(baseName);
        this.structureName = structureName;
        this.color = color;
        this.radius = radius;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
        var stack = playerIn.getItemInHand(handIn);
        if (!levelIn.isClientSide && ((ServerLevel) levelIn).structureFeatureManager().shouldGenerateFeatures()) {
            var pos = ((ServerLevel) levelIn).getChunkSource().getGenerator().findNearestMapFeature((ServerLevel) levelIn, this.structureName, playerIn.blockPosition(), this.radius, false);
            if (pos != null) {
                var entity = new EntityStructureFinder(ModEntities.STRUCTURE_FINDER, levelIn);
                entity.setPos(playerIn.getX(), playerIn.getY(0.5D), playerIn.getZ());
                entity.setItem(stack);
                entity.getEntityData().set(EntityStructureFinder.COLOR, this.color);
                entity.signalTo(pos.above(64));
                levelIn.addFreshEntity(entity);

                stack.shrink(1);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
