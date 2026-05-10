import {
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import {
  RecipeAdminService,
  RecipeItemDto,
} from '../../service/recipe-admin.service';
import { RecipeDto } from '../../models/recipe.dto';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RecipeEditDetailComponent } from '../recipe-edit-detail/recipe-edit-detail.component';

@Component({
  selector: 'app-recipe-edit',
  imports: [ReactiveFormsModule, RecipeEditDetailComponent],
  templateUrl: './recipe-edit.component.html',
  styleUrl: './recipe-edit.component.css',
})
export class RecipeEditComponent {
  recipeService = inject(RecipeAdminService);

  recipes = toSignal(this.recipeService.getRecipes());
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

  selectRecipe(recipe: RecipeDto): void {
    this.recipeService
      .getRecipeItems(recipe.name)
      .subscribe((items) => {
        this.selectedRecipe.set(recipe);
        this.selectedItems.set(items);
      });
  }

  closeDetail(): void {
    this.selectedRecipe.set(null);
    this.selectedItems.set(null);
  }
}
