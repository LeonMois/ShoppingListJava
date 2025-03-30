import { signalStore, withState, withMethods } from '@ngrx/signals';
import { ShoppingListItem } from '../model/shopping-list-item-model';

type ShoppingListState = {
  items: ShoppingListItem[];
};

const initialState: ShoppingListState = {
  items: [],
};
export const ShoppingListStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods(store => {
    loadItems() {
        patchState(store, (state) =>({items: {...state.items}}) )
    },

  })
);
