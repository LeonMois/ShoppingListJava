import { Component, computed, effect, inject, signal } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ListService } from '../shopping-list/service/list-service';
import { ShoppingListItem } from '../shopping-list/model/shopping-list-item-model';
import { FormsModule } from '@angular/forms';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

import { MatCheckboxModule } from '@angular/material/checkbox';
import { NgFor } from '@angular/common';
import { switchMap } from 'rxjs';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './ai-gen-list.component.html',
  styleUrls: ['./ai-gen-list.component.scss'],
  imports: [
    // Angular Material modules
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatSelectModule,
    MatCheckboxModule,
    MatInputModule,
    NgFor,
  ],
})
export class AiGenListComponent {
  isSidebarCollapsed = signal<boolean>(false);

  service = inject(ListService);
  items = signal<ShoppingListItem[]>([]);
  sortOrder = signal<string>('default');
  categories = computed(() =>
    Array.from(new Set(this.items()?.map((item) => item.category))).sort()
  );

  constructor() {
    effect(() => {
      const order = this.sortOrder;
      this.service
        .getAllItems(order())
        .subscribe((items) => this.items.set(items));
    });
  }

  toggleDeleted(item: ShoppingListItem) {
    item.deleted = item.deleted == 1 ? 0 : 1;
    this.service.toggleDeleted(item).subscribe();
  }
  changeSortOrder(sortOrder: String) {
    this.sortOrder.set(sortOrder.toString());
  }

  removeDeleted(): void {
    this.service
      .removeDeleted()
      .pipe(switchMap(() => this.service.getAllItems(this.sortOrder())))
      .subscribe({
        next: (items) => this.items.set(items),
        error: (err) => console.error('Error:', err),
      });
  }

  isChecked(item: ShoppingListItem): boolean {
    return item.deleted == 1 ? true : false;
  }
}
