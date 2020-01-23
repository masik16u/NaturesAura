package de.ellpeck.naturesaura.blocks;

import de.ellpeck.naturesaura.Helper;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityNatureAltar;
import de.ellpeck.naturesaura.blocks.tiles.render.RenderNatureAltar;
import de.ellpeck.naturesaura.reg.ITESRProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class BlockNatureAltar extends BlockContainerImpl implements ITESRProvider {

    private static final VoxelShape SHAPE = VoxelShapes.create(0, 0, 0, 1, 12 / 16F, 1);

    public BlockNatureAltar() {
        super("nature_altar", TileEntityNatureAltar::new, ModBlocks.prop(Material.ROCK).hardnessAndResistance(4F).harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return Helper.putStackOnTile(player, handIn, pos, 0, true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Tuple<Class, TileEntityRenderer> getTESR() {
        return new Tuple<>(TileEntityNatureAltar.class, new RenderNatureAltar());
    }
}
