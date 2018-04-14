import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';

@Component({
  selector: 'app-chatview',
  templateUrl: './chatview.component.html',
  styleUrls: ['./chatview.component.scss']
})
export class ChatviewComponent implements OnInit {

  isSending: boolean;
  message: string;
  messages: Message[] = [];

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.dataService.getMessages()
      .subscribe((data) => { this.messages = data; });
  }

  sendMessage() {
    if (this.message) {
      console.log(this.message);
      this.isSending = true;
      setTimeout(() => { this.isSending = false; this.message = ''; }, 2000);
    }
  }
}
