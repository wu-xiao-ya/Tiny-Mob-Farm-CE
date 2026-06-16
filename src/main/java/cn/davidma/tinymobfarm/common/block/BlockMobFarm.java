package cn.davidma.tinymobfarm.common.block;

import cn.davidma.tinymobfarm.core.MobFarmTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockMobFarm extends Block {
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    private final MobFarmTier tier;
    private final boolean mechanical;

    public BlockMobFarm(MobFarmTier tier, boolean mechanical) {
        super(tier.createBlockProperties());
        this.tier = tier;
        this.mechanical = mechanical;
        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalBlock.FACING, Direction.NORTH));
    }

    public MobFarmTier getTier() {
        return this.tier;
    }

    public boolean isMechanical() {
        return this.mechanical;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        LivingEntity placer = context.getPlayer();
        Direction facing = placer == null ? Direction.NORTH : placer.getDirection().getOpposite();
        return this.defaultBlockState().setValue(HorizontalBlock.FACING, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, net.minecraft.util.math.BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
