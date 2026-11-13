package dxxi3d.recipes_unlocker.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void dxxi3d$RecipesUnlocker$unlockRecipes(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        this.dxxi3d$awardRecipes(player);
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void unlockRecipesOnDatapackReload(CallbackInfo ci) {
        for (ServerPlayer player : this.players) {
            this.dxxi3d$awardRecipes(player);
        }
    }

    @Unique
    private void dxxi3d$awardRecipes(ServerPlayer player) {
        if (this.server == null || player == null) {
            return;
        }

        Collection<RecipeHolder<?>> recipes = this.server.getRecipeManager().getRecipes();
        if (recipes.isEmpty()) {
            return;
        }

        player.awardRecipes(recipes);
    }
}