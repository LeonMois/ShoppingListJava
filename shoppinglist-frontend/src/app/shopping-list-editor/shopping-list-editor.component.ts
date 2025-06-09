import { AsyncPipe, NgFor } from '@angular/common';
import { Component, inject, ViewChild } from '@angular/core';
import { FormControl, FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { map, Observable, startWith } from 'rxjs';

import {
  MatAutocompleteModule,
  MatAutocompleteTrigger,
} from '@angular/material/autocomplete';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { Item } from '../shopping-list/model/item-model';
import { ItemsService } from '../shopping-list/service/items.service';
import { ListService } from '../shopping-list/service/list-service';
import { EditShoppingListItem } from '../shopping-list/model/shopping-list-item-model';
import { RecipeService } from '../shopping-list/service/recipe.service';
import { Recipe } from '../shopping-list/model/recipe-model';

@Component({
  selector: 'app-shopping-list-editor',
  templateUrl: './shopping-list-editor.component.html',
  styleUrls: ['./shopping-list-editor.component.scss'],
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    NgFor,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatIcon,
    AsyncPipe,
  ],
})
export class ShoppingListEditorComponent {
  itemsService = inject(ItemsService);
  shoppingListService = inject(ListService);
  recipeService = inject(RecipeService);
  recipes: string[] = [];
  recipesTyped: Recipe[] = [];
  items: string[] = [];
  itemsTyped: Item[] = [];
  recipeControl = new FormControl('');
  itemControl = new FormControl('');
  servings: number = 1;
  amount: number = 1;

  filteredRecipes: Observable<string[]> = new Observable<string[]>();
  filteredItems: Observable<string[]> = new Observable<string[]>();

  @ViewChild('autoRecipe', { read: MatAutocompleteTrigger })
  recipeAutoTrigger!: MatAutocompleteTrigger;
  @ViewChild('autoItem', { read: MatAutocompleteTrigger })
  itemAutoTrigger!: MatAutocompleteTrigger;

  constructor() {
    this.itemsService.getAllItems().subscribe((items) => {
      const itemsString: string[] = items.map((item) =>
        item.name.concat(' / ', item.unit.toString())
      );
      this.items = itemsString;
      this.itemsTyped = items;
      this.filteredItems = this.itemControl.valueChanges.pipe(
        startWith(''),
        map((value) => this._filter(value || '', this.items))
      );
    });

    this.recipeService.getAllRecipes().subscribe((recipes) => {
      const recipeStrings: string[] = recipes.map((recipe) =>
        recipe.name.concat(' / serves: ', recipe.servings.toString())
      );
      this.recipes = recipeStrings;
      this.recipesTyped = recipes;
      this.filteredRecipes = this.recipeControl.valueChanges.pipe(
        startWith(''),
        map((value) => this._filter(value || '', this.recipes))
      );
    });
  }

  private _filter(value: string, options: string[]): string[] {
    const filterValue = value.toLowerCase();
    return options.filter((option) =>
      option.toLowerCase().includes(filterValue)
    );
  }

  openRecipeAutocomplete(event: Event) {
    event.preventDefault();
    this.recipeAutoTrigger.openPanel();
  }
  openItemAutocomplete(event: Event) {
    event.preventDefault();
    this.itemAutoTrigger.openPanel();
  }

  addRecipe() {
    const recipeAndServings = this.recipeControl.value?.split('/');
    const recipe = recipeAndServings?.at(0)?.trim();

    const selection = this.recipesTyped.filter(
      (option) => option.name == recipe
    );

    const requestRecipes = selection.map((option) => {
      return <Recipe>{
        name: option.name,
        servings: option.servings * this.servings,
      };
    });

    this.shoppingListService.addRecipesToList(requestRecipes);

    this.recipeControl.reset();

    this.servings = 1;
  }

  addItem() {
    const itemAndUnit = this.itemControl.value?.split('/');
    const item = itemAndUnit?.at(0)?.trim();
    const selectedUnit = itemAndUnit?.at(1)?.trim();

    const selection = this.itemsTyped.filter(
      (option) => option.name == item && option.unit == selectedUnit
    );
    const requestItems = selection.map((option) => {
      return <EditShoppingListItem>{
        itemName: option.name,
        unitName: option.unit,
        quantity: this.amount,
        deleted: 0,
        category: '',
      };
    });
    this.shoppingListService.addItemsToList(requestItems);
    this.itemControl.reset();

    this.amount = 1;
  }
}
