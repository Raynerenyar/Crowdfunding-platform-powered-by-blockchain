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
  constructor(private mongoSvc: MongoRepoService, private walletSvc: WalletService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {

    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        this.projectAddress = param.get('projectAddress')!

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
      })

  }

  goBack() {
    this.router.navigate(['explore', this.projectAddress], { replaceUrl: false })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
