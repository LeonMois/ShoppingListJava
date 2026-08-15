import { provideRouter } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarComponent } from './sidebar.component';

describe('SidebarComponent', () => {
  let fixture: ComponentFixture<SidebarComponent>;
  let component: SidebarComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('renders a navigation link for every route', () => {
    const links = fixture.nativeElement.querySelectorAll('a[routerLink]');
    const hrefs = Array.from(links as NodeListOf<HTMLAnchorElement>).map((a) =>
      a.getAttribute('routerLink'),
    );
    expect(hrefs).toEqual([
      '/',
      '/ingredients',
      '/recipes',
      '/handmade-recipes',
    ]);
  });

  it('labels each link', () => {
    const text = fixture.nativeElement.textContent as string;
    expect(text).toContain('Frontpage');
    expect(text).toContain('Ingredients Admin');
    expect(text).toContain('Recipe Admin');
    expect(text).toContain('Recipe Handmade');
  });
});
