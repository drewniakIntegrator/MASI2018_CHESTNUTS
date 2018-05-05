import { Component, OnInit, ViewChild, ElementRef, Input, ViewEncapsulation } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';
import { MESSAGESSTATIC } from '../../models/messagesstatic';
import { ScrollbarComponent } from 'ngx-scrollbar';

@Component({
    selector: 'app-chatview',
    templateUrl: './chatview.component.html',
    styleUrls: ['./chatview.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class ChatviewComponent implements OnInit {

    @ViewChild(ScrollbarComponent) scrollRef: ScrollbarComponent;
    @ViewChild('textMessage') textEl: ElementRef;

    nameUser: string;
    isSending: boolean;
    message: string;
    messages: Message[] = [];
    mockMessageIndex = 0;
    username: string;
    afterstart: boolean;
    hintOccurs: boolean;

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.mockMessageIndex = 0;
        this.dataService.getMessages()
            .subscribe((data) => { this.messages = data; });
    }

    sendName() {
        if (this.username) {
            this.afterstart = true;
            this.messages.push(MESSAGESSTATIC[this.mockMessageIndex]);
            setTimeout(() => {
                this.textEl.nativeElement.focus();
            }, 10);
        }
    }

    sendMessage() {
        if (this.message) {

            this.isSending = true;
            this.messages.push(new Message('', this.message, true));
            this.message = '';

            setTimeout(() => {
                this.isSending = false;

                if (this.mockMessageIndex >= MESSAGESSTATIC.length) {
                    this.mockMessageIndex = MESSAGESSTATIC.length - 1;
                }

                this.messages.push(MESSAGESSTATIC[this.mockMessageIndex]);

                setTimeout(() => {
                    this.scrollRef.scrollYTo(this.scrollRef.view.scrollHeight, 600);
                }, 10);

                setTimeout(() => {
                    this.textEl.nativeElement.focus();
                }, 10);
            }, 1000);

            this.mockMessageIndex++;
        }
    }

    chooseHint(hint: string) {
        this.message = hint;
        this.sendMessage();
    }

    resetChat() {
        this.messages.splice(0, this.messages.length);
        this.mockMessageIndex = 0;
        this.username = '';
        this.nameUser = '';
        this.afterstart = false;
    }

    exitChat() {
        //window.open(String(location), '_self', '');
        //window.close();
    }

    helpChat() {
        this.dataService.getHelp().subscribe(
            (data) => {
                this.messages.push(new Message('', this.buildHelpTree(data), false));
            }

        );
    }

    private buildHelpTree(tree, isSub = false): string {
        let html = `<div class="${isSub ? 'category-tree__submenu' : 'category-tree'}">`;

        tree.forEach(item => {
            let subMenu = '';
            if (item.sub && item.sub.length > 0){
                subMenu = this.buildHelpTree(item.sub, true);
            }

            html += `<a role="button">${item.name}</a>${subMenu}`;
        });

        html += '</div>';

        return html;
    }
}
