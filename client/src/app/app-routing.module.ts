import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from './layout/app.layout.component';
import { ProjectDashboardComponent } from './components/projectAdmin/project-dashboard/project-dashboard.component';
import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
import { FaucetComponent } from './components/faucet/faucet.component';
import { NewProjectComponent } from './components/projectAdmin/new-project/new-project.component';
import { ProjectOverviewComponent } from './components/projectAdmin/project-overview/project-overview.component';
import { ExploreProjectsComponent } from './components/explore/explore-projects/explore-projects.component';
import { ProjectMainComponent } from './components/explore/project/project-main.component';
import { RequestDetailsComponent } from './components/projectAdmin/request-details/request-details.component';
import { AnnouncementEditorComponent } from './components/projectAdmin/new-announcement/new-announcement.component';
import { ProjectCommentsComponent } from './components/projectAdmin/project-comments/project-comments.component';
import { authenticationGuard } from './services/auth-guard.service';
import { AnnouncementComponent } from './components/projectAdmin/announcements/announcement.component';
import { ViewAnnouncementComponent } from './components/explore/viewAnnouncements/view-announcement.component';
import { RequestComponent } from './components/explore/viewRequest/request/request.component';
import { CommentsComponent } from './components/explore/comments/view-comments/comments.component';
import { NewCommentComponent } from './components/explore/comments/new-comment/new-comment.component';
import { LoginPageComponent } from './components/auth/components/login-page/login-page.component';
import { AboutComponent } from './components/about/about.component';
import { ForgetComponent } from './components/auth/components/forget/forget.component';


const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'project-admin', component: ProjectDashboardComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/new-project', component: NewProjectComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address', component: ProjectOverviewComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/new-request', component: NewRequestComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/new-announcement', component: AnnouncementEditorComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/comments', component: ProjectCommentsComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/announcements', component: AnnouncementComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/edit-announcement/:edit', component: AnnouncementEditorComponent, canActivate: [authenticationGuard()] },
      { path: 'project-admin/:address/request/:requestId', component: RequestDetailsComponent, canActivate: [authenticationGuard()] },
      { path: 'explore', component: ExploreProjectsComponent },
      { path: 'explore/:projectAddress', component: ProjectMainComponent },
      { path: 'explore/:projectAddress/announcements', component: ViewAnnouncementComponent },
      { path: 'explore/:projectAddress/request/:requestId', component: RequestComponent },
      { path: 'explore/:projectAddress/comments', component: CommentsComponent },
      { path: 'explore/:projectAddress/comments/new-comment', component: NewCommentComponent },
      { path: 'login', component: LoginPageComponent },
      { path: 'faucet', component: FaucetComponent },
      { path: 'about', component: AboutComponent },
      { path: 'forget', component: ForgetComponent },
      { path: '**', redirectTo: 'explore', pathMatch: 'full' },
    ]
  },
];
// { useHash: true }
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
