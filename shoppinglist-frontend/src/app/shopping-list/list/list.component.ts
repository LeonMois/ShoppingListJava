import { Component, inject, Signal } from '@angular/core';
import { ShoppingListItem } from '../model/shopping-list-item-model';
import { ListServiceService } from '../service/list-service.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-list',
  imports: [NgClass],
  templateUrl: './list.component.html',
  styleUrl: './list.component.scss',
})
export class ListComponent {
  service = inject(ListServiceService);
  items: Signal<ShoppingListItem[] | undefined>;
  constructor() {
    this.items = toSignal(this.service.getAllItems());
  }

  toggleDeleted(item: ShoppingListItem) {
    item.deleted = item.deleted == 1 ? 0 : 1;
    this.service.toggleDeleted(item).subscribe();
  }
}
