import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ShoppingListItem } from '../model/shopping-list-item-model';
import { Item } from '../model/item-model';

@Injectable({
  providedIn: 'root',
})
export class ItemsService {
  private readonly http = inject(HttpClient);

  private readonly BASE_URL = '/items';

  getAllItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.BASE_URL);
  }
}
