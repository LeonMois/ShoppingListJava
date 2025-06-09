import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recipe } from '../model/recipe-model';

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private readonly http = inject(HttpClient);

  private readonly BASE_URL = '/api/recipes';

  getAllRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.BASE_URL);
  }
}
