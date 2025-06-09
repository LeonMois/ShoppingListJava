import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  EditShoppingListItem,
  ShoppingListItem,
} from '../model/shopping-list-item-model';
import { Recipe } from '../model/recipe-model';

@Injectable({
  providedIn: 'root',
})
export class ListService {
  private readonly http = inject(HttpClient);

  private readonly BASE_URL = '/api/shopping-list';

  getAllItems(sortOrder: String): Observable<ShoppingListItem[]> {
    return this.http.get<ShoppingListItem[]>(
      this.BASE_URL + '?sortOrder=' + sortOrder
    );
  }

  toggleDeleted(item: ShoppingListItem): Observable<ShoppingListItem[]> {
    return this.http.put<ShoppingListItem[]>(this.BASE_URL + '/toggle/items', [
      item,
    ]);
  }

  removeDeleted(): Observable<ShoppingListItem[]> {
    return this.http.delete<ShoppingListItem[]>(this.BASE_URL + '/delete');
  }

  addItemsToList(items: EditShoppingListItem[]): void {
    this.http
      .post<ShoppingListItem[]>(this.BASE_URL + '/add/items', items)
      .subscribe();
  }

  addRecipeToList(recipes: Recipe[]): void {
    this.http
      .post<ShoppingListItem[]>(this.BASE_URL + '/add/recipes', recipes)
      .subscribe();
  }

  addRecipesToList(recipes: Recipe[]): void {
    this.http
      .post<Recipe[]>(this.BASE_URL + '/add/recipes', recipes)
      .subscribe();
  }
}
