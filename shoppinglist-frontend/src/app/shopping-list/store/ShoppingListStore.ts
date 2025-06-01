import { signalStore, withState, withMethods, patchState, withComputed } from '@ngrx/signals';
import { ShoppingListItem } from '../model/shopping-list-item-model';
import { ListService } from '../service/list-service';
import { inject } from '@angular/core';

type ShoppingListState = {
  items: ShoppingListItem[];
  sortOrder: string;
};

const initialState: ShoppingListState = {
  items: [],
  sortOrder: '',
};
export const ShoppingListStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store, shoppingListService = inject(ListService)) => ({
    getItemsByOrder(sortOrder: string): void {
      const items = shoppingListService.getAllItems();
      patchState(store, ))
    }
  }))
);
