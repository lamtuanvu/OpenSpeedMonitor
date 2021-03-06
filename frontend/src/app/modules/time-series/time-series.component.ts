import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {URL} from '../../enums/url.enum';
import {LineChartDataService} from './services/line-chart-data.service';
import {ResultSelectionStore} from '../result-selection/services/result-selection.store';
import {EventResultData, EventResultDataDTO} from './models/event-result-data.model';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'osm-time-series',
  templateUrl: './time-series.component.html',
  styleUrls: ['./time-series.component.scss'],

  // used to render context menu with styles from time-series.component.scss file
  encapsulation: ViewEncapsulation.None
})
export class TimeSeriesComponent implements OnInit {

  public showTimeSeriesChart = false;
  public results$ = new BehaviorSubject<EventResultDataDTO>(new EventResultData());

  constructor(private linechartDataService: LineChartDataService, private resultSelectionStore: ResultSelectionStore) {
  }

  ngOnInit() {
    if (this.resultSelectionStore.validQuery) {
      this.getTimeSeriesChartData();
    }
  }

  getTimeSeriesChartData() {
    this.showTimeSeriesChart = true;
    this.results$.next(null);

    this.linechartDataService.fetchEventResultData<EventResultDataDTO>(
      this.resultSelectionStore.resultSelectionCommand,
      this.resultSelectionStore.remainingResultSelection,
      URL.EVENT_RESULT_DASHBOARD_LINECHART_DATA
    ).subscribe(next => this.results$.next(next));
  }
}
