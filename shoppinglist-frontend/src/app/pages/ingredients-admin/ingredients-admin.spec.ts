import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { IngredientsAdmin } from './ingredients-admin';

describe('IngredientsAdmin', () => {
  let component: IngredientsAdmin;
  let fixture: ComponentFixture<IngredientsAdmin>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientsAdmin, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(IngredientsAdmin);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    httpMock.expectOne('http://localhost:8080/items').flush([]);
    httpMock.expectOne('http://localhost:8080/units').flush([]);
    httpMock.expectOne('http://localhost:8080/category').flush([]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  afterEach(() => {
    httpMock.verify();
  });
});
