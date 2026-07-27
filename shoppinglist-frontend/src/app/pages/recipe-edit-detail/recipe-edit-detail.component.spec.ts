import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { RecipeEditDetailComponent } from './recipe-edit-detail.component';
import { RecipeItemDto } from '../../service/recipe-admin.service';

describe('RecipeEditDetailComponent', () => {
  let component: RecipeEditDetailComponent;
  let fixture: ComponentFixture<RecipeEditDetailComponent>;
  let httpMock: HttpTestingController;

  const milkItem: RecipeItemDto = {
    recipeName: 'Pancakes',
    itemName: 'Milk',
    unit: 'l',
    category: 'Dairy',
    quantity: 1,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeEditDetailComponent, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeEditDetailComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('recipe', { name: 'Pancakes', servings: 2 });
    fixture.componentRef.setInput('items', [milkItem]);
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    httpMock
      .expectOne('/api/items')
      .flush([
        { name: 'Milk', category: 'Dairy', unit: 'l' },
        { name: 'Flour', category: 'Baking', unit: 'kg' },
      ]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('excludes items already on the recipe from allItems', () => {
    expect(component.allItems().map((i) => i.name)).toEqual(['Flour']);
  });

  it('removes an item from the list', () => {
    component.remove(milkItem);
    expect(component.items()).toEqual([]);
  });

  it('addNewItem appends a placeholder item for the current recipe', () => {
    component.addNewItem();
    expect(component.items().length).toBe(2);
    expect(component.items()[1]).toEqual({
      recipeName: 'Pancakes',
      itemName: 'Select Item',
      unit: '',
      category: '',
      quantity: 0,
    });
  });

  it('onItemInputChange updates the item when a matching catalog item is selected', () => {
    component.addNewItem();
    const newItem = component.items()[1];

    component.onItemInputChange(newItem, 'Flour | kg');

    expect(newItem.itemName).toBe('Flour');
    expect(newItem.unit).toBe('kg');
    expect(newItem.category).toBe('Baking');
  });

  it('onItemInputChange leaves the item untouched if there is no match', () => {
    const before = { ...milkItem };
    component.onItemInputChange(milkItem, 'Unknown | thing');
    expect(milkItem).toEqual(before);
  });

  it('unselect emits the closed event', () => {
    spyOn(component.closed, 'emit');
    component.unselect();
    expect(component.closed.emit).toHaveBeenCalled();
  });

  it('save persists the items and emits closed', () => {
    spyOn(component.closed, 'emit');
    component.save();

    const req = httpMock.expectOne('/api/recipe-items/update');
    expect(req.request.method).toBe('PUT');
    req.flush([]);

    expect(component.closed.emit).toHaveBeenCalled();
  });

  afterEach(() => {
    httpMock.verify();
  });
});
