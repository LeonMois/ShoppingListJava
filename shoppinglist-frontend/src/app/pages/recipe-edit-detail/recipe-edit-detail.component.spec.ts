import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeEditDetailComponent } from './recipe-edit-detail.component';

describe('RecipeEditDetailComponent', () => {
  let component: RecipeEditDetailComponent;
  let fixture: ComponentFixture<RecipeEditDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeEditDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeEditDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
