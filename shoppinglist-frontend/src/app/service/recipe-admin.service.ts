import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ItemDto } from '../models/item.dto';
import { RecipeDto } from '../models/recipe.dto';

export interface RecipeItemDto {
  recipeName: string;
  itemName: string;
  unit: string;
  category: string;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class RecipeAdminService {
  private readonly baseUrl = '/api';

  constructor(private http: HttpClient) {}

  getItems(): Observable<ItemDto[]> {
    return this.http.get<ItemDto[]>(`${this.baseUrl}/items`);
  }

  getRecipes(): Observable<RecipeDto[]> {
    return this.http.get<RecipeDto[]>(`${this.baseUrl}/recipes`);
  }

  addRecipe(recipe: RecipeDto): Observable<RecipeDto> {
    return this.http.post<RecipeDto>(`${this.baseUrl}/recipes/add`, recipe);
  }

  deleteRecipe(recipe: RecipeDto): Observable<RecipeDto> {
    return this.http.delete<RecipeDto>(`${this.baseUrl}/recipes/delete`, {
      body: recipe,
    });
  }

  updateRecipe(
    oldRecipe: RecipeDto,
    updatedRecipe: RecipeDto,
  ): Observable<RecipeDto> {
    return this.http.put<RecipeDto>(`${this.baseUrl}/recipes/update`, [
      oldRecipe,
      updatedRecipe,
    ]);
  }

  getRecipeItems(recipeName: string): Observable<RecipeItemDto[]> {
    return this.http.get<RecipeItemDto[]>(
      `${this.baseUrl}/recipe-items/${encodeURIComponent(recipeName)}`,
    );
  }

  updateRecipeItems(items: RecipeItemDto[]): Observable<RecipeItemDto[]> {
    return this.http.put<RecipeItemDto[]>(
      `${this.baseUrl}/recipe-items/update`,
      items,
    );
  }
}
