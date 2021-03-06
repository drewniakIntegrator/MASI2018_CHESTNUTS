import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { HttpClient } from '@angular/common/http';
import { Message } from '../models/message';
import { Observable } from 'rxjs';
import { CATEGORIES_TREE } from '../models/categorytree';
import { environment } from '../../environments/environment';
import { ResponseObject } from '../models/responseObject';
import { RequestObject } from '../models/requestObject';
import { ScoreRequest } from '../models/scoreRequest';

@Injectable()
export class DataService {

    conversationId: string;

    messages: Message[] = [];
    constructor(private http: HttpClient) {
    }

    getMessages(): Observable<Message[]> {
        return Observable.of(this.messages);
    }

    getHelp(): Observable<any> {
        return Observable.of(CATEGORIES_TREE);
    }

    initConversation(username: string): Observable<any> {
        return this.http
            .get(`${environment.url}/conversations/init?username=${username}`);
    }

    sendMessage(message: string): Observable<any> {
        const msg: RequestObject = new RequestObject(message, this.conversationId);

        return this.http
            .post(`${environment.url}/conversations`, msg);
    }

    sendScores(scores: ScoreRequest): Observable<any> {
        return this.http
            .post(`${environment.url}/conversations/${this.conversationId}/score`, scores);
    }

    getReports(): Observable<any> {
        return this.http
            .get(`${environment.url}/conversations/${this.conversationId}/report`);
    }
}
