import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProjectHeaderComponent } from './components/explore/project/projectHeader/project-header.component';
import { HomeComponent } from './components/auth/components/home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { ProjectDashboardComponent } from './components/projectAdmin/project-dashboard/project-dashboard.component';
import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
import { FaucetComponent } from './components/faucet/faucet.component';
import { LoginComponent } from './components/auth/components/login/login.component';
import { RegisterComponent } from './components/auth/components/register/register.component';
import { ProfileComponent } from './components/auth/components/profile/profile.component';
import { BoardUserComponent } from './components/auth/components/board-user/board-user.component';
import { BoardModeratorComponent } from './components/auth/components/board-moderator/board-moderator.component';
import { BoardAdminComponent } from './components/auth/components/board-admin/board-admin.component';
import { NewProjectComponent } from './components/projectAdmin/new-project/new-project.component';
import { ProjectOverviewComponent } from './components/projectAdmin/project-overview/project-overview.component';
import { ExploreProjectsComponent } from './components/explore/explore-projects/explore-projects.component';
import { RequestComponent } from './components/explore/viewRequest/request.component';
import { ProjectMainComponent } from './components/explore/project/project-main/project-main.component';
import { ProjectBodyComponent } from './components/explore/project/project-body/project-body.component';
import { RequestDetailsComponent } from './components/projectAdmin/request-details/request-details.component';
import { AnnouncementEditorComponent } from './components/projectAdmin/new-announcement/new-announcement.component';
import { ProjectCommentsComponent } from './components/projectAdmin/project-comments/project-comments.component';
import { authenticationGuard } from './services/auth-guard.service';
import { AnnouncementComponent } from './components/projectAdmin/announcements/announcement.component';


const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      {
        path: 'project-admin', component: ProjectDashboardComponent, canActivate: [authenticationGuard()],
        children: [
          { path: 'new-project', component: NewProjectComponent },
          { path: ':address', component: ProjectOverviewComponent },
          { path: ':address/new-request', component: NewRequestComponent },
          { path: ':address/new-announcement', component: AnnouncementEditorComponent },
          { path: ':address/comments', component: ProjectCommentsComponent },
          { path: ':address/announcements', component: AnnouncementComponent },
          { path: ':address/edit-announcement/:edit', component: AnnouncementEditorComponent },
          { path: ':address/:requestId', component: RequestDetailsComponent },

        ]
      },
      { path: 'explore', component: ExploreProjectsComponent },
      {
        path: 'explore/:projectAddress', component: ProjectMainComponent,
        children: [
          { path: 'request/:requestNum', component: RequestComponent },
        ]
      },
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
