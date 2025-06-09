import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingListEditorComponent } from './shopping-list-editor.component';

describe('ShoppingListEditorComponent', () => {
  let component: ShoppingListEditorComponent;
  let fixture: ComponentFixture<ShoppingListEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShoppingListEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShoppingListEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
