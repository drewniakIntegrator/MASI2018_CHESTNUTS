import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { DataService } from '../../../services/data.service';
import { Stats } from '../../../models/stats';
import { QEIItem } from '../../../models/qeiItem';

@Component({
    selector: 'app-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

    @Output()
    reset = new EventEmitter();

    productsQEI: QEIItem[] = [];
    summary: Stats = new Stats();

    constructor(private dataService: DataService) { }

    ngOnInit() {
        this.dataService.getReports().subscribe(
            (data: Stats) => {
                this.summary = data;
                this.prepareQEI(this.summary.conversationSummary.numberOfQuestionToAmountOfProducts);
            },
            (error) => {

            }
        );
    }

    prepareQEI(data) {
        console.log(Object.keys(data), Object.keys(data).length);
        for (let i = 0; i < Object.keys(data).length - 1; i++) {
            const key = Object.keys(data)[i];
            const nextKey = Object.keys(data)[i + 1];

            if (parseInt(data[key], 10) !== 0 && parseInt(data[nextKey], 10) !== 0) {
                this.productsQEI.push(new QEIItem(parseInt(data[key], 10), parseInt(data[nextKey], 10)));
            }
        }
    }

    restart() {
        this.reset.emit('RESET');
    }

}
