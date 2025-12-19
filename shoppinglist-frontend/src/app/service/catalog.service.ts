import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ItemDto } from '../models/item.dto';
import { RecipeDto } from '../models/recipe.dto';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getItems(): Observable<ItemDto[]> {
    return this.http.get<ItemDto[]>(`${this.baseUrl}/items`);
  }

  getRecipes(): Observable<RecipeDto[]> {
    return this.http.get<RecipeDto[]>(`${this.baseUrl}/recipes`);
  }
}

