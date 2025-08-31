import { Component, inject, signal } from '@angular/core';
import { SiblingDataService } from '../service/sibling-data.service';
@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  isMenuCollapsed: boolean = false;
  OPEN_TEXT = 'Open Menu';
  CLOSE_TEXT = 'Close Menu';
  buttonText = this.CLOSE_TEXT;
  siblingDataService = inject(SiblingDataService);

  toggleMenu(): void {
    this.siblingDataService.toggleMenu();
    this.buttonText = this.siblingDataService.isMenuCollapsed
      ? this.OPEN_TEXT
      : this.CLOSE_TEXT;
  }
}
