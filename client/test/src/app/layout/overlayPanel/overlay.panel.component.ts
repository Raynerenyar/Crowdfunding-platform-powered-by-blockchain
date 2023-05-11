import { Component, Input, OnInit, Output } from '@angular/core';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Subject } from 'rxjs';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-overlayPanel',
  templateUrl: './overlay.panel.component.html',
  styleUrls: ['./overlay.panel.component.css']
})
export class OverlayPanelComponent implements OnInit {

  isLoggedIn = false;

  @Output()
  onLoginEvent = new Subject<string>()

  constructor(private storageService: SessionStorageService) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true
    } else this.isLoggedIn = false
  }

  emitLoginEvent() {
    this.onLoginEvent.next("login")
  }
}
