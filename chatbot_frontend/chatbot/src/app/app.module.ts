import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {DataService} from './services/data.service';
import { ScrollbarModule } from 'ngx-scrollbar';


import { AppComponent } from './app.component';
import { ChatviewComponent } from './components/chatview/chatview.component';
import { EndviewComponent } from './components/endview/endview.component';


@NgModule({
  declarations: [
    AppComponent,
    ChatviewComponent,
    EndviewComponent
  ],
  imports: [
    BrowserModule,
    ScrollbarModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
