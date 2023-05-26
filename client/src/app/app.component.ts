import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { SessionStorageService } from './services/session.storage.service';
import { environment } from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username?: string;

  constructor(private primengConfig: PrimeNGConfig, private storageService: SessionStorageService) { }

  ngOnInit() {
    console.log(environment)
    // console.log(environment.apiUrl)
    this.primengConfig.overlayOptions;
    this.primengConfig.setTranslation({
      dateFormat: 'dd/mm/yy'
    });
    this.isLoggedIn = this.storageService.isLoggedIn();
    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    };
  }


  title = 'Crowdfunding';
}
