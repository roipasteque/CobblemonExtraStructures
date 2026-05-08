package net.pastek.cobblemonextrastructures.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.pastek.cobblemonextrastructures.common.inventory.container.ConditionalPokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.tile.TileConditionalPokemonSpawner;
import net.pastek.cobblemonextrastructures.registers.EXTiles;
import org.jetbrains.annotations.Nullable;

public class ConditionalPokemonSpawnerBlock extends BaseEntityBlock {

    public ConditionalPokemonSpawnerBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<ConditionalPokemonSpawnerBlock> CODEC = simpleCodec(ConditionalPokemonSpawnerBlock::new);

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileConditionalPokemonSpawner(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player.isCreative()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof TileConditionalPokemonSpawner spawnerEntity) {
                player.openMenu(new SimpleMenuProvider((id, inventory, p) ->
                                new ConditionalPokemonSpawnerMenu(id, inventory, spawnerEntity), Component.literal("Conditional Spawner Settings")),
                        buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, EXTiles.CONDITIONAL_POKEMON_SPAWNER, TileConditionalPokemonSpawner::tick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}