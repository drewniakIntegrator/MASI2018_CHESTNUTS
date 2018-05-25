import { Component, OnInit, ViewChild, ElementRef, Input, ViewEncapsulation } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';
import { MESSAGESSTATIC } from '../../models/messagesstatic';
import { ScrollbarComponent } from 'ngx-scrollbar';
import { trigger, animate, style, state, transition } from '@angular/animations';
//import { relative } from 'path';
import { findLast } from '@angular/compiler/src/directive_resolver';
import { ResponseObject } from '../../models/responseObject';

declare var jquery: any;   // not required
declare var $: any;   // not required

@Component({
    selector: 'app-chatview',
    templateUrl: './chatview.component.html',
    styleUrls: ['./chatview.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations: [
        trigger('slideInFromLeft', [
            transition('void => *', [
                style({ opacity: '0' }),
                animate('500ms ease-in', style({ opacity: '1' }))
            ])
        ]),
        trigger('slideInFromRight', [
            transition('void => *', [
                style({ opacity: '0' }),
                animate('500ms ease-in', style({ opacity: '1' }))
            ])
        ]),
        trigger('loader', [
            state('inactive', style({ transform: 'translateY(0%)' })),
            state('active', style({ transform: 'translateY(-20%)' })),
            transition('* <=> *', [
                animate(250)
            ])
        ])
    ]
})
export class ChatviewComponent implements OnInit {

    @ViewChild(ScrollbarComponent) scrollRef: ScrollbarComponent;
    @ViewChild('textMessage') textEl: ElementRef;

    isSending: boolean;
    isUsernameSending: boolean;

    nameUser: string;
    isHelpSending: boolean;
    message: string;
    messages: Message[] = [];
    mockMessageIndex = 0;
    username: string;
    afterstart: boolean;
    hintOccurs: boolean;
    state: string;
    isEnd: boolean;

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.isUsernameSending = false;
        this.isEnd = false;
        this.helpAction();
        this.mockMessageIndex = 0;
        this.dataService.getMessages()
            .subscribe((data) => { this.messages = data; });
    }

    private addChatbotMessage(response: ResponseObject) {
        const newMessage: Message = new Message('', response.message, false, response.hints, response.items);
        this.messages.push(newMessage);
    }

    private addUserMessage(text: string) {
        const newMessage: Message = new Message('', text, true, [], []);
        this.messages.push(newMessage);
    }

    private scrollChatbot(): void {
        setTimeout(() => {
            this.scrollRef.scrollYTo(this.scrollRef.view.scrollHeight, 200);
        }, 10);
    }

    private focusTextarea(): void {
        setTimeout(() => {
            this.textEl.nativeElement.focus();
        }, 10);
    }

    initChat() {
        if (this.username) {
            this.isUsernameSending = true;
            this.dataService.initConversation(this.username).subscribe(
                (data: ResponseObject) => {
                    this.dataService.conversationId = data.conversationId;
                    this.addChatbotMessage(data);
                    this.afterstart = true;
                    this.isUsernameSending = false;
                    this.focusTextarea();
                },
                (error) => {

                }
            );
        }
    }

    sendMessage() {
        if (this.message) {
            const messageCopy: string = this.message.replace(/[^a-zA-Z0-9 ]/g, '');
            this.isSending = true;

            this.addUserMessage(this.message);

            this.message = '';

            this.scrollChatbot();

            this.dataService.sendMessage(messageCopy).subscribe(
                (data: ResponseObject) => {
                    this.isSending = false;

                    this.addChatbotMessage(data);

                    this.scrollChatbot();
                    this.focusTextarea();
                },
                (error) => {
                    this.isSending = false;
                }
            );
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
        this.isEnd = false;
    }

    exitChat() {
        this.isEnd = true;
    }

    helpChat() {
        this.isHelpSending = true;
        this.messages.push(new Message('', 'CATEGORY_TREE', true));
        this.dataService.getHelp().subscribe(
            (data) => {
                this.messages.push(new Message('', this.buildHelpTree(data), false));
                this.isHelpSending = false;
            }

        );
    }

    private helpAction(): void {
        const CONTAINER_CLASS = '.category-row';
        const ACTIVE_CLASS = 'isOpen';

        $('body').off('click', '.category-tree a').on('click', '.category-tree a', function () {

            const $parent = $(this).closest(CONTAINER_CLASS);
            const $activeElements = $parent.children(`.${ACTIVE_CLASS}`);
            const $this = $(this);
            const wasActive = $this.hasClass(ACTIVE_CLASS);

            $activeElements.each(function () {

                $(this).removeClass(ACTIVE_CLASS);
                $(this).next('div').slideUp();

            });

            if (!wasActive) {
                if ($this.next('div').length > 0) {
                    $this.addClass(ACTIVE_CLASS);
                    $this.next('div').slideDown();
                }
            }

        });
    }

    private buildHelpTree(tree, isSub = false): string {
        let html = `<div class="category-row ${isSub ? 'category-tree__submenu' : 'category-tree'}">`;

        tree.forEach(item => {
            let subMenu = '';
            if (item.sub && item.sub.length > 0) {
                subMenu = this.buildHelpTree(item.sub, true);
            }

            html += `<a role="button">${item.name}</a>${subMenu}`;
        });

        html += '</div>';

        return html;
    }

    onDone($event) {
        for (let i = 0; i < 5; i++) {
            this.state = this.state === 'active' ? 'inactive' : 'active';
        }
    }
}
