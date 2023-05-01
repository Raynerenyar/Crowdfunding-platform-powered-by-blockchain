import { Component, Input, OnInit, Output } from '@angular/core';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Subject } from 'rxjs';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-overlayPanel',
  templateUrl: './overlay.panel.component.html',
  styleUrls: ['./overlay.panel.component.css']
})
export class OverlayPanelComponent implements OnInit {

  isLoggedIn = false;

  @Output()
  onLoginEvent = new Subject<string>()

  constructor(private storageService: StorageService) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
    }
  }

  emitLoginEvent() {
    this.onLoginEvent.next("login")
  }
}
