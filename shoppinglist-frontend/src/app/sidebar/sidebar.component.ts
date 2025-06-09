import { Component, input, Signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FormsModule } from '@angular/forms';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-sidebar',
  imports: [
    FormsModule,
    MatExpansionModule,
    MatToolbarModule,
    MatListModule,
    MatIcon,
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  isMenuCollapsed: boolean = true;
  toggleMenu(): void {
    this.isMenuCollapsed = !this.isMenuCollapsed;
  }
}
