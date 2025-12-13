import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { NgClass, NgFor } from '@angular/common';
import { ShoppingListService } from '../../service/shopping-list.service';

@Component({
  selector: 'app-frontpage',
  standalone: true,
  imports: [NgFor, NgClass],
  templateUrl: './frontpage.html',
  styleUrl: './frontpage.scss',
})
export class Frontpage implements OnInit {
  private shoppingListService = inject(ShoppingListService);
  items = signal<ShoppingListItem[]>([]);
  sortOrder = signal<string>('');
  categories = computed(() =>
    Array.from(new Set(this.items().map((item) => item.category))).sort()
  );

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.shoppingListService.getItems(this.sortOrder() || undefined).subscribe({
      next: (items) => this.items.set(items),
      error: (err) => console.error('Failed to load shopping list', err),
    });
  }

  toggle(item: ShoppingListItem): void {
    const updated = { ...item, deleted: !item.deleted };
    this.shoppingListService.toggleItems([updated]).subscribe({
      next: (res) => {
        const serverItem = res?.[0];
        item.deleted = serverItem?.deleted ?? updated.deleted;
      },
      error: (err) => {
        console.error('Failed to toggle item', err);
        item.deleted = item.deleted; // no-op, keeps current UI state
      },
    });
  }

  changeSort(order: string): void {
    this.sortOrder.set(order);
    this.loadItems();
  }
}

export interface ShoppingListItem {
  itemName: string;
  unitName: string;
  quantity: number;
  deleted: boolean;
  category: string;
}
