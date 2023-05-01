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