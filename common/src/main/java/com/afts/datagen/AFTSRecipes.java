package com.afts.datagen;

import com.afts.item.ArmorSets;
import com.afts.item.MaterialItems;
import com.afts.item.SmithingTemplates;
import com.afts.item.Weapons;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static com.afts.AFTSMod.MOD_ID;

public class AFTSRecipes extends FabricRecipeProvider {
    public AFTSRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Weapons.fotv.item())
                .pattern(" C ")
                .pattern("BCB")
                .pattern(" A ")
                .input('C', MaterialItems.voids_eye.item())
                .input('B', Items.ENDER_PEARL)
                .input('A', Items.END_STONE)
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MaterialItems.voids_eye.item())
                .pattern("AAA")
                .pattern("ACB")
                .pattern("BBB")
                .input('C', Items.ENDER_EYE)
                .input('B', Items.OBSIDIAN)
                .input('A', Items.AMETHYST_SHARD)
                .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MaterialItems.null_ingot.item())
                .pattern("CCC")
                .pattern("CAB")
                .pattern("BBB")
                .input('C', MaterialItems.voids_eye.item())
                .input('B', Items.DRAGON_BREATH)
                .input('A', Items.NETHERITE_INGOT)
                .criterion(hasItem(Items.DRAGON_BREATH), conditionsFromItem(Items.DRAGON_BREATH))
                .offerTo(recipeExporter);

        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(SmithingTemplates.NULL_UPGRADE.item().get()),
                Ingredient.ofItems(Weapons.fotv.item()),
                Ingredient.ofItems(MaterialItems.null_ingot.item()),
                RecipeCategory.COMBAT,
                Weapons.void_devourer.item()
        ).criterion(hasItem(MaterialItems.null_ingot.item()), conditionsFromItem(MaterialItems.null_ingot.item()))
                .offerTo(recipeExporter,Identifier.of(MOD_ID, "void_devourer_upgrade"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorSets.dragonArmorSet.armorSet().head.asItem())
                .pattern("AAA")
                .pattern("A A")
                .input('A', MaterialItems.dragon_scales.item())
                .criterion(hasItem(MaterialItems.dragon_scales.item()), conditionsFromItem(MaterialItems.dragon_scales.item()))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorSets.dragonArmorSet.armorSet().chest.asItem())
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .input('A', MaterialItems.dragon_scales.item())
                .criterion(hasItem(MaterialItems.dragon_scales.item()), conditionsFromItem(MaterialItems.dragon_scales.item()))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorSets.dragonArmorSet.armorSet().legs.asItem())
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .input('A', MaterialItems.dragon_scales.item())
                .criterion(hasItem(MaterialItems.dragon_scales.item()), conditionsFromItem(MaterialItems.dragon_scales.item()))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorSets.dragonArmorSet.armorSet().feet.asItem())
                .pattern("A A")
                .pattern("A A")
                .input('A', MaterialItems.dragon_scales.item())
                .criterion(hasItem(MaterialItems.dragon_scales.item()), conditionsFromItem(MaterialItems.dragon_scales.item()))
                .offerTo(recipeExporter);
    }
}
