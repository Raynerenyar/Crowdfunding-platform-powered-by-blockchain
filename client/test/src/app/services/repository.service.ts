import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Url } from '../util/url.util';
import { constants } from 'src/environments/environment';
import { Observable, Subject, Subscription } from 'rxjs';
import { ProjectDetails } from '../model/model';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  projectAddressEvent = new Subject<string>()
  projectDetails = new Subject<ProjectDetails>()

  constructor(private http: HttpClient) { }

  // get list of projects with all the column data
  getProjects(address: string): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-projects-by-creator-address/")
      .add(address)
      .getUrl()
    return this.http.get(url, { responseType: 'json' })
  }

  getRequests(projectAddress: string): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-requests/")
      .add(projectAddress)
      .getUrl()
    return this.http.get(url, { responseType: 'json' })
  }

  getProjectsWithPage(offset: number, limit: number): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-projects")
      .getUrl()
    let params = new HttpParams()
      .append('offset', offset)
      .append('limit', limit)
    return this.http.get(url, { params })
  }

  getLatestProject(creatorAddress: string) {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-latest-project/")
      .add(creatorAddress)
      .getUrl()
    return this.http.get(url)

  }

  getCountProjects(): Observable<any> {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-count-projects")
      .getUrl()
    return this.http.get(url)
  }

  // get single project with all the column data
  getSingleProject(projectAddress: string) {
    let url = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-single-project/")
      .add(projectAddress)
      .getUrl()
    return this.http.get(url)
  }

  emitProjectAddress(projectAddress: string) {
    this.projectAddressEvent.next(projectAddress)
  }

  emitProjectDetails(project: ProjectDetails) {
    this.projectDetails.next(project)
  }

}
