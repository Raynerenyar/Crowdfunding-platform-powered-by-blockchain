import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
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

  @Input()
  projectAddress!: string
  notifier$ = new Subject<boolean>()
  announcements!: Announcement[]
  constructor(private mongoSvc: MongoRepoService, private walletSvc: WalletService) { }

  ngOnInit(): void {
    console.log(this.projectAddress)
    this.mongoSvc.getAnnouncements(this.projectAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (announcements: Announcement[]) => {
          this.announcements = announcements
          console.log(this.announcements)
        },
        error: (error: HttpErrorResponse) => { }
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
