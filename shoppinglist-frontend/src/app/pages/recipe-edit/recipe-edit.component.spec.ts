import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { RecipeEditComponent } from './recipe-edit.component';

describe('RecipeEditComponent', () => {
  let component: RecipeEditComponent;
  let fixture: ComponentFixture<RecipeEditComponent>;
  let httpMock: HttpTestingController;

  const pancakes = { name: 'Pancakes', servings: 2 };
  const waffles = { name: 'Waffles', servings: 4 };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeEditComponent, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeEditComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    httpMock.expectOne('/api/recipes').flush([pancakes, waffles]);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('loads recipes into the resource', () => {
    expect(component.recipes()).toEqual([pancakes, waffles]);
  });

  it('filters recipes by the filter control, case-insensitively', () => {
    component.filterControl.setValue('wa');
    expect(component.filteredRecipes()?.map((r) => r.name)).toEqual(['Waffles']);
  });

  it('has no detail data until a recipe is selected', () => {
    expect(component.detailData()).toBeNull();
  });

  it('selectRecipe loads recipe items and populates detailData', () => {
    component.selectRecipe(pancakes);

    httpMock.expectOne('/api/recipe-items/Pancakes').flush([
      {
        recipeName: 'Pancakes',
        itemName: 'Milk',
        unit: 'l',
        category: 'Dairy',
        quantity: 1,
      },
    ]);

    expect(component.detailData()?.recipe).toEqual(pancakes);
    expect(component.detailData()?.items.length).toBe(1);
  });

  it('closeDetail clears the selection', () => {
    component.selectRecipe(pancakes);
    httpMock.expectOne('/api/recipe-items/Pancakes').flush([]);

    component.closeDetail();
    expect(component.detailData()).toBeNull();
  });

  it('addNewRecipe posts the new recipe and reloads the resource', () => {
    component.addNewRecipe('Toast', '1');

    const req = httpMock.expectOne('/api/recipes/add');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ name: 'Toast', servings: 1 });
    req.flush({ name: 'Toast', servings: 1 });
    fixture.detectChanges();

    httpMock.expectOne('/api/recipes').flush([pancakes, waffles, { name: 'Toast', servings: 1 }]);
  });

  it('deleteRecipe removes the recipe and reloads the resource', () => {
    component.deleteRecipe(pancakes);

    const req = httpMock.expectOne('/api/recipes/delete');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
    fixture.detectChanges();

    httpMock.expectOne('/api/recipes').flush([waffles]);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
