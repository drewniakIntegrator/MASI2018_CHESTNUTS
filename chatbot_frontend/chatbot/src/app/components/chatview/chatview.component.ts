import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';

@Component({
  selector: 'app-chatview',
  templateUrl: './chatview.component.html',
  styleUrls: ['./chatview.component.scss']
})
export class ChatviewComponent implements OnInit {

  @ViewChild('chatBody') el: ElementRef;

  isSending: boolean;
  message: string;
  messages: Message[] = [];
  x = 0;

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.dataService.getMessages()
      .subscribe((data) => { this.messages = data; });
  }
  sendMessage() {
    if (this.message) {

      const tab = ['Hello', 'What category do you like?', 'Older or newer film? ',
        'Which actor do you like?', 'Matrix, Matrix 2', 'Just google bro. I am tired', 'What did you say?'];

      this.isSending = true;
      this.messages.push(new Message(`dsa`, this.message, true));
      this.message = '';

      setTimeout(() => {
        this.el.nativeElement.scrollTo(0, this.el.nativeElement.scrollHeight);
      }, 10);

      setTimeout(() => {
        this.isSending = false;
        this.messages.push(new Message(`dsa`, tab[this.x], false));

        setTimeout(() => {
          this.el.nativeElement.scrollTo(0, this.el.nativeElement.scrollHeight);
        }, 10);
      }, 2000);
      this.x++;
    }
  }
}
