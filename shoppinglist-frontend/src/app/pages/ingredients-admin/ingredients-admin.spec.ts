import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsAdmin } from './ingredients-admin';

describe('IngredientsAdmin', () => {
  let component: IngredientsAdmin;
  let fixture: ComponentFixture<IngredientsAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientsAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngredientsAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
