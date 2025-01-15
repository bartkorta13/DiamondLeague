import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IGameTeam } from '../game-team.model';

@Component({
  selector: 'jhi-game-team-detail',
  templateUrl: './game-team-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class GameTeamDetailComponent {
  gameTeam = input<IGameTeam | null>(null);

  previousState(): void {
    window.history.back();
  }
}
