import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  apiHost: string = 'http://localhost:8080/';
  headers: HttpHeaders = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient) { }

  simpleSearch(searchTerm: object) {
    return this.http.post(this.apiHost + 'api/search/simple', searchTerm);
  }

  advancedSearch(searchTerm: object) {
    return this.http.post(this.apiHost + 'api/search/advanced', searchTerm);
  }

  phraseSearch(searchTerm: string) {
    return this.http.post(this.apiHost + 'api/search/phrase', searchTerm);
  }

  geolocationSearch(address: string, radius: number) {
    const geolocationQueryDTO = {
      address: address,
      radius: radius,
    };
    return this.http.post(this.apiHost + 'api/search/geolocation', geolocationQueryDTO);
  }

  download(serverFilename: string) {
    return this.http.get(this.apiHost + 'api/file/' + serverFilename, {responseType: 'blob'});
  }

}
