import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddToShoppingListModalComponent } from './add-to-shopping-list-modal.component';

describe('AddToShoppingListModalComponent', () => {
  let fixture: ComponentFixture<AddToShoppingListModalComponent>;
  let component: AddToShoppingListModalComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddToShoppingListModalComponent, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(AddToShoppingListModalComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();
    httpMock
      .expectOne('/api/items')
      .flush([{ name: 'Milk', category: 'Dairy', unit: 'l' }]);
    httpMock
      .expectOne('/api/recipes')
      .flush([{ name: 'Pancakes', servings: 2 }]);
  });

  it('resets only the item selection after adding an item', () => {
    spyOn(component.shoppingListChanged, 'emit');

    component.recipeForm.setValue({ recipeName: 'Pancakes', servings: 3 });
    component.itemForm.setValue({ itemName: 'Milk', quantity: 2 });

    component.addItem();

    httpMock
      .expectOne('/api/shopping-list/add/items')
      .flush([]);

    expect(component.shoppingListChanged.emit).toHaveBeenCalled();
    expect(component.itemForm.controls.itemName.value).toBe('');
    expect(component.itemForm.controls.quantity.value).toBe(1);
    expect(component.recipeForm.controls.recipeName.value).toBe('Pancakes');
    expect(component.recipeForm.controls.servings.value).toBe(3);
  });

  it('resets only the recipe selection after adding a recipe', () => {
    spyOn(component.shoppingListChanged, 'emit');

    component.itemForm.setValue({ itemName: 'Milk', quantity: 2 });
    component.recipeForm.setValue({ recipeName: 'Pancakes', servings: 4 });

    component.addRecipe();

    httpMock
      .expectOne('/api/shopping-list/add/recipes')
      .flush([]);

    expect(component.shoppingListChanged.emit).toHaveBeenCalled();
    expect(component.recipeForm.controls.recipeName.value).toBe('');
    expect(component.recipeForm.controls.servings.value).toBe(1);
    expect(component.itemForm.controls.itemName.value).toBe('Milk');
    expect(component.itemForm.controls.quantity.value).toBe(2);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
