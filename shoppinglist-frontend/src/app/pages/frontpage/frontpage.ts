import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { NgClass, NgFor } from '@angular/common';
import { AddToShoppingListModalComponent } from '../../components/add-to-shopping-list-modal/add-to-shopping-list-modal.component';
import { ShoppingListItem } from '../../models/shopping-list-item';
import { ShoppingListService } from '../../service/shopping-list.service';

@Component({
  selector: 'app-frontpage',
  standalone: true,
  imports: [NgClass, AddToShoppingListModalComponent],
  templateUrl: './frontpage.html',
  styleUrl: './frontpage.css',
})
export class Frontpage implements OnInit {
  private shoppingListService = inject(ShoppingListService);
  items = signal<ShoppingListItem[]>([]);
  sortOrder = signal<string>('');
  categories = computed(() =>
    Array.from(new Set(this.items().map((item) => item.category))).sort(),
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

  toggle(item: ShoppingListItem, deleted: boolean): void {
    const previous = item.deleted;
    item.deleted = deleted;
    const updated = { ...item, deleted };
    this.shoppingListService.toggleItems([updated]).subscribe({
      next: (res) => {
        const serverItem = res?.[0];
        item.deleted = serverItem?.deleted ?? deleted;
      },
      error: (err) => {
        console.error('Failed to toggle item', err);
        item.deleted = previous;
      },
    });
  }

  changeSort(order: string): void {
    this.sortOrder.set(order);
    this.loadItems();
  }
}
