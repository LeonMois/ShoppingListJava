import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RecipeAdmin } from './recipe-admin';

describe('RecipeAdmin', () => {
  let component: RecipeAdmin;
  let fixture: ComponentFixture<RecipeAdmin>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeAdmin, HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeAdmin);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    httpMock.expectOne('http://localhost:8080/items').flush([]);
    httpMock.expectOne('http://localhost:8080/recipes').flush([{ name: 'Test Recipe', servings: 2 }]);
    httpMock.expectOne('http://localhost:8080/recipe-items/Test%20Recipe').flush([]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  afterEach(() => {
    httpMock.verify();
  });
});
