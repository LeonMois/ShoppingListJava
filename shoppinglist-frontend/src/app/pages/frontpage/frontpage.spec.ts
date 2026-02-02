import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Frontpage } from './frontpage';

describe('Frontpage', () => {
  let component: Frontpage;
  let fixture: ComponentFixture<Frontpage>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Frontpage, HttpClientTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(Frontpage);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();

    httpMock.expectOne('/api/shopping-list').flush([]);
    httpMock.expectOne('/api/items').flush([]);
    httpMock.expectOne('/api/recipes').flush([]);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  afterEach(() => {
    httpMock.verify();
  });
});
