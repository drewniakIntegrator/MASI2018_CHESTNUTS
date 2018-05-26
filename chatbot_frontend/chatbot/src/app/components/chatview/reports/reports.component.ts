import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'app-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

    @Output()
    reset = new EventEmitter();

    constructor() { }

    ngOnInit() {
    }

    restart() {
        this.reset.emit("RESET");
    }

}
