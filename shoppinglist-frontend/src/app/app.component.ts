import { Component, signal, ChangeDetectionStrategy } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'shoppinglist-frontend';
  isOpen = signal<boolean>(false);

  setSidebar(open: boolean): void {
    this.isOpen.set(open);
  }

  toggleSidebar(): void {
    this.isOpen.update((open) => !open);
  }
}
