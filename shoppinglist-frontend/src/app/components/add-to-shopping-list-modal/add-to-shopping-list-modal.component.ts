import { CommonModule } from '@angular/common';
import {
  Component,
  ElementRef,
  EventEmitter,
  OnInit,
  Output,
  ViewChild,
  inject,
  signal,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ItemDto } from '../../models/item.dto';
import { RecipeDto } from '../../models/recipe.dto';
import { CatalogService } from '../../service/catalog.service';
import { ShoppingListService } from '../../service/shopping-list.service';

@Component({
  selector: 'app-add-to-shopping-list-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-to-shopping-list-modal.component.html',
  styleUrl: './add-to-shopping-list-modal.component.css',
})
export class AddToShoppingListModalComponent implements OnInit {
  private readonly catalogService = inject(CatalogService);
  private readonly shoppingListService = inject(ShoppingListService);
  private readonly formBuilder = inject(FormBuilder);

  @ViewChild('dialog', { static: true })
  private dialog!: ElementRef<HTMLDialogElement>;

  @Output() shoppingListChanged = new EventEmitter<void>();

  readonly items = signal<ItemDto[]>([]);
  readonly recipes = signal<RecipeDto[]>([]);

  readonly itemsLoading = signal(false);
  readonly recipesLoading = signal(false);
  readonly itemSubmitting = signal(false);
  readonly recipeSubmitting = signal(false);

  readonly itemError = signal<string | null>(null);
  readonly recipeError = signal<string | null>(null);

  readonly itemForm = this.formBuilder.nonNullable.group({
    itemName: [''],
    quantity: [1, [Validators.required, Validators.min(Number.MIN_VALUE)]],
  });

  readonly recipeForm = this.formBuilder.nonNullable.group({
    recipeName: [''],
    servings: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    this.loadItems();
    this.loadRecipes();
  }

  open(): void {
    this.dialog.nativeElement.showModal();
  }

  resetAll(): void {
    this.resetItemSelection();
    this.resetRecipeSelection();
  }

  addItem(): void {
    this.itemError.set(null);

    const itemName = this.itemForm.controls.itemName.value.trim();
    const quantity = this.itemForm.controls.quantity.value;
    const item = this.items().find((candidate) => candidate.name === itemName);

    if (!itemName || !item) {
      this.itemError.set('Select an item from the list.');
      return;
    }
    if (!(quantity > 0)) {
      this.itemError.set('Quantity must be greater than 0.');
      return;
    }

    this.itemSubmitting.set(true);
    this.shoppingListService
      .addItems([
        {
          itemName: item.name,
          unitName: item.unit,
          category: item.category,
          quantity,
          deleted: false,
        },
      ])
      .pipe(finalize(() => this.itemSubmitting.set(false)))
      .subscribe({
        next: () => {
          this.shoppingListChanged.emit();
          this.resetItemSelection();
        },
        error: (err) => {
          console.error('Failed to add item to shopping list', err);
          this.itemError.set('Failed to add item. Please try again.');
        },
      });
  }

  addRecipe(): void {
    this.recipeError.set(null);

    const recipeName = this.recipeForm.controls.recipeName.value.trim();
    const servings = this.recipeForm.controls.servings.value;
    const recipe = this.recipes().find(
      (candidate) => candidate.name === recipeName,
    );

    if (!recipeName || !recipe) {
      this.recipeError.set('Select a recipe from the list.');
      return;
    }
    if (!Number.isInteger(servings) || servings < 1) {
      this.recipeError.set('Servings must be an integer of 1 or more.');
      return;
    }

    this.recipeSubmitting.set(true);
    this.shoppingListService
      .addRecipes([{ name: recipe.name, servings }])
      .pipe(finalize(() => this.recipeSubmitting.set(false)))
      .subscribe({
        next: () => {
          this.shoppingListChanged.emit();
          this.resetRecipeSelection();
        },
        error: (err) => {
          console.error('Failed to add recipe to shopping list', err);
          this.recipeError.set('Failed to add recipe. Please try again.');
        },
      });
  }

  private loadItems(): void {
    this.itemsLoading.set(true);
    this.catalogService
      .getItems()
      .pipe(finalize(() => this.itemsLoading.set(false)))
      .subscribe({
        next: (items) => this.items.set(items),
        error: (err) => {
          console.error('Failed to load items', err);
          this.itemError.set('Failed to load items.');
        },
      });
  }

  private loadRecipes(): void {
    this.recipesLoading.set(true);
    this.catalogService
      .getRecipes()
      .pipe(finalize(() => this.recipesLoading.set(false)))
      .subscribe({
        next: (recipes) => this.recipes.set(recipes),
        error: (err) => {
          console.error('Failed to load recipes', err);
          this.recipeError.set('Failed to load recipes.');
        },
      });
  }

  private resetItemSelection(): void {
    this.itemForm.reset({ itemName: '', quantity: 1 });
    this.itemError.set(null);
  }

  private resetRecipeSelection(): void {
    this.recipeForm.reset({ recipeName: '', servings: 1 });
    this.recipeError.set(null);
  }
}
