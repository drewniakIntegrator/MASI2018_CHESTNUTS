import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { Message } from '../models/message';
import { Observable } from 'rxjs';
import { CATEGORIES_TREE } from '../models/categorytree';
import { environment } from '../../environments/environment';

@Injectable()
export class DataService {

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

    // addHouse(house: House): Observable<any> {
    //     return this.http
    //         .post(`${environment.url}/houses`, house)
    //         .map((response: Response) => response.json())
    //         .catch((error: Response) => {
    //             if (error.status >= 400 || error.status === 0) {
    //                 return Observable.throw(error.json());
    //             }
    //         });
    // }
}
