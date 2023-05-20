import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ScrollPanel } from 'primeng/scrollpanel';

@Component({
  selector: 'app-request-list',
  templateUrl: './request.list.component.html',
  styleUrls: ['./request.list.component.css']
})
export class RequestListComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input()
  requests!: Request[]
  @Input()
  project!: Project
  @Input()
  tokenAddress!: string
  @Input()
  projectAddress!: string

  userAddress!: string
  tokenBalance!: number
  selectedRequestIndex!: number
  request!: Request

  notifier$ = new Subject<boolean>()

  @ViewChild('sp')
  scrollPanel!: ScrollPanel
  scrollPanelHeight = 800 // in px

  constructor(private route: ActivatedRoute, private router: Router, private repoSvc: SqlRepositoryService, private msgSvc: PrimeMessageService, private storageSvc: SessionStorageService, private bcSvc: BlockchainService, private renderer: Renderer2) { }

  ngOnInit(): void {
    console.log("request loaded")
    this.userAddress = this.storageSvc.getAddress()
    console.log(this.tokenAddress)

    // get token balance if there are requests
    if (this.requests) {
      this.bcSvc.getBalanceOf(this.userAddress, this.tokenAddress)
        .then((balance) => this.tokenBalance = balance)
        .catch()
    }
  }

  ngAfterViewInit(): void {
    if (this.scrollPanel)
      this.renderer.setStyle(this.scrollPanel, 'height', `${this.scrollPanelHeight}px`)
  }


  onChosenRequest(index: number) {
    this.request = this.requests[index]
    this.selectedRequestIndex = index
  }

  onContributeRequestInit(height: number) {
    // if (height > this.scrollPanelHeight) this.renderer.setStyle(this.scrollPanel, 'height', `${height}px`)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
