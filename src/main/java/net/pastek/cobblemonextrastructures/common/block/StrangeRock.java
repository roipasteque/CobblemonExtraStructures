package net.pastek.cobblemonextrastructures.common.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class StrangeRock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> SHAPES = calculateShapes();

    public StrangeRock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    private static Map<Direction, VoxelShape> calculateShapes() {
        VoxelShape northShape = Shapes.or(
                Block.box(0, 0, 3, 16, 14, 13),
                Block.box(1, 14, 3, 15, 15, 13),
                Block.box(2, 15, 4, 14, 16, 12),
                Block.box(14, 0, 2, 15, 14, 3),
                Block.box(1, 0, 2, 2, 14, 3),
                Block.box(2, 0, 2.25, 14, 13, 3),
                Block.box(2, 12.75, 2.5, 14, 13.75, 3.25),
                Block.box(2, 0.1, 2, 14, 1.1, 2.75),
                Block.box(1, 0, 13, 15, 14, 14)
        );

        return ImmutableMap.<Direction, VoxelShape>builder()
                .put(Direction.NORTH, northShape)
                .put(Direction.SOUTH, rotateShape(Direction.NORTH, Direction.SOUTH, northShape))
                .put(Direction.EAST, rotateShape(Direction.NORTH, Direction.EAST, northShape))
                .put(Direction.WEST, rotateShape(Direction.NORTH, Direction.WEST, northShape))
                .build();
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = {shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }
}