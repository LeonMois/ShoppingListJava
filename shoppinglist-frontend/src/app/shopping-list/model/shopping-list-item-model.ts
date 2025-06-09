export interface ShoppingListItem {
  itemName: String;
  unitName: String;
  quantity: number;
  deleted: number;
  category: String;
}

export interface EditShoppingListItem {
  itemName: String;
  unitName: String;
  quantity: number;
  deleted: number;
}
