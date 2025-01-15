import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';

@Component({
  templateUrl: './stadium-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StadiumDeleteDialogComponent {
  stadium?: IStadium;

  protected stadiumService = inject(StadiumService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stadiumService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
