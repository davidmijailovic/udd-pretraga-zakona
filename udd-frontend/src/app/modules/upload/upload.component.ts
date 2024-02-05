import { Component } from '@angular/core';
import { UploadService } from 'src/app/services/upload/upload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  selectedFile: File | null = null;

  constructor(private uploadService: UploadService) { }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] as File;
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.uploadService.uploadFile(this.selectedFile).subscribe({
        next: (res: any) => {
          window.alert("Fajl je uspešno otpremljen !")
          console.log("File is uploaded" + this.selectedFile);
          console.log(res);
        },
        error: (err: any) => {
          console.log(err);
        }
      })
    } else {
      console.warn('No file selected.');
    }
  }

}
