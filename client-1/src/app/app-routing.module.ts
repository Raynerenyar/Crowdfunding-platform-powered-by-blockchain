import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from './layout/app.layout.component';
import { ProjectAdminComponent } from './components/projectAdmin/project-admin.component';

const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'project-admin', component: ProjectAdminComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
