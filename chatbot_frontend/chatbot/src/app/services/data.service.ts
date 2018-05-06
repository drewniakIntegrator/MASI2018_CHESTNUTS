import { Injectable } from '@angular/core';
import { Message } from '../models/message';
import { Observable } from 'rxjs';
import { CATEGORIES_TREE } from '../models/categorytree';

@Injectable()
export class DataService {

  messages: Message[] = [];
  constructor() {
    // wywolanie po sendName
    // this.messages.push(new Message(``, `Hello! How can I help you?`, false, 'x'));
  }

  getMessages(): Observable<Message[]> {
    return Observable.of(this.messages);
  }

  getHelp(): Observable<any> {
      return Observable.of(CATEGORIES_TREE);
  }
}
