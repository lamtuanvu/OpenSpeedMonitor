import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MetricFinderComponent} from './metric-finder.component';
import {LineChartComponent} from './components/line-chart/line-chart.component';
import {MetricFinderService} from './services/metric-finder.service';
import {FormsModule} from '@angular/forms';
import {FilmstripComponent} from './components/filmstrip/filmstrip.component';
import {FilmstripService} from './services/filmstrip.service';
import {FilmstripServiceMock} from './services/filmstrip.service.mock';
import {ComparableFilmstripsComponent} from './components/comparable-filmstrips/comparable-filmstrips.component';
import {MetricFinderServiceMock} from './services/metric-finder.service.mock';
import {MetricSelectionComponent} from './components/metric-selection/metric-selection.component';
import {ResultSelectionService} from '../result-selection/services/result-selection.service';
import {SharedMocksModule} from '../../testing/shared-mocks.module';

describe('MetricFinderComponent', () => {
  let component: MetricFinderComponent;
  let fixture: ComponentFixture<MetricFinderComponent>;
  let metricService: MetricFinderService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        MetricFinderComponent,
        FilmstripComponent,
        LineChartComponent,
        ComparableFilmstripsComponent,
        MetricSelectionComponent
      ],
      imports: [
        FormsModule,
        SharedMocksModule
      ],
      providers: [
        {
          provide: MetricFinderService,
          useClass: MetricFinderServiceMock
        }, {
          provide: FilmstripService,
          useClass: FilmstripServiceMock
        },
        ResultSelectionService
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    metricService = TestBed.get(MetricFinderService);
    fixture = TestBed.createComponent(MetricFinderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
