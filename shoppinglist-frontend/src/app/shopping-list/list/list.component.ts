import {
  Component,
  computed,
  effect,
  inject,
  signal,
  Signal,
} from '@angular/core';
import { ShoppingListItem } from '../model/shopping-list-item-model';
import { ListService } from '../service/list-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { NgClass, NgFor } from '@angular/common';
import { Observable } from 'rxjs';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list',
  imports: [
    NgClass,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    NgFor,
  ],
  templateUrl: './list.component.html',
  styleUrl: './list.component.scss',
})
export class ListComponent {
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
}
