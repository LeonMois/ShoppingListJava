import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  router = inject(Router);

  edit() {
    this.router.navigateByUrl('/edit');
  }
  list() {
    this.router.navigateByUrl('/');
  }
}
