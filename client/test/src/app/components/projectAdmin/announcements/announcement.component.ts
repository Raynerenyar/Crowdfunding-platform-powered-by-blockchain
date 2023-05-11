import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Announcement } from 'src/app/model/model';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';

@Component({
  selector: 'app-announcement',
  templateUrl: './announcement.component.html',
  styleUrls: ['./announcement.component.css']
})
export class AnnouncementComponent implements OnInit {

  announcements!: Announcement[]
  notifier$ = new Subject<boolean>()
  projectAddress!: string | null
  tooltip = "It has passed one hour."

  constructor(private mongoSvc: MongoRepoService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((params: ParamMap) => {
        this.projectAddress = params.get('address')
        if (this.projectAddress) {
          this.mongoSvc.getAnnouncements(this.projectAddress)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (announcements: Announcement[]) => {
                this.announcements = announcements
                this.mongoSvc.announcements = announcements
              },
              error: (error: HttpErrorResponse) => { }
            })
        }
      })
  }

  onEdit(index: number) {
    this.router.navigateByUrl(`/project-admin/${this.projectAddress}/edit-announcement/${index}`)
  }

  editable(announce: Announcement): boolean {
    let datetime = announce.datetimePosted
    let timeDiff = new Date().getTime() - new Date(datetime).getTime()
    console.log(timeDiff)
    if (timeDiff >= 3_600_000) {
      return true
    }
    return false
  }

}
