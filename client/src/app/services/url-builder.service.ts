import { Injectable } from '@angular/core';
import { constants } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UrlBuilderService {
  private baseUrl = constants.SERVER_URL;
  private path = '';

  constructor() { }

  /**  sets path `api/something` or`api/:variable`
    * first path variable does not need`/` as prefix
    * @param `api/something` or`api/:variable`
    * @returns `this`
  **/
  setPath(path: string): UrlBuilderService {
    this.path = path;
    return this;
  }

  addPathVariable(key: string, value: string): UrlBuilderService {
    this.path = this.path.replace(`:${key}`, value);
    return this;
  }

  build(): string {
    return `${this.baseUrl}/${this.path}`;
  }
}
