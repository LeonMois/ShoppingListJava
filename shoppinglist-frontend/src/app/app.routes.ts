import { Routes } from '@angular/router';
import { ListComponent } from './shopping-list/list/list.component';
import { HeaderComponent } from './header/header.component';
import { AiGenListComponent } from './ai-gen-list/ai-gen-list.component';
import { ShoppingListEditorComponent } from './shopping-list-editor/shopping-list-editor.component';

export const routes: Routes = [
  { path: '', component: AiGenListComponent },
  { path: 'createShoppingList', component: ShoppingListEditorComponent },
];
