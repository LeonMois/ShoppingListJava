import { CommonModule } from '@angular/common';
import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  computed,
  inject,
  signal,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ItemDto } from '../../models/item.dto';
import { RecipeDto } from '../../models/recipe.dto';
import {
  RecipeAdminService,
  RecipeItemDto,
} from '../../service/recipe-admin.service';

@Component({
  selector: 'app-recipe-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './recipe-admin.html',
  styleUrl: './recipe-admin.css',
})
export class RecipeAdmin implements OnInit {
  private readonly recipeAdminService = inject(RecipeAdminService);
  private readonly formBuilder = inject(FormBuilder);

  @ViewChild('editRecipeDialog', { static: true })
  private editRecipeDialog!: ElementRef<HTMLDialogElement>;

  readonly recipes = signal<RecipeDto[]>([]);
  readonly items = signal<ItemDto[]>([]);
  readonly recipeItems = signal<RecipeItemDto[]>([]);

  readonly selectedRecipeName = signal<string>('');
  readonly selectedRecipe = computed(
    () =>
      this.recipes().find((r) => r.name === this.selectedRecipeName()) ?? null,
  );

  readonly recipesLoading = signal(false);
  readonly itemsLoading = signal(false);
  readonly recipeItemsLoading = signal(false);
  readonly submitting = signal(false);

  readonly error = signal<string | null>(null);

  readonly sortedRecipes = computed(() =>
    [...this.recipes()].sort((a, b) => a.name.localeCompare(b.name)),
  );

  readonly sortedItems = computed(() =>
    [...this.items()].sort((a, b) => a.name.localeCompare(b.name)),
  );

