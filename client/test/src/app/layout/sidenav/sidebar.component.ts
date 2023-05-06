import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { RepositoryService } from 'src/app/services/repository.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private storageSvc: StorageService) { }

  private roles: string[] = []
  sidebarVisible!: boolean;
  items!: MenuItem[]
  showUserBoard = false
  showAdminBoard = false;
  showModeratorBoard = false;
  isLoggedIn = false
  username!: string

  // menu items
  // homeMenu = { label: 'Home', icon: 'pi pi-home', routerLink: '/home', }
  exploreMenu = { label: 'Explore', icon: 'pi pi-globe', routerLink: '/explore' }
  dashboardMenu = { label: 'Project Dashboard', icon: 'pi pi-folder-open', routerLink: '/project-admin' }
  newProjMenu = { label: 'Start A Project', icon: 'pi pi-book', routerLink: '/project-admin/new-project' }
  newReqMenu = { label: 'New Requests', icon: 'pi pi-clone', routerLink: '/project-admin/new-request' }
  faucetMenu = { label: 'Faucet', icon: 'pi pi-plus-circle', routerLink: '/faucet' }
  aboutMenu = { label: 'About', icon: 'pi faucet-icon', routerLink: '/login/user' }

  ngOnInit(): void {
    if (this.storageSvc.isLoggedIn()) {
      this.isLoggedIn = true;

      const user = this.storageSvc.getUser();
      this.roles = user.roles;
      this.showUserBoard = this.roles.includes('ROLE_USER')
      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    } else {
      this.isLoggedIn = false
      this.showUserBoard = false
      this.showAdminBoard = false
      this.showModeratorBoard = false
    }

    console.log(this.showUserBoard)
    if (this.showUserBoard) {
      this.items = [
        // this.homeMenu,
        this.exploreMenu,
        this.dashboardMenu,
        this.newProjMenu,
        this.newReqMenu,
        this.faucetMenu,
        this.aboutMenu
      ]
    } else {
      this.items = [
        // this.homeMenu,
        this.exploreMenu,
        this.faucetMenu,
        this.aboutMenu
      ]
    }
    //   this.items = [
    //     {
    //       label: 'Home',
    //       icon: 'pi pi-home',
    //       routerLink: '/home',
    //       // items: [
    //       //   {
    //       //     label: 'sub',
    //       //     icon: 'pi pi-refresh',

    //       //   }
    //       // ]
    //     },
    //     {
    //       label: 'Explore',
    //       icon: 'pi pi-globe',
    //       routerLink: '/explore'
    //     },
    //     {
    //       label: 'Project Dashboard',
    //       icon: 'pi pi-folder-open',
    //       routerLink: '/project-admin'
    //     },
    //     {
    //       label: 'Start A Project',
    //       icon: 'pi pi-book',
    //       routerLink: '/project-admin/new-project',
    //     },
    //     {
    //       label: 'New Requests',
    //       icon: 'pi pi-clone',
    //       routerLink: '/project-admin/new-request'
    //     },
    //     {
    //       label: 'Faucet',
    //       icon: 'pi pi-plus-circle',
    //       routerLink: '/faucet'
    //     },
    //     {
    //       label: 'About',
    //       icon: 'pi faucet-icon',
    //       routerLink: '/login/user'
    //     }
    //   ]
    // } else {
    //   this.items = [
    //     {
    //       label: 'Home',
    //       icon: 'pi pi-home'
    //       // items: [
    //       //   {
    //       //     label: 'sub',
    //       //     icon: 'pi pi-refresh'
    //       //   }
    //       // ]
    //     },
    //     {
    //       label: 'Explore',
    //       icon: 'pi pi-globe',
    //       routerLink: '/explore'
    //     },
    //     {
    //       label: 'Faucet',
    //       icon: 'pi pi-plus-circle',
    //       routerLink: '/faucet'
    //     },
    //     {
    //       label: 'About',
    //       icon: 'pi faucet-icon',
    //       routerLink: '/login/user'
    //     }
    //   ]
    // }

  }
}
