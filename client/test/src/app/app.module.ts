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
import { ProjectComponent } from './components/project/project.component';
import { ProjectAdminComponent } from './components/projectAdmin/project-admin.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FaucetComponent } from './components/faucet/faucet.component';
import { PrimeNgModule } from './primeng.module';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidenav/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NewRequestComponent } from './components/projectAdmin/newRequest/new-request.component';
import { ReceivenContributionComponent } from './components/projectAdmin/receiveContribution/receiven-contribution.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { AuthComponents } from "./auth/auth.index"
import { httpInterceptorProviders } from './auth/auth.interceptor';
import { alertMessageService } from './services/alert.message.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { WalletService } from './services/wallet.service';
import { layoutComponents } from "./layout/layout.index";

@NgModule({
  declarations: [
    AppComponent,
    ProjectComponent,
    ProjectAdminComponent,
    FaucetComponent,
    HeaderComponent,
    SidebarComponent,
    NewRequestComponent,
    ReceivenContributionComponent,
    AppLayoutComponent,
    ...layoutComponents,
    ...AuthComponents,

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
  providers: [AuthService, BlockchainService, httpInterceptorProviders, alertMessageService, MessageService, WalletService, ConfirmationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
