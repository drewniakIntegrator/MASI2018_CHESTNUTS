import { Injectable } from '@angular/core';
import { Message } from '../models/message';
import {Observable} from 'rxjs';

@Injectable()
export class DataService {

  messages: Message[] = [];
  constructor() {
    for (let i = 0; i < 5; i++) {
      this.messages.push(new Message(`0${i + 1}:01:1992 10:22`, `wiadomosc${i + 3}`, i % 2 === 0));
    }
  }

  getMessages(): Observable<Message[]> {
    return Observable.of(this.messages);
  }
}

