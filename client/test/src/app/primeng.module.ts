import { NgModule } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SidebarModule } from 'primeng/sidebar';
import { PanelMenuModule } from 'primeng/panelmenu';
import { MenuModule } from 'primeng/menu';
import { CalendarModule } from 'primeng/calendar';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { ToolbarModule } from 'primeng/toolbar';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { TabViewModule } from 'primeng/tabview';
import { PasswordModule } from 'primeng/password';
import { DividerModule } from 'primeng/divider';
import { FieldsetModule } from 'primeng/fieldset';
import { ConfirmPopupModule } from 'primeng/confirmpopup';

const PrimeNgModules = [
    ButtonModule,
    SidebarModule,
    PanelMenuModule,
    MenuModule,
    CalendarModule,
    InputTextareaModule,
    InputNumberModule,
    InputTextModule,
    PanelModule,
    ToolbarModule,
    OverlayPanelModule,
    // MessagesModule,
    // MessageModule,
    // PasswordModule,
    ToastModule,
    TabViewModule,
    DividerModule,
    FieldsetModule,
    ConfirmPopupModule
]

@NgModule({
    declarations: [],
    imports: [
        PrimeNgModules
    ],
    exports: [PrimeNgModules],
    providers: [],
    bootstrap: []
})
export class PrimeNgModule { }