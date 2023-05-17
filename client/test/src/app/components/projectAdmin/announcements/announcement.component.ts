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
  announcement!: Announcement
  selectedIndex!: number
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
                console.log(announcements)
                this.announcements = announcements.reverse()
                this.mongoSvc.announcements = this.announcements
              },
              error: (error: HttpErrorResponse) => { }
            })
        }
      })
  }

  onEdit() {
    console.log(this.selectedIndex)
    if (this.selectedIndex != undefined)
      this.router.navigateByUrl(`/project-admin/${this.projectAddress}/edit-announcement/${this.selectedIndex}`)
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

}
