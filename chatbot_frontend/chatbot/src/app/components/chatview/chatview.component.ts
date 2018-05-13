import { Component, OnInit, ViewChild, ElementRef, Input, ViewEncapsulation } from '@angular/core';
import { DataService } from '../../services/data.service';
import { Message } from '../../models/message';
import { MESSAGESSTATIC } from '../../models/messagesstatic';
import { ScrollbarComponent } from 'ngx-scrollbar';
import { trigger, animate, style, state, transition } from '@angular/animations';
//import { relative } from 'path';
import { findLast } from '@angular/compiler/src/directive_resolver';

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
        style({transform: 'translateX(-100%)'}),
        animate('250ms ease-in')
      ])
    ]),
    trigger('slideInFromRight', [
      transition('void => *', [
        style({transform: 'translateX(+100%)'}),
        animate('250ms ease-in')
      ])
    ]),
    trigger('loader', [
      state('inactive', style({transform: 'translateY(0%)'})),
      state('active', style({transform: 'translateY(-20%)'})),
      transition('* <=> *', [
        animate(250)
      ])
    ])
  ]
})
export class ChatviewComponent implements OnInit {

    @ViewChild(ScrollbarComponent) scrollRef: ScrollbarComponent;
    @ViewChild('textMessage') textEl: ElementRef;

    nameUser: string;
    isSending: boolean;
    isHelpSending: boolean;
    message: string;
    messages: Message[] = [];
    mockMessageIndex = 0;
    username: string;
    afterstart: boolean;
    hintOccurs: boolean;
    state: string;
    isEnd: boolean = false;

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.helpAction();
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
                this.scrollRef.scrollYTo(this.scrollRef.view.scrollHeight, 200);
            }, 10);

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
        this.isEnd = false;
    }

    exitChat() {
        //window.open(String(location), '_self', '');
        //window.close();

        //Chwilowe rozwiązanie
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
      // call this function at the end of the previous animation.
      // run it as many time as defined
      for (var i=0; i<5; i++) {
        this.state = this.state === 'active' ? 'inactive' : 'active';
      }
    }
}
