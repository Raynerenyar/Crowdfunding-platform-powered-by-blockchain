import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { Sidebar } from 'primeng/sidebar';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private storageSvc: SessionStorageService, private router: Router) { }

  private roles: string[] = []
  sidebarVisible!: boolean;
  items!: MenuItem[]
  showUserBoard = false
  // showAdminBoard = false;
  // showModeratorBoard = false;
  isLoggedIn = false
  username!: string

  @ViewChild('sb')
  sb!: Sidebar


  // menu items
  // homeMenu = { label: 'Home', icon: 'pi pi-home', routerLink: '/home', }
  exploreMenu = { label: 'Explore', icon: 'pi pi-globe', routerLink: '/explore' }
  dashboardMenu = { label: 'Project Dashboard', icon: 'pi pi-folder-open', routerLink: '/project-admin' }
  newProjMenu = { label: 'Start A Project', icon: 'pi pi-book', routerLink: '/project-admin/new-project' }
  newReqMenu = { label: 'New Requests', icon: 'pi pi-clone', routerLink: '/project-admin/new-request' }
  faucetMenu = { label: 'Faucet', icon: 'pi pi-plus-circle', routerLink: '/faucet' }
  // aboutMenu = { label: 'About', icon: 'pi faucet-icon', routerLink: '/login/user' }

  ngOnInit(): void {
    if (this.storageSvc.isLoggedIn()) {
      const user = this.storageSvc.getUser()
      this.roles = user.roles;
      this.isLoggedIn = this.roles.includes('ROLE_USER')
      // this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      // this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    } else {
      this.isLoggedIn = false
      this.showUserBoard = false
      // this.showAdminBoard = false
      // this.showModeratorBoard = false
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
        this.faucetMenu, this.newProjMenu,
        // this.newReqMenu,
        // this.aboutMenu
      ]
    } else {
      this.items = [
        // this.homeMenu,
        this.exploreMenu,
        this.faucetMenu,
        // this.aboutMenu
      ]
    }
  }

  goExplore() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    // this.sidebarVisible = false
    this.router.navigate(['explore'])
  }

  goLogin() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    this.router.navigate(['login'])
  }

  goDashboard() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    this.router.navigate(['project-admin'])
  }

  goNewProject() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    this.router.navigate(['project-admin', 'new-project'])
  }

  goFaucet() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    this.router.navigate(['faucet'])
  }

  goAbout() {
    this.sb.hide()
    this.sb.visibleChange.emit(false)
    this.router.navigate(['about'])
  }
}
