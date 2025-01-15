import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IStadium } from '../stadium.model';

@Component({
  selector: 'jhi-stadium-detail',
  templateUrl: './stadium-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class StadiumDetailComponent {
  stadium = input<IStadium | null>(null);

  previousState(): void {
    window.history.back();
  }
}
