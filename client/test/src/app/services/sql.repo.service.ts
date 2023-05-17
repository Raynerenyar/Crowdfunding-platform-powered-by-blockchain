import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Url } from '../util/url.util';
import { constants } from 'src/environments/environment';
import { Observable, Subject, Subscription } from 'rxjs';
import { Project, Request } from '../model/model';
import { UrlBuilderService } from './url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class SqlRepositoryService {

  projectAddressEvent = new Subject<string>()
  projectDetailsEvent = new Subject<Project>()
  project!: Project
  projects!: Project[]
  requests!: Request[]

  constructor(private http: HttpClient, private urlBuilder: UrlBuilderService) { }

  // get list of projects with all the column data
  getProjects(address: string): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-projects-by-creator-address/")
      .add(address)
      .getUrl()
    let url2 = this.urlBuilder
      .setPath('api/get-projects-by-creator-address/:address')
      .addPathVariable('address', address)
      .build()
    // console.log(url, url2)
    return this.http.get(url2)
  }

  getRequests(projectAddress: string): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-requests/")
      .add(projectAddress)
      .getUrl()
    let url2 = this.urlBuilder
      .setPath('api/get-requests/:projectAddress')
      .addPathVariable('projectAddress', projectAddress)
      .build()
    console.log(url, url2)
    return this.http.get(url2)
  }

  getProjectsWithPage(offset: number, limit: number): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-projects")
      .getUrl()
    let url2 = this.urlBuilder.setPath('api/get-projects').build()
    console.log(url, url2)
    let params = new HttpParams()
      .append('offset', offset)
      .append('limit', limit)
    return this.http.get(url2, { params })
  }

  getLatestProject(creatorAddress: string): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-latest-project/")
      .add(creatorAddress)
      .getUrl()
    let url2 = this.urlBuilder
      .setPath('api/get-latest-project/:creatorAddress')
      .addPathVariable('creatorAddress', creatorAddress)
      .build()
    console.log(url, url2)
    return this.http.get(url2)
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



  emitProjectAddress(projectAddress: string) {
    this.projectAddressEvent.next(projectAddress)
  }

  emitProjectDetails(project: Project) {
    console.log("emitting project Details")
    this.projectDetailsEvent.next(project)
  }

}
