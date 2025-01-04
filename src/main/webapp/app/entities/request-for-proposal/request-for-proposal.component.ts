import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-request-for-proposal',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './request-for-proposal.component.html',
  styleUrls: ['../../shared/table-scss-shared.scss', './request-for-proposal.component.scss'],
})
export class RequestForProposalComponent {
  constructor(private router: Router) {}

  proposal = {
    title: '',
    description: '',
  };
  selectedFile: File | null = null;
  formError: string | null = null;
  formSubmitted = false;

  navigateToResultatRfp2() {
    this.router.navigate(['/ResultatRFP']);
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onSubmit(proposalForm: any): void {
    this.formSubmitted = true;

    if (!this.proposal.title || !this.proposal.description || !this.selectedFile) {
      return;
    }

    this.formError = null;

    const formData = new FormData();
    formData.append('title', this.proposal.title);
    formData.append('description', this.proposal.description);

    if (this.selectedFile) {
      formData.append('file', this.selectedFile, this.selectedFile.name);
    }

    console.log('Proposal submitted:', this.proposal, this.selectedFile ? this.selectedFile.name : 'No file');
    this.navigateToResultatRfp2();
  }
}
