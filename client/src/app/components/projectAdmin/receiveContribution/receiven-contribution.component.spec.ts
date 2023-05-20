import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceivenContributionComponent } from './receiven-contribution.component';

describe('ReceivenContributionComponent', () => {
  let component: ReceivenContributionComponent;
  let fixture: ComponentFixture<ReceivenContributionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceivenContributionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReceivenContributionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
