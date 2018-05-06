import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {DataService} from './services/data.service';
import { ScrollbarModule } from 'ngx-scrollbar';


import { AppComponent } from './app.component';
import { ChatviewComponent } from './components/chatview/chatview.component';


@NgModule({
  declarations: [
    AppComponent,
    ChatviewComponent
  ],
  imports: [
    BrowserModule,
    ScrollbarModule,
    FormsModule
  ],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
