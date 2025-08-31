import { Component, computed, inject, input, signal } from '@angular/core';
import { Router } from '@angular/router';
import { SiblingDataService } from '../service/sibling-data.service';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  signalDataService = inject(SiblingDataService);
  //isMenuCollapsed = signal(() => this.signalDataService.isMenuCollapsed);

  router = inject(Router);

  setRoute(path: string): void {
    this.router.navigate([path]);
  }
}
