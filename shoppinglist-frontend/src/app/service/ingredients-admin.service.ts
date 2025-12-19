import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ItemDto } from '../models/item.dto';

interface UnitDto {
  unitName: string;
}

interface CategoryDto {
  categoryName: string;
}

@Injectable({ providedIn: 'root' })
export class IngredientsAdminService {
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getItems(): Observable<ItemDto[]> {
    return this.http.get<ItemDto[]>(`${this.baseUrl}/items`);
  }

  addItem(item: ItemDto): Observable<ItemDto> {
    return this.http.post<ItemDto>(`${this.baseUrl}/items/item/add`, item);
  }

  updateItem(oldItem: ItemDto, updatedItem: ItemDto): Observable<ItemDto> {
    return this.http.put<ItemDto>(`${this.baseUrl}/items/item/update`, [oldItem, updatedItem]);
  }

  deleteItem(item: ItemDto): Observable<ItemDto> {
    return this.http.delete<ItemDto>(`${this.baseUrl}/items/item/delete`, { body: item });
  }

  getUnits(): Observable<UnitDto[]> {
    return this.http.get<UnitDto[]>(`${this.baseUrl}/units`);
  }

  getCategories(): Observable<CategoryDto[]> {
    return this.http.get<CategoryDto[]>(`${this.baseUrl}/category`);
  }
}

