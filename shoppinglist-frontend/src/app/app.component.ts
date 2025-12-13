import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { HttpClient, provideHttpClient } from '@angular/common/http';
import { Frontpage } from './pages/frontpage/frontpage';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Frontpage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'shoppinglist-frontend';
}
