import { Component, inject, Signal } from '@angular/core';
import { ShoppingListItem } from '../shopping-list-item-model';
import { ListServiceService } from '../list-service.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-list',
  imports: [],
  templateUrl: './list.component.html',
  styleUrl: './list.component.scss',
})
export class ListComponent {
  service = inject(ListServiceService);
  items: Signal<ShoppingListItem[] | undefined>;
  constructor() {
    this.items = toSignal(this.service.getAllItems());
    console.log(this.items);
  }
}
