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
import { DividerModule } from 'primeng/divider';
import { FieldsetModule } from 'primeng/fieldset';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { MegaMenuModule } from 'primeng/megamenu';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { CardModule } from 'primeng/card';
import { CarouselModule } from 'primeng/carousel';
import { PaginatorModule } from 'primeng/paginator';
import { SplitterModule } from 'primeng/splitter';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { TabMenuModule } from 'primeng/tabmenu';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ProgressBarModule } from 'primeng/progressbar';
import { DropdownModule } from 'primeng/dropdown';

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
    ToastModule,
    TabViewModule,
    DividerModule,
    FieldsetModule,
    ConfirmPopupModule,
    MegaMenuModule,
    TieredMenuModule,
    CardModule,
    CarouselModule,
    PaginatorModule,
    SplitterModule,
    ScrollPanelModule,
    TabMenuModule,
    SelectButtonModule,
    ToggleButtonModule,
    TagModule,
    TooltipModule,
    ProgressBarModule,
    DropdownModule
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