import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { environment } from '../environments/environment';
import { provideAuth, getAuth } from '@angular/fire/auth';
import { provideFirestore, getFirestore } from '@angular/fire/firestore';
// import { AuthService } from './services/auth.service';
// import { BlockchainService } from './services/blockchain.service';
// import { ProjectComponent } from './components/project/project.component';
// import { ProjectAdminComponent } from './components/projectAdmin/project-admin.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { FaucetComponent } from './components/faucet/faucet.component';
import { PrimeNgModule } from './primeng.module';
// import { DashboardComponent } from './layout/dashboard/dashboard.component';
// import { HeaderComponent } from './layout/header/header.component';
// import { HomeComponent } from './layout/home/home.component';
import { SidebarComponent } from './layout/sidenav/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './layout/header/header.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { ProjectAdminComponent } from './components/projectAdmin/project-admin.component';
// import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
// import { ReceivenContributionComponent } from './components/projectAdmin/receiveContribution/receiven-contribution.component';
// import { AppLayoutComponent } from './layout/app.layout.component';

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    HeaderComponent,
    AppLayoutComponent,
    ProjectAdminComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeNgModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
