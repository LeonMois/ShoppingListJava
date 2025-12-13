import { Routes } from '@angular/router';
import { Frontpage } from './pages/frontpage/frontpage';
import { IngredientsAdmin } from './pages/ingredients-admin/ingredients-admin';
import { RecipeAdmin } from './pages/recipe-admin/recipe-admin';

export const routes: Routes = [
  { path: '', component: Frontpage },
  { path: 'ingredients', component: IngredientsAdmin },
  { path: 'recipes', component: RecipeAdmin },
];
