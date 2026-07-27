import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'shoppinglist-frontend' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('shoppinglist-frontend');
  });

  it('starts with the sidebar closed', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.isOpen()).toBe(false);
  });

  it('setSidebar sets the open state explicitly', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.setSidebar(true);
    expect(app.isOpen()).toBe(true);

    app.setSidebar(false);
    expect(app.isOpen()).toBe(false);
  });

  it('toggleSidebar flips the open state', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.toggleSidebar();
    expect(app.isOpen()).toBe(true);

    app.toggleSidebar();
    expect(app.isOpen()).toBe(false);
  });
});
