import { TestBed } from '@angular/core/testing';

import { SiblingDataService } from '../sibling-data.service';

describe('SiblingDataService', () => {
  let service: SiblingDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SiblingDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
