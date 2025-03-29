import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ShoppingListItem } from './shopping-list-item-model';

@Injectable({
  providedIn: 'root',
})
export class ListServiceService {
  private readonly http = inject(HttpClient);

  private readonly BASE_URL = '/shopping-list';

  getAllItems(): Observable<ShoppingListItem[]> {
    return this.http.get<ShoppingListItem[]>(this.BASE_URL);
  }
}
