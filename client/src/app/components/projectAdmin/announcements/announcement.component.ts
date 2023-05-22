import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Announcement } from 'src/app/model/model';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';

@Component({
  selector: 'app-announcement',
  templateUrl: './announcement.component.html',
  styleUrls: ['./announcement.component.css']
})
export class AnnouncementComponent implements OnInit {

  announcements!: Announcement[]
  announcement!: Announcement
  selectedIndex!: number
  notifier$ = new Subject<boolean>()
  projectAddress!: string | null
  first = 0 // first index
  rows = 10 // number of results to show
  length = 0 // total length of results.

  constructor(private mongoSvc: MongoRepoService, private route: ActivatedRoute, private router: Router, private msgSvc: PrimeMessageService) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((params: ParamMap) => {
        this.projectAddress = params.get('address')
        if (this.projectAddress) {
          this.getAnnouncements(this.projectAddress, this.first, this.rows)
          this.getCountAnnouncements()
        }
      })
  }

  onEdit() {
    let timePosted = new Date(this.announcements[this.selectedIndex].datetimePosted).getTime()
    let timeNow = new Date().getTime()
    if ((timeNow - timePosted) < 3600_000) {
      if (this.selectedIndex != undefined) {
        this.router.navigateByUrl(`/project-admin/${this.projectAddress}/edit-announcement/${this.selectedIndex}`)
        return
      }
      this.msgSvc.detailedInfoMethod("Info", "Please select one announcement to edit")
      return
    }
    this.msgSvc.generalInfoMethod("You have passed the time allowed to edit")
    return
  }

  goBack() {
    this.router.navigate(['project-admin', this.projectAddress], { replaceUrl: false });
  }

  newAnnouncement() {
    this.router.navigate(['project-admin', this.projectAddress, 'new-announcement']);
  }

  onChosenAnnouncement(index: number) {
    this.announcement = this.announcements[index]
    this.selectedIndex = index
  }

  editable(announce: Announcement): boolean {
    let datetime = announce.datetimePosted
    let timeDiff = new Date().getTime() - new Date(datetime).getTime()
    // after 1 hour cannot edit announcement
    if (timeDiff >= 3_600_000) {
      return true
    }
    return false
  }

  getAnnouncements(projectAddress: string, offset: number, limit: number) {
    this.mongoSvc.getAnnouncementsByPage(projectAddress, offset, limit)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (announcements: Announcement[]) => {
          this.announcements = announcements

          // assign new first and rows value
          this.first = offset
          this.rows = limit
        }
      });
  }

  getCountAnnouncements() {
    this.mongoSvc.countAnnouncements(this.projectAddress!)
      .pipe(takeUntil(this.notifier$))
      .subscribe((count) => {
        this.length = count
      })
  }

  onPageNumChange(event: { first: number; rows: number; }) {
    console.log(event.first, event.rows)
    this.getAnnouncements(this.projectAddress!, event.first, event.rows)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
