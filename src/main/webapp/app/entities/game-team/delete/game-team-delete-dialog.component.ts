import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGameTeam } from '../game-team.model';
import { GameTeamService } from '../service/game-team.service';

@Component({
  templateUrl: './game-team-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GameTeamDeleteDialogComponent {
  gameTeam?: IGameTeam;

  protected gameTeamService = inject(GameTeamService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameTeamService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
