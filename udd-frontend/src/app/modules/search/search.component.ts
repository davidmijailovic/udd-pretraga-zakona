import { Component } from '@angular/core';
import { SearchService } from 'src/app/services/search/search.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  searchTerm: string = '';
  isAdvanced: boolean = false;
  searchTermsList: string[] = [];
  responseItems: any[] = [];
  phrase: string = '';
  address: string = '';
  radius!: number;

  constructor(private searchService: SearchService) { }

  search(){
    if(this.searchTerm.startsWith('"') && this.searchTerm.endsWith('"')) 
      this.phraseSearch(this.searchTerm);

    this.searchTermsList = this.searchTerm.split(' ');
    const searchTerms = {keywords: this.searchTermsList}

    if (this.isAdvanced) {
      this.advancedSearch(searchTerms);
    } else {
      this.simpleSearch(searchTerms);
    }
  }

  searchGeolocation(){
    this.searchService.geolocationSearch(this.address,this.radius).subscribe({
      next: (res: any) => {
        console.log(res);
        this.responseItems = res.content;
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }


  private simpleSearch(searchTerms: { keywords: string[]; }) {
    this.searchService.simpleSearch(searchTerms).subscribe({
      next: (res: any) => {
        console.log(res);
        this.responseItems = res.content;
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  private advancedSearch(searchTerms: { keywords: string[]; }) {
    this.searchService.advancedSearch(searchTerms).subscribe({
      next: (res: any) => {
        console.log(res);
        this.responseItems = res.content;
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  private phraseSearch(searchTerm: string) {
    this.searchService.phraseSearch(this.searchTerm.replaceAll('"', "")).subscribe({
      next: (res: any) => {
        this.responseItems = res.content;
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  toggleAdvanced(value: boolean) {
    this.isAdvanced = value;
  }

  download(serverFilename: string) {
    this.searchService.download(serverFilename).subscribe({
      next: (res: any) => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        FileSaver.saveAs(blob, serverFilename);
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }
}
