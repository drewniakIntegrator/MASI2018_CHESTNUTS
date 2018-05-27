import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { DataService } from '../../services/data.service';
import { ScoreRequest } from '../../models/scoreRequest';


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

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.hover.stars1 = -1;
        this.hover.stars2 = -1;
        this.stars = Array(10).fill(0).map((x, i) => i);
    }

    send() {
        const scores: ScoreRequest = new ScoreRequest();
        scores.usabilityScore = this.active.stars1 + 1;
        scores.effectivenessScore = this.active.stars2 + 1;

        this.dataService.sendScores(scores).subscribe(
            (data) => {
                this.done.emit('SEND');
            },
            (error) => {

            }
        );
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
