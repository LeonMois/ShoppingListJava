import {
  Component,
  computed,
  inject,
  input,
  model,
  output,
  WritableSignal,
  ChangeDetectionStrategy
} from '@angular/core';
import { RecipeDto } from '../../models/recipe.dto';
import {
  RecipeAdminService,
  RecipeItemDto,
} from '../../service/recipe-admin.service';
import { IngredientsAdminService } from '../../service/ingredients-admin.service';
import { from } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ItemDto } from '../../models/item.dto';

@Component({
  selector: 'app-recipe-edit-detail',
  imports: [FormsModule],
  templateUrl: './recipe-edit-detail.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './recipe-edit-detail.component.css',
})
export class RecipeEditDetailComponent {
  recipeService = inject(RecipeAdminService);
  itemService = inject(IngredientsAdminService);
  recipe = model.required<RecipeDto>();
  items = model.required<RecipeItemDto[]>();
  private allItemsRaw = toSignal(this.itemService.getItems(), {
    initialValue: [],
  });

  allItems = computed(() => {
    const used = new Set(this.items().map((i) => i.itemName));
    return this.allItemsRaw().filter((i) => !used.has(i.name));
  });
  closed = output<void>();

  editRecipeItem(): void {
    this.recipeService.updateRecipeItems(this.items()).subscribe();
  }

  unselect(): void {
    this.closed.emit();
  }

  remove(item: RecipeItemDto) {
    this.items.update(() => this.items().filter((i) => i != item));
  }
  addNewItem() {
    this.items().push({
      recipeName: this.recipe().name,
      itemName: 'Select Item',
      unit: '',
      category: '',
      quantity: 0,
    });
    this.items.update(() => this.items());
  }

  save() {
    this.editRecipeItem();
    this.unselect();
  }
  onItemInputChange(item: RecipeItemDto, value: string) {
    const parts = value.split(' | ');
    if (parts.length === 2) {
      const found = this.allItems()?.find(
        (i) => i.name === parts[0] && i.unit === parts[1],
      );
      if (found) {
        item.itemName = found.name;
        item.unit = found.unit;
        item.category = found.category ?? '';
      }
    }
    this.items.update(() => [...this.items()]);
  }
}
