import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Frontpage } from './frontpage';
import { ShoppingListItem } from '../../models/shopping-list-item';

describe('Frontpage', () => {
  let component: Frontpage;
  let fixture: ComponentFixture<Frontpage>;
  let httpMock: HttpTestingController;

  let milk: ShoppingListItem;
  let bread: ShoppingListItem;

  beforeEach(async () => {
    milk = {
      itemName: 'Milk',
      unitName: 'l',
      quantity: 2,
      deleted: false,
      category: 'Dairy',
    };
    bread = {
      itemName: 'Bread',
      unitName: 'pcs',
      quantity: 1,
      deleted: true,
      category: 'Bakery',
    };

    await TestBed.configureTestingModule({
      imports: [Frontpage, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(Frontpage);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();

    httpMock.expectOne((r) => r.url === '/api/shopping-list').flush([milk, bread]);
    httpMock.expectOne('/api/items').flush([]);
    httpMock.expectOne('/api/recipes').flush([]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('loads shopping list items on init', () => {
    expect(component.items()).toEqual([milk, bread]);
  });

  it('sorts active items before deleted items', () => {
    const sorted = component.sortedItems();
    expect(sorted.map((i) => i.itemName)).toEqual(['Milk', 'Bread']);
  });

  it('derives the distinct, sorted category list', () => {
    expect(component.categories()).toEqual(['Bakery', 'Dairy']);
  });

  it('toggles an item optimistically and reconciles with the server response', () => {
    component.toggle(milk, true);

    expect(milk.deleted).toBe(true);

    const req = httpMock.expectOne('/api/shopping-list/toggle/items');
    expect(req.request.method).toBe('PUT');
    req.flush([{ ...milk, deleted: true }]);

    expect(milk.deleted).toBe(true);
  });

  it('reverts the toggle if the request fails', () => {
    spyOn(console, 'error');
    component.toggle(milk, true);

    const req = httpMock.expectOne('/api/shopping-list/toggle/items');
    req.error(new ProgressEvent('error'));

    expect(milk.deleted).toBe(false);
  });

  it('removeDeletedItems deletes then reloads the list', () => {
    component.removeDeletedItems();

    httpMock.expectOne('/api/shopping-list/delete').flush([]);
    httpMock.expectOne((r) => r.url === '/api/shopping-list').flush([]);

    expect(component.items()).toEqual([]);
  });

  it('changeSort updates sortOrder and reloads with the new order', () => {
    component.changeSort('category');

    expect(component.sortOrder()).toBe('category');
    const req = httpMock.expectOne(
      (r) =>
        r.url === '/api/shopping-list' && r.params.get('sortOrder') === 'category',
    );
    req.flush([]);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
