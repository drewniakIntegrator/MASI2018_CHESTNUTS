import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { DataService } from './services/data.service';
import { ScrollbarModule } from 'ngx-scrollbar';


import { AppComponent } from './app.component';
import { ChatviewComponent } from './components/chatview/chatview.component';
import { EndviewComponent } from './components/endview/endview.component';
import { ReportsComponent } from './components/chatview/reports/reports.component';


@NgModule({
  declarations: [
    AppComponent,
    ChatviewComponent,
    EndviewComponent,
    ReportsComponent
  ],
  imports: [
    BrowserModule,
    ScrollbarModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
