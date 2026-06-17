package cn.davidma.tinymobfarm.common.block;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.MobFarmTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

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

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMobFarm();
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityMobFarm) {
            TileEntityMobFarm farm = (TileEntityMobFarm) tileEntity;
            farm.setMobFarmTier(this.tier);
            farm.setMechanical(this.mechanical);
            farm.updateRedstone();
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (!world.isClientSide && tileEntity instanceof TileEntityMobFarm) {
            TileEntityMobFarm farm = (TileEntityMobFarm) tileEntity;
            farm.updateRedstone();
            farm.onNeighborOutputChanged();
            farm.setChangedAndSync();
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (!world.isClientSide && tileEntity instanceof TileEntityMobFarm) {
                ((TileEntityMobFarm) tileEntity).dropLasso();
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TileEntityMobFarm && player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityMobFarm) tileEntity, pos);
            }
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }
}
