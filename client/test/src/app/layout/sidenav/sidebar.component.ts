import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private storageService: StorageService) { }

  private roles: string[] = []
  sidebarVisible!: boolean;
  items!: MenuItem[]
  showUserBoard = false
  showAdminBoard = false;
  showModeratorBoard = false;
  isLoggedIn = false
  username!: string

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;

      const user = this.storageService.getUser();
      this.roles = user.roles;
      this.showUserBoard = this.roles.includes('ROLE_USER')
      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    } else this.isLoggedIn = false;

    console.log(this.showUserBoard)
    if (this.showUserBoard) {
      this.items = [
        {
          label: 'Home',
          icon: 'pi pi-home',
          routerLink: '/home'
          // items: [
          //   {
          //     label: 'sub',
          //     icon: 'pi pi-refresh'
          //   }
          // ]
        },
        {
          label: 'Explore',
          icon: 'pi pi-globe',
        },
        {
          label: 'Your Projects',
          icon: 'pi pi-folder-open'
        },
        {
          label: 'Start A Project',
          icon: 'pi pi-book',
          routerLink: '/project-admin',
        },
        {
          label: 'New Requests',
          icon: 'pi pi-clone',
          routerLink: '/project-admin/new-request'
        },
        {
          label: 'Faucet',
          icon: 'pi pi-plus-circle',
          routerLink: '/faucet'
        },
        {
          label: 'About',
          icon: 'pi pi-plus-circle',
          routerLink: '/login/user'
        }
      ]
    } else {
      this.items = [
        {
          label: 'Home',
          icon: 'pi pi-home'
          // items: [
          //   {
          //     label: 'sub',
          //     icon: 'pi pi-refresh'
          //   }
          // ]
        },
        {
          label: 'Explore',
          icon: 'pi pi-globe',
        },
        {
          label: 'Faucet',
          icon: 'pi pi-plus-circle faucet-icon',
          routerLink: '/faucet'
        },
        {
          label: 'About',
          icon: 'pi pi-plus-circle',
          routerLink: '/login/user'
        }
      ]
    }
  }



}
