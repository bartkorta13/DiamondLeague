import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';
import { StadiumFormGroup, StadiumFormService } from './stadium-form.service';

@Component({
  selector: 'jhi-stadium-update',
  templateUrl: './stadium-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StadiumUpdateComponent implements OnInit {
  isSaving = false;
  stadium: IStadium | null = null;

  protected stadiumService = inject(StadiumService);
  protected stadiumFormService = inject(StadiumFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StadiumFormGroup = this.stadiumFormService.createStadiumFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stadium }) => {
      this.stadium = stadium;
      if (stadium) {
        this.updateForm(stadium);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stadium = this.stadiumFormService.getStadium(this.editForm);
    if (stadium.id !== null) {
      this.subscribeToSaveResponse(this.stadiumService.update(stadium));
    } else {
      this.subscribeToSaveResponse(this.stadiumService.create(stadium));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStadium>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(stadium: IStadium): void {
    this.stadium = stadium;
    this.stadiumFormService.resetForm(this.editForm, stadium);
  }
}
