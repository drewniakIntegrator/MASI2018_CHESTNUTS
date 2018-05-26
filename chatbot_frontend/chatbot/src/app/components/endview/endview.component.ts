import { Component, OnInit, Output, EventEmitter } from '@angular/core';


@Component({
    selector: 'app-endview',
    templateUrl: './endview.component.html',
    styleUrls: ['./endview.component.scss']
})
export class EndviewComponent implements OnInit {

    @Output()
    done = new EventEmitter();

    hover: {
        stars1: number,
        stars2: number
    } = { stars1: -1, stars2: -1 };

    active: {
        stars1: number,
        stars2: number
    } = { stars1: -1, stars2: -1 };

    stars: number[] = [];

    constructor() { }

    ngOnInit() {
        this.hover.stars1 = -1;
        this.hover.stars2 = -1;
        this.stars = Array(10).fill(0).map((x, i) => i);
    }

    send() {
        this.done.emit('SEND');
    }

    hoverStars1(star: number) {
        this.hover.stars1 = star;
    }

    selectStars1(star: number) {
        this.active.stars1 = star;
    }

    noHoverStars1() {
        this.hover.stars1 = -1;
    }

    hoverStars2(star: number) {
        this.hover.stars2 = star;
    }

    selectStars2(star: number) {
        this.active.stars2 = star;
    }

    noHoverStars2() {
        this.hover.stars2 = -1;
    }
}
