import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UrlBuilderService } from './url-builder.service';
import { Announcement } from "../model/model";
import { Observable } from 'rxjs';
import { NewComment } from "../model/model";

@Injectable({
  providedIn: 'root'
})
export class MongoRepoService {

  constructor(private http: HttpClient, private urlBuilder: UrlBuilderService) { }

  public insertAnnouncement(announcement: Announcement): Observable<any> {
    let url = this.urlBuilder.setPath('api/insert-announcement').build()
    console.log(url)
    return this.http.post(url, announcement)
  }

  public editAnnouncement(announcement: Announcement): Observable<any> {
    let url = this.urlBuilder.setPath('api/edit-announcement').build()
    return this.http.put(url, announcement)
  }

  public getAnnouncements(projectAddress: string): Observable<any> {
    let url = this.urlBuilder.setPath('api/get-announcements').build()
    let params = new HttpParams().append('projectAddress', projectAddress)
    return this.http.get(url, { params })
  }

  public getComments(projectAddress: string): Observable<any> {
    let url = this.urlBuilder.setPath('api/get-comments').build()
    let params = new HttpParams().append('projectAddress', projectAddress)
    return this.http.get(url, { params })
  }

  public insertComment(comment: NewComment): Observable<any> {
    let url = this.urlBuilder.setPath('api/insert-comment').build()
    return this.http.post(url, comment)
  }


}
