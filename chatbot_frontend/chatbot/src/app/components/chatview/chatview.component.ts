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
  hintList: Array<string> = [];
  hintOccurs: boolean;

  constructor(private dataService: DataService) { }

  ngOnInit() {
    this.dataService.getMessages()
      .subscribe((data) => { this.messages = data; });

      this.dataService.getHints()
      .subscribe((data) => { this.hintList = data; });

      this.hintList.push('SampleFilm1', 'SampleFilm2', 'SampleFilm3');
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
        if (this.x > 0) {
          setTimeout(() => {
            this.hintOccurs = true;
          }, 200);
        }
        setTimeout(() => {
          this.bodyEl.nativeElement.scrollTo(0, this.bodyEl.nativeElement.scrollHeight);
        }, 10);
        setTimeout(() => {
          this.textEl.nativeElement.focus();
        }, 10);
      }, 1000);
      this.x++;
      this.hintOccurs = false;
    }
  }

  chooseHint(hint) {
    // Przekierowanie na strone wybranego produktu
    console.log(hint.target.innerText);
  }

  resetChat() {
    this.messages.splice(0, this.messages.length);
    this.username = '';
    this.nameUser = '';
    this.afterstart = false;
    this.hintList.splice(0, this.hintList.length);
  }

  exitChat() {
    //window.open(String(location), '_self', '');
    //window.close();
  }
}
