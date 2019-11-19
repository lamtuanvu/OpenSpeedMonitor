import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TimeSeriesComponent} from './time-series.component';
import {SharedMocksModule} from "../../testing/shared-mocks.module";
import {ResultSelectionModule} from "../result-selection/result-selection.module";
import {OsmLangService} from "../../services/osm-lang.service";
import {GrailsBridgeService} from "../../services/grails-bridge.service";
import {TimeSeriesLineChartComponent} from "./components/time-series-line-chart/time-series-line-chart.component";
import {SpinnerService} from "../shared/services/spinner.service";

describe('TimeSeriesComponent', () => {
  let component: TimeSeriesComponent;
  let fixture: ComponentFixture<TimeSeriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        TimeSeriesComponent,
        TimeSeriesLineChartComponent
      ],
      imports: [
        SharedMocksModule,
        ResultSelectionModule
      ],
      providers: [
        OsmLangService,
        GrailsBridgeService,
        SpinnerService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
