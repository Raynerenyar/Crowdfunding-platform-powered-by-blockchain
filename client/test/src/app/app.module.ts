import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { environment } from '../environments/environment';
import { provideAuth, getAuth } from '@angular/fire/auth';
import { provideFirestore, getFirestore } from '@angular/fire/firestore';
import { AuthService } from './services/auth.service';
import { BlockchainService } from './services/blockchain.service';
import { ProjectHeaderComponent } from './components/explore/project/projectHeader/project-header.component';
import { ProjectDashboardComponent } from './components/projectAdmin/project-dashboard/project-dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FaucetComponent } from './components/faucet/faucet.component';
import { PrimeNgModule } from './primeng.module';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidenav/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
import { ReceivenContributionComponent } from './components/projectAdmin/receiveContribution/receiven-contribution.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { AuthComponents } from "./components/auth/auth.index"
import { httpInterceptorProviders } from './components/auth/auth.interceptor';
import { AlertMessageService } from './services/alert.message.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { WalletService } from './services/wallet.service';
import { layoutComponents } from "./layout/layout.index";
import { projectAdminComponents } from "./components/projectAdmin/projectAdmin.index";
import { RepositoryService } from './services/repository.service';
import { ExploreProjectsComponent } from './components/explore/explore-projects/explore-projects.component';
import { RequestComponent } from './components/explore/viewRequest/request.component';
import { ContributeRequestComponent } from './components/explore/viewRequest/contribute-request/contribute-request.component';
import { ProjectBodyComponent } from './components/explore/project/project-body/project-body.component';
import { ProjectMainComponent } from './components/explore/project/project-main/project-main.component';
import { DexieDBService } from './services/dexie-db.service';
import { TruncatePipe } from './util/truncatePipe';

@NgModule({
  declarations: [
    AppComponent,
    FaucetComponent,
    HeaderComponent,
    SidebarComponent,
    ExploreProjectsComponent,
    RequestComponent,
    ContributeRequestComponent,
    ProjectMainComponent,
    ProjectHeaderComponent,
    ProjectBodyComponent,
    // NewRequestComponent,
    // ReceivenContributionComponent,
    AppLayoutComponent,
    ...layoutComponents,
    ...AuthComponents,
    ...projectAdminComponents,
    TruncatePipe,

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeNgModule,
    provideFirebaseApp(() => initializeApp(environment.firebase)),
    provideAuth(() => getAuth()),
    provideFirestore(() => getFirestore())
  ],
  providers: [
    AuthService,
    BlockchainService,
    httpInterceptorProviders,
    AlertMessageService,
    MessageService,
    WalletService,
    ConfirmationService,
    RepositoryService,
    DexieDBService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
