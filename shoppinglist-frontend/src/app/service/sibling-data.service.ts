import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SiblingDataService {
  isMenuCollapsed: boolean = false;
  constructor() {}
  toggleMenu(): void {
    this.isMenuCollapsed = this.isMenuCollapsed ? false : true;
  }
}