  readonly createRecipeForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required]],
    servings: [1, [Validators.required, Validators.min(1)]],
  });

  readonly editRecipeForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required]],
    servings: [1, [Validators.required, Validators.min(1)]],
  });

  readonly addRecipeItemForm = this.formBuilder.nonNullable.group({
    itemName: [''],
    quantity: [1, [Validators.required, Validators.min(Number.MIN_VALUE)]],
  });

  ngOnInit(): void {
    this.loadItems();
    this.loadRecipes();
  }

  loadItems(): void {
    this.itemsLoading.set(true);
    this.recipeAdminService
      .getItems()
      .pipe(finalize(() => this.itemsLoading.set(false)))
      .subscribe({
        next: (items) => this.items.set(items),
        error: (err) => {
          console.error('Failed to load items', err);
          this.error.set('Failed to load items.');
        },
      });
  }

  loadRecipes(): void {
    this.recipesLoading.set(true);
    this.recipeAdminService
      .getRecipes()
      .pipe(finalize(() => this.recipesLoading.set(false)))
      .subscribe({
        next: (recipes) => {
          this.recipes.set(recipes);

          const current = this.selectedRecipeName();
          if (!current && recipes.length > 0) {
            this.selectRecipe(recipes[0]!.name);
          } else if (current && recipes.some((r) => r.name === current)) {
            this.selectRecipe(current);
          } else {
            this.selectedRecipeName.set('');
            this.recipeItems.set([]);
          }
        },
        error: (err) => {
          console.error('Failed to load recipes', err);
          this.error.set('Failed to load recipes.');
        },
      });
  }

  selectRecipe(recipeName: string): void {
    if (!recipeName) return;
    this.selectedRecipeName.set(recipeName);
    this.loadRecipeItems(recipeName);
  }

  loadRecipeItems(recipeName: string): void {
    this.recipeItemsLoading.set(true);
    this.recipeAdminService
      .getRecipeItems(recipeName)
      .pipe(finalize(() => this.recipeItemsLoading.set(false)))
      .subscribe({
        next: (items) => this.recipeItems.set(items),
        error: (err) => {
          console.error('Failed to load recipe items', err);
          this.error.set('Failed to load recipe items.');
        },
      });
  }

  addRecipe(): void {
    this.error.set(null);

    const name = this.createRecipeForm.controls.name.value.trim();
    const servings = this.createRecipeForm.controls.servings.value;

    if (!name) {
      this.error.set('Recipe name is required.');
      return;
    }
    if (!Number.isInteger(servings) || servings < 1) {
      this.error.set('Servings must be an integer of 1 or more.');
      return;
    }

    this.submitting.set(true);
    this.recipeAdminService
      .addRecipe({ name, servings })
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => {
          this.createRecipeForm.reset({ name: '', servings: 1 });
          this.loadRecipes();
        },
        error: (err) => {
          console.error('Failed to add recipe', err);
          this.error.set(
            'Failed to add recipe. The recipe might already exist.',
          );
        },
      });
  }

  deleteSelectedRecipe(): void {
    this.error.set(null);
    const recipe = this.selectedRecipe();
    if (!recipe) return;

    this.submitting.set(true);
    this.recipeAdminService
      .deleteRecipe(recipe)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => {
          this.selectedRecipeName.set('');
          this.recipeItems.set([]);
          this.loadRecipes();
        },
        error: (err) => {
          console.error('Failed to delete recipe', err);
          this.error.set('Failed to delete recipe.');
        },
      });
  }

  openEditRecipe(): void {
    const recipe = this.selectedRecipe();
    if (!recipe) return;
    this.editRecipeForm.reset({ name: recipe.name, servings: recipe.servings });
    this.editRecipeDialog.nativeElement.showModal();
  }

  closeEditRecipe(): void {
    this.editRecipeForm.reset({ name: '', servings: 1 });
  }

  dismissEditRecipe(): void {
    if (this.editRecipeDialog.nativeElement.open) {
      this.editRecipeDialog.nativeElement.close();
    } else {
      this.closeEditRecipe();
    }
  }

  saveRecipeEdit(): void {
    this.error.set(null);
    const original = this.selectedRecipe();
    if (!original) return;

    const updatedName = this.editRecipeForm.controls.name.value.trim();
    const updatedServings = this.editRecipeForm.controls.servings.value;

    if (!updatedName) {
      this.error.set('Recipe name is required.');
      return;
    }
    if (!Number.isInteger(updatedServings) || updatedServings < 1) {
      this.error.set('Servings must be an integer of 1 or more.');
      return;
    }

    if (
      original.name.trim().toLowerCase() === updatedName.toLowerCase() &&
      original.servings !== updatedServings
    ) {
      this.error.set(
        'Backend does not support updating servings without changing the recipe name.',
      );
      return;
    }

    const updated: RecipeDto = { name: updatedName, servings: updatedServings };
    this.submitting.set(true);
    this.recipeAdminService
      .updateRecipe(original, updated)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => {
          if (this.selectedRecipeName() === original.name) {
            this.selectedRecipeName.set(updated.name);
            this.recipeItems.update((items) =>
              items.map((it) => ({ ...it, recipeName: updated.name })),
            );
          }
          this.dismissEditRecipe();
          this.loadRecipes();
        },
        error: (err) => {
          console.error('Failed to update recipe', err);
          this.error.set(
            'Failed to update recipe. The new recipe might already exist.',
          );
        },
      });
  }

  addItemToRecipe(): void {
    this.error.set(null);
    const recipeName = this.selectedRecipeName().trim();
    if (!recipeName) {
      this.error.set('Select a recipe first.');
      return;
    }

    const itemName = this.addRecipeItemForm.controls.itemName.value.trim();
    const quantity = this.addRecipeItemForm.controls.quantity.value;
    const item = this.items().find((candidate) => candidate.name === itemName);

    if (!itemName || !item) {
      this.error.set('Select an item from the list.');
      return;
    }
    if (!(quantity > 0)) {
      this.error.set('Quantity must be greater than 0.');
      return;
    }

    this.recipeItems.update((items) => {
      const index = items.findIndex(
        (existing) =>
          existing.itemName === item.name && existing.unit === item.unit,
      );
      const nextItem: RecipeItemDto = {
        recipeName,
        itemName: item.name,
        unit: item.unit,
        category: item.category,
        quantity,
      };
      if (index >= 0) {
        return items.map((existing, i) => (i === index ? nextItem : existing));
      }
      return [...items, nextItem].sort((a, b) =>
        a.itemName.localeCompare(b.itemName),
      );
    });

    this.addRecipeItemForm.reset({ itemName: '', quantity: 1 });
  }

  removeRecipeItem(item: RecipeItemDto): void {
    this.recipeItems.update((items) =>
      items.filter(
        (existing) =>
          !(existing.itemName === item.itemName && existing.unit === item.unit),
      ),
    );
  }

  updateQuantity(item: RecipeItemDto, quantityRaw: unknown): void {
    const quantity =
      typeof quantityRaw === 'number' ? quantityRaw : Number(quantityRaw);
    this.recipeItems.update((items) =>
      items.map((existing) =>
        existing.itemName === item.itemName && existing.unit === item.unit
          ? { ...existing, quantity }
          : existing,
      ),
    );
  }

  saveRecipeItems(): void {
    this.error.set(null);
    const recipeName = this.selectedRecipeName().trim();
    if (!recipeName) return;

    const payload = this.recipeItems().map((item) => ({ ...item, recipeName }));
    if (payload.some((item) => !(item.quantity > 0))) {
      this.error.set('All quantities must be greater than 0.');
      return;
    }

    this.submitting.set(true);
    this.recipeAdminService
      .updateRecipeItems(payload)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: (items) => this.recipeItems.set(items),
        error: (err) => {
          console.error('Failed to save recipe items', err);
          this.error.set('Failed to save recipe items.');
        },
      });
  }
}
