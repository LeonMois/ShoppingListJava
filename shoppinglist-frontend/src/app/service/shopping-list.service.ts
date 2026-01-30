import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RecipeDto } from '../models/recipe.dto';
import { ShoppingListItem } from '../models/shopping-list-item';

@Injectable({ providedIn: 'root' })
export class ShoppingListService {
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getItems(sortOrder?: string): Observable<ShoppingListItem[]> {
    const params = sortOrder
      ? new HttpParams().set('sortOrder', sortOrder)
      : undefined;
    return this.http.get<ShoppingListItem[]>(`${this.baseUrl}/shopping-list`, {
      params,
    });
  }

  addItems(items: ShoppingListItem[]): Observable<ShoppingListItem[]> {
    return this.http.post<ShoppingListItem[]>(
      `${this.baseUrl}/shopping-list/add/items`,
      items,
    );
  }

  addRecipes(recipes: RecipeDto[]): Observable<ShoppingListItem[]> {
    return this.http.post<ShoppingListItem[]>(
      `${this.baseUrl}/shopping-list/add/recipes`,
      recipes,
    );
  }

  toggleItems(items: ShoppingListItem[]): Observable<ShoppingListItem[]> {
    return this.http.put<ShoppingListItem[]>(
      `${this.baseUrl}/shopping-list/toggle/items`,
      items,
    );
  }

  deleteDeletedItems(): Observable<ShoppingListItem[]> {
    return this.http.delete<ShoppingListItem[]>(
      `${this.baseUrl}/shopping-list/delete`,
    );
  }
}
