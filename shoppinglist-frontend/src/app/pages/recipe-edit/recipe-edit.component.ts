import { Component, computed, inject, signal, ChangeDetectionStrategy } from '@angular/core';
import {
  RecipeAdminService,
  RecipeItemDto,
} from '../../service/recipe-admin.service';
import { httpResource } from '@angular/common/http';
import { RecipeDto } from '../../models/recipe.dto';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RecipeEditDetailComponent } from '../recipe-edit-detail/recipe-edit-detail.component';

@Component({
  selector: 'app-recipe-edit',
  imports: [ReactiveFormsModule, RecipeEditDetailComponent],
  templateUrl: './recipe-edit.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './recipe-edit.component.css',
})
export class RecipeEditComponent {
  recipeService = inject(RecipeAdminService);
  recipesResource = httpResource<RecipeDto[]>(() => '/api/recipes');

  recipes = this.recipesResource.value;
  filterControl = new FormControl('');
  filterQuery = toSignal(this.filterControl.valueChanges, { initialValue: '' });
  filteredRecipes = computed(() => {
    const query = this.filterQuery();
    return this.recipes()?.filter((recipe) =>
      recipe.name.toLowerCase().includes(query ? query.toLowerCase() : ''),
    );
  });

  private selectedRecipe = signal<RecipeDto | null>(null);
  private selectedItems = signal<RecipeItemDto[] | null>(null);

  detailData = computed(() => {
    const recipe = this.selectedRecipe();
    const items = this.selectedItems();
    if (recipe && items) return { recipe, items };
    return null;
  });

  addNewRecipe(name: string, amount: string) {
    this.recipeService
      .addRecipe({ name: name, servings: parseInt(amount) })
      .subscribe(() => this.recipesResource.reload());
  }
  selectRecipe(recipe: RecipeDto): void {
    this.recipeService.getRecipeItems(recipe.name).subscribe((items) => {
      this.selectedRecipe.set(recipe);
      this.selectedItems.set(items);
    });
  }

  deleteRecipe(recipe: RecipeDto): void {
    this.recipeService
      .deleteRecipe(recipe)
      .subscribe(() => this.recipesResource.reload());
  }

  closeDetail(): void {
    this.selectedRecipe.set(null);
    this.selectedItems.set(null);
  }
}
