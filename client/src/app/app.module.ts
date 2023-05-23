import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthService } from './services/auth.service';
import { BlockchainService } from './services/blockchain.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FaucetComponent } from './components/faucet/faucet.component';
import { PrimeNgModule } from './primeng.module';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidenav/sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthComponents } from "./components/auth/auth.index"
import { httpInterceptorProviders } from './components/auth/auth.interceptor';
import { PrimeMessageService } from './services/prime.message.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { WalletService } from './services/wallet.service';
import { layoutComponents } from "./layout/layout.index";
import { projectAdminComponents } from "./components/projectAdmin/projectAdmin.index";
import { SqlRepositoryService as SqlRepoService } from './services/sql.repo.service';
import { TruncatePipe } from './util/truncatePipe';
import { NgxEditorImportsModule } from './ngxEditor.imports.module';
import { MongoRepoService } from './services/mongo.repo.service';
import { exploreComponents } from './components/explore/explore.index';
import { DatePipe } from '@angular/common';
import { ValidatorService } from "../app/services/validator.service";
import { CommaSeparatedPipe } from './util/commaPipe';

@NgModule({
  declarations: [
    AppComponent,
    FaucetComponent,
    HeaderComponent,
    SidebarComponent,
    ...layoutComponents,
    ...AuthComponents,
    ...projectAdminComponents,
    ...exploreComponents,
    TruncatePipe,
    CommaSeparatedPipe,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    PrimeNgModule,
    NgxEditorImportsModule
  ],
  providers: [
    httpInterceptorProviders,
    AuthService,
    BlockchainService,
    PrimeMessageService,
    MessageService,
    WalletService,
    ConfirmationService,
    SqlRepoService,
    MongoRepoService,
    DatePipe,
    ValidatorService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
