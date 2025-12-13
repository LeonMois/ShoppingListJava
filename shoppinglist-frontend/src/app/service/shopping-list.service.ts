import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ShoppingListItem } from '../pages/frontpage/frontpage';

@Injectable({ providedIn: 'root' })
export class ShoppingListService {
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getItems(sortOrder?: string): Observable<ShoppingListItem[]> {
    const params = sortOrder ? new HttpParams().set('sortOrder', sortOrder) : undefined;
    return this.http.get<ShoppingListItem[]>(`${this.baseUrl}/shopping-list`, { params });
  }

  toggleItems(items: ShoppingListItem[]): Observable<ShoppingListItem[]> {
    return this.http.put<ShoppingListItem[]>(`${this.baseUrl}/shopping-list/toggle/items`, items);
  }
}
