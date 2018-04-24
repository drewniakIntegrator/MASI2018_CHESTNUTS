import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';

@Component({
  selector: 'app-chatview',
  templateUrl: './chatview.component.html',
  styleUrls: ['./chatview.component.scss']
})
export class ChatviewComponent implements OnInit {

  @ViewChild('chatBody') bodyEl: ElementRef;
  @ViewChild('textMessage') textEl: ElementRef;

  nameUser: string;
  isSending: boolean;
  message: string;
  messages: Message[] = [];
  x = 0;
  username: string;
  afterstart: boolean;

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.dataService.getMessages()
      .subscribe((data) => { this.messages = data; });
  }

  sendName() {
    if (this.username) {
      this.afterstart = true;
      this.messages.push(new Message(``, `Hello! How can I help you?`, false, ''));
    }
  }

  sendMessage() {
    if (this.message) {

      const tab = ['Hello', 'What category do you like?', 'Older or newer film? ',
        'Which actor do you like?', 'Matrix, Matrix 2', 'Just google bro. I am tired', 'What did you say?'];

      this.isSending = true;
      this.messages.push(new Message(`dsa`, this.message, true, this.username));
      this.message = '';


      setTimeout(() => {
        this.bodyEl.nativeElement.scrollTo(0, this.bodyEl.nativeElement.scrollHeight);
      }, 10);

      setTimeout(() => {
        this.isSending = false;
        this.messages.push(new Message(`dsa`, tab[this.x], false, 'x'));
        setTimeout(() => {
          this.bodyEl.nativeElement.scrollTo(0, this.bodyEl.nativeElement.scrollHeight);
        }, 10);
        setTimeout(() => {
          this.textEl.nativeElement.focus();
        }, 10);
      }, 2000);
      this.x++;
    }
  }
}
