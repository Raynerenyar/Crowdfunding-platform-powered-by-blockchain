import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProjectComponent } from './components/project/project.component';
import { HomeComponent } from './auth/components/home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { ProjectAdminComponent } from './components/projectAdmin/project-admin.component';
import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
import { FaucetComponent } from './components/faucet/faucet.component';
import { LoginComponent } from './auth/components/login/login.component';
import { RegisterComponent } from './auth/components/register/register.component';
import { ProfileComponent } from './auth/components/profile/profile.component';
import { BoardUserComponent } from './auth/components/board-user/board-user.component';
import { BoardModeratorComponent } from './auth/components/board-moderator/board-moderator.component';
import { BoardAdminComponent } from './auth/components/board-admin/board-admin.component';


const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'project-admin', component: ProjectAdminComponent, },
      { path: 'project-admin/new-request', component: NewRequestComponent },
      { path: 'faucet', component: FaucetComponent },
      { path: 'home', component: HomeComponent, },
      { path: 'profile', component: ProfileComponent },
      { path: 'mod', component: BoardModeratorComponent },
      { path: 'admin', component: BoardAdminComponent },
      { path: '**', redirectTo: '', pathMatch: 'full' },
    ]
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
// { useHash: true }
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
