import { Routes } from '@angular/router';
import { ListComponent } from './shopping-list/list/list.component';
import { HeaderComponent } from './header/header.component';

export const routes: Routes = [
  { path: '', component: ListComponent },
  { path: 'edit', component: HeaderComponent },
];
