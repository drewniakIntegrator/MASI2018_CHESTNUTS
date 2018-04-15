import { Injectable } from '@angular/core';
import { Message } from '../models/message';
import {Observable} from 'rxjs';

@Injectable()
export class DataService {

  messages: Message[] = [];
  constructor() {
    // for (let i = 0; i < 5; i++) {
       this.messages.push(new Message(``, `Hello! How can I help you?`, false));
    // }
  }

  getMessages(): Observable<Message[]> {
    return Observable.of(this.messages);
  }
}

