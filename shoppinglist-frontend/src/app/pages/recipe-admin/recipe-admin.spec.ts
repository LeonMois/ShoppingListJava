import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeAdmin } from './recipe-admin';

describe('RecipeAdmin', () => {
  let component: RecipeAdmin;
  let fixture: ComponentFixture<RecipeAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
