import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { DataService } from '../../../services/data.service';

@Component({
    selector: 'app-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

    @Output()
    reset = new EventEmitter();

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.dataService.getReports().subscribe(
            (data) => {

            },
            (error) => {

            }
        );
    }

    restart() {
        this.reset.emit('RESET');
    }

}
