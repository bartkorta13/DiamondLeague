import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPlayer } from '../player.model';

@Component({
  selector: 'jhi-player-detail',
  templateUrl: './player-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PlayerDetailComponent {
  player = input<IPlayer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
