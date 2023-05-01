import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  sidebarVisible!: boolean;
  items!: MenuItem[]

  ngOnInit(): void {
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
        label: 'Project',
        icon: 'pi pi-book',
        routerLink: '/project-admin'
      },
      {
        label: 'Requests',
        icon: 'pi pi-clone'


      },
      {
        label: 'Faucet',
        icon: 'pi pi-plus-circle faucet-icon'
      }
    ]
  }
}
