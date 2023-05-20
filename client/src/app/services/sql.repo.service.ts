import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project, Request } from '../model/model';
import { UrlBuilderService } from './url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class SqlRepositoryService {

  project!: Project
  projects!: Project[]
  requests!: Request[]

  constructor(private http: HttpClient, private urlBuilder: UrlBuilderService) { }

  // get list of projects with all the column data
  getProjects(address: string): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-projects-by-creator-address/:address')
      .addPathVariable('address', address)
      .build()
    return this.http.get(url)
  }

  getRequests(projectAddress: string): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-requests/:projectAddress')
      .addPathVariable('projectAddress', projectAddress)
      .build()
    return this.http.get(url)
  }

  getProjectsWithPage(offset: number, limit: number): Observable<any> {
    let url = this.urlBuilder.setPath('api/get-projects').build()
    let params = new HttpParams()
      .append('offset', offset)
      .append('limit', limit)
    return this.http.get(url, { params })
  }

  getLatestProject(creatorAddress: string): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-latest-project/:creatorAddress')
      .addPathVariable('creatorAddress', creatorAddress)
      .build()
    return this.http.get(url)
  }

  getCountProjects(): Observable<any> {

    let url = this.urlBuilder
      .setPath('api/get-count-projects')
      .build()
    return this.http.get(url)
  }

  // get single project with all the column data
  getSingleProject(projectAddress: string): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-single-project/:projectAddress')
      .addPathVariable('projectAddress', projectAddress)
      .build()
    return this.http.get(url)
  }

  getSingleRequest(requestId: number): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-single-request/:requestId')
      .addPathVariable('requestId', requestId.toString())
      .build()
    return this.http.get(url)
  }

  getValueOfVotes(projectAddress: string, requestNum: number): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-value-of-votes/:projectAddress/:requestNo')
      .addPathVariable('projectAddress', projectAddress)
      .addPathVariable('requestNo', requestNum.toString())
      .build()
    return this.http.get(url)
  }

  getCountOfVotes(projectAddress: string, requestNum: number): Observable<any> {
    let url = this.urlBuilder
      .setPath('api/get-count-of-votes/:projectAddress/:requestNo')
      .addPathVariable('projectAddress', projectAddress)
      .addPathVariable('requestNo', requestNum.toString())
      .build()
    return this.http.get(url)
  }

  getTokens(): Observable<any> {
    let url = this.urlBuilder.setPath('api/get-tokens').build()
    return this.http.get(url)
  }
}
