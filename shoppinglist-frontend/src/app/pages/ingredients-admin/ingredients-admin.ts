import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ItemDto } from '../../models/item.dto';
import { IngredientsAdminService } from '../../service/ingredients-admin.service';

@Component({
  selector: 'app-ingredients-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ingredients-admin.html',
  styleUrl: './ingredients-admin.scss',
})
export class IngredientsAdmin implements OnInit {
  private readonly ingredientsAdminService = inject(IngredientsAdminService);
  private readonly formBuilder = inject(FormBuilder);

  @ViewChild('editDialog', { static: true }) private editDialog!: ElementRef<HTMLDialogElement>;

  readonly items = signal<ItemDto[]>([]);
  readonly units = signal<string[]>([]);
  readonly categories = signal<string[]>([]);

  readonly itemsLoading = signal(false);
  readonly metaLoading = signal(false);
  readonly submitting = signal(false);

  readonly error = signal<string | null>(null);

  readonly selectedItem = signal<ItemDto | null>(null);

  readonly sortedItems = computed(() =>
    [...this.items()].sort((a, b) => a.name.localeCompare(b.name))
  );

  readonly createForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required]],
    category: ['', [Validators.required]],
    unit: ['', [Validators.required]],
  });

  readonly editForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required]],
    category: ['', [Validators.required]],
    unit: ['', [Validators.required]],
  });

  ngOnInit(): void {
    this.loadItems();
    this.loadMeta();
  }

  loadItems(): void {
    this.error.set(null);
    this.itemsLoading.set(true);
    this.ingredientsAdminService
      .getItems()
      .pipe(finalize(() => this.itemsLoading.set(false)))
      .subscribe({
        next: (items) => this.items.set(items),
        error: (err) => {
          console.error('Failed to load items', err);
          this.error.set('Failed to load items.');
        },
      });
  }

  loadMeta(): void {
    this.metaLoading.set(true);

    let pending = 2;
    const finalizeOne = () => {
      pending -= 1;
      if (pending <= 0) this.metaLoading.set(false);
    };

    this.ingredientsAdminService
      .getUnits()
      .pipe(finalize(finalizeOne))
      .subscribe({
        next: (units) => this.units.set(units.map((u) => u.unitName).sort()),
        error: (err) => {
          console.error('Failed to load units', err);
          this.error.set('Failed to load units.');
        },
      });

    this.ingredientsAdminService
      .getCategories()
      .pipe(finalize(finalizeOne))
      .subscribe({
        next: (categories) => this.categories.set(categories.map((c) => c.categoryName).sort()),
        error: (err) => {
          console.error('Failed to load categories', err);
          this.error.set('Failed to load categories.');
        },
      });
  }

  addItem(): void {
    this.error.set(null);

    const name = this.createForm.controls.name.value.trim();
    const category = this.createForm.controls.category.value.trim();
    const unit = this.createForm.controls.unit.value.trim();

    if (!name || !category || !unit) {
      this.error.set('Name, category and unit are required.');
      return;
    }

    this.submitting.set(true);
    this.ingredientsAdminService
      .addItem({ name, category, unit })
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => {
          this.createForm.reset({ name: '', category: '', unit: '' });
          this.loadItems();
          this.loadMeta();
        },
        error: (err) => {
          console.error('Failed to add item', err);
          this.error.set('Failed to add item. The item might already exist.');
        },
      });
  }

  openEdit(item: ItemDto): void {
    this.selectedItem.set(item);
    this.editForm.reset({ name: item.name, category: item.category, unit: item.unit });
    this.editDialog.nativeElement.showModal();
  }

  closeEdit(): void {
    this.selectedItem.set(null);
    this.editForm.reset({ name: '', category: '', unit: '' });
  }

  dismissEdit(): void {
    if (this.editDialog.nativeElement.open) {
      this.editDialog.nativeElement.close();
    } else {
      this.closeEdit();
    }
  }

  saveEdit(): void {
    this.error.set(null);
    const original = this.selectedItem();
    if (!original) return;

    const name = this.editForm.controls.name.value.trim();
    const category = this.editForm.controls.category.value.trim();
    const unit = this.editForm.controls.unit.value.trim();

    if (!name || !category || !unit) {
      this.error.set('Name, category and unit are required.');
      return;
    }

    const updated: ItemDto = { name, category, unit };
    this.submitting.set(true);
    this.ingredientsAdminService
      .updateItem(original, updated)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => {
          this.dismissEdit();
          this.loadItems();
          this.loadMeta();
        },
        error: (err) => {
          console.error('Failed to update item', err);
          this.error.set('Failed to update item. The new item might already exist.');
        },
      });
  }

  deleteItem(item: ItemDto): void {
    this.error.set(null);

    this.submitting.set(true);
    this.ingredientsAdminService
      .deleteItem(item)
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: () => this.loadItems(),
        error: (err) => {
          console.error('Failed to delete item', err);
          this.error.set('Failed to delete item.');
        },
      });
  }

}
