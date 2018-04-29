import { Injectable } from '@angular/core';
import { Message } from '../models/message';
import { Observable } from 'rxjs';

@Injectable()
export class DataService {

  messages: Message[] = [];
  hintList: Array<string> = [];
  constructor() {
    // wywolanie po sendName
    // this.messages.push(new Message(``, `Hello! How can I help you?`, false, 'x'));
  }

  getMessages(): Observable<Message[]> {
    return Observable.of(this.messages);
  }

  getHints(): Observable<string[]> {
    return Observable.of(this.hintList);
  }
}

