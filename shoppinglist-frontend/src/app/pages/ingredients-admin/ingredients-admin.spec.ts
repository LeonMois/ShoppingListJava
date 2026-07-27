import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { IngredientsAdmin } from './ingredients-admin';
import { ItemDto } from '../../models/item.dto';

describe('IngredientsAdmin', () => {
  let component: IngredientsAdmin;
  let fixture: ComponentFixture<IngredientsAdmin>;
  let httpMock: HttpTestingController;

  const milk: ItemDto = { name: 'Milk', category: 'Dairy', unit: 'l' };
  const bread: ItemDto = { name: 'Bread', category: 'Bakery', unit: 'pcs' };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientsAdmin, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(IngredientsAdmin);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    httpMock.expectOne('/api/items').flush([milk, bread]);
    httpMock.expectOne('/api/units').flush([{ unitName: 'l' }, { unitName: 'pcs' }]);
    httpMock
      .expectOne('/api/category')
      .flush([{ categoryName: 'Dairy' }, { categoryName: 'Bakery' }]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('loads items sorted by name and loads units/categories sorted', () => {
    expect(component.sortedItems().map((i) => i.name)).toEqual(['Bread', 'Milk']);
    expect(component.units()).toEqual(['l', 'pcs']);
    expect(component.categories()).toEqual(['Bakery', 'Dairy']);
  });

  it('filters items by the search string', () => {
    component.searchString.set('Mil');
    expect(component.searchItems().map((i) => i.name)).toEqual(['Milk']);
  });

  it('paginates items', () => {
    expect(component.totalPages()).toBe(1);
    expect(component.pagedItems().length).toBe(2);
    expect(component.paginationLabel()).toBe('Showing 1-2 of 2');
  });

  it('changePage clamps to the valid range', () => {
    component.changePage(99);
    expect(component.currentPage()).toBe(component.totalPages());

    component.changePage(-5);
    expect(component.currentPage()).toBe(1);
  });

  it('rejects adding an item with missing fields', () => {
    component.createForm.setValue({ name: '', category: 'Dairy', unit: 'l' });
    component.addItem();
    expect(component.error()).toBe('Name, category and unit are required.');
  });

  it('adds an item and reloads items and metadata', () => {
    component.createForm.setValue({ name: 'Eggs', category: 'Dairy', unit: 'pcs' });
    component.addItem();

    httpMock.expectOne('/api/items/item/add').flush({});

    httpMock.expectOne('/api/items').flush([milk, bread]);
    httpMock.expectOne('/api/units').flush([]);
    httpMock.expectOne('/api/category').flush([]);

    expect(component.createForm.controls.name.value).toBe('');
  });

  it('surfaces an error when adding an item fails', () => {
    spyOn(console, 'error');
    component.createForm.setValue({ name: 'Eggs', category: 'Dairy', unit: 'pcs' });
    component.addItem();

    httpMock.expectOne('/api/items/item/add').error(new ProgressEvent('error'));

    expect(component.error()).toBe(
      'Failed to add item. The item might already exist.',
    );
  });

  it('openEdit populates the edit form and opens the dialog, closeEdit resets it', () => {
    component.openEdit(milk);
    expect(component.selectedItem()).toEqual(milk);
    expect(component.editForm.value).toEqual({
      newName: 'Milk',
      newCategory: 'Dairy',
      newUnit: 'l',
    });

    component.closeEdit();
    expect(component.selectedItem()).toBeNull();
    expect(component.editForm.controls.newName.value).toBe('');
  });

  it('saveEdit updates the item then reloads items and metadata', () => {
    component.openEdit(milk);
    component.editForm.setValue({
      newName: 'Whole Milk',
      newCategory: 'Dairy',
      newUnit: 'l',
    });

    component.saveEdit();

    const req = httpMock.expectOne('/api/items/item/update');
    expect(req.request.method).toBe('PUT');
    req.flush({});

    httpMock.expectOne('/api/items').flush([]);
    httpMock.expectOne('/api/units').flush([]);
    httpMock.expectOne('/api/category').flush([]);
  });

  it('deleteItem removes the item and reloads the list', () => {
    component.deleteItem(milk);

    const req = httpMock.expectOne('/api/items/item/delete');
    expect(req.request.method).toBe('DELETE');
    req.flush({});

    httpMock.expectOne('/api/items').flush([]);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
