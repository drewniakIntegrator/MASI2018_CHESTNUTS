import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { Message } from '../models/message';
import { Observable } from 'rxjs';
import { CATEGORIES_TREE } from '../models/categorytree';
import { environment } from '../../environments/environment';
import { ResponseObject } from '../models/responseObject';
import { RequestObject } from '../models/requestObject';

@Injectable()
export class DataService {

    conversationId: string;

    messages: Message[] = [];
    constructor(private http: HttpClient) {
        // wywolanie po sendName
        // this.messages.push(new Message(``, `Hello! How can I help you?`, false, 'x'));
    }

    getMessages(): Observable<Message[]> {
        return Observable.of(this.messages);
    }

    getHelp(): Observable<any> {
        return Observable.of(CATEGORIES_TREE);
    }

    initConversation(): Observable<any> {
        return this.http
            .get(`${environment.url}/conversations/init`);
    }

    sendMessage(message: string): Observable<any> {
        const msg: RequestObject = new RequestObject(message, this.conversationId);

        return this.http
            .post(`${environment.url}/conversations`, msg);
    }
}
