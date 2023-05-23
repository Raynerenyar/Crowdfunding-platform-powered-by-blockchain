import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Announcement } from 'src/app/model/model';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-view-announcement',
  templateUrl: './view-announcement.component.html',
  styleUrls: ['./view-announcement.component.css']
})
export class ViewAnnouncementComponent implements OnInit {

  projectAddress!: string
  notifier$ = new Subject<boolean>()
  announcements!: Announcement[]
  first = 0
  rows = 10
  length = 0

  constructor(private mongoSvc: MongoRepoService, private walletSvc: WalletService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {

    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        this.projectAddress = param.get('projectAddress')!

        console.log(this.projectAddress)
        this.getAnnouncements(this.projectAddress, this.first, this.rows)
        this.getCountAnnouncements()
      })

  }

  goBack() {
    this.router.navigate(['explore', this.projectAddress], { replaceUrl: false })
  }

  onPageNumChange(event: Event) {

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

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
