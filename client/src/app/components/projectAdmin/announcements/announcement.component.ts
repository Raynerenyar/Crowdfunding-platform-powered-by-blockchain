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
  tooltip = "It has passed one hour."

  constructor(private mongoSvc: MongoRepoService, private route: ActivatedRoute, private router: Router, private msgSvc: PrimeMessageService) { }

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
                if (announcements) {
                  this.announcements = announcements.reverse()
                  this.mongoSvc.announcements = this.announcements
                }
              },
              error: (error: HttpErrorResponse) => { }
            })
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

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
