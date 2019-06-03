import {Component, OnInit} from '@angular/core';
import {combineLatest, Observable, ReplaySubject} from "rxjs";
import {Application} from "../../../../models/application.model";
import {Page} from "../../../../models/page.model";
import {ApplicationService} from "../../../../services/application.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {AspectConfigurationService} from "../../services/aspect-configuration.service";
import {ExtendedPerformanceAspect, PerformanceAspectType} from "../../../../models/perfomance-aspect.model";
import {filter, map} from "rxjs/operators";
import {MetricFinderService} from "../../../metric-finder/services/metric-finder.service";

@Component({
  selector: 'osm-edit-aspect-metrics',
  templateUrl: './edit-aspect-metrics.component.html',
  styleUrls: ['./edit-aspect-metrics.component.scss']
})
export class EditAspectMetricsComponent implements OnInit {

  application$: Observable<Application>;
  page$: Observable<Page>;
  performanceAspects$: Observable<ExtendedPerformanceAspect[]>;
  aspectType$ = new ReplaySubject<PerformanceAspectType>(1);
  browserId$ = new ReplaySubject<number>(1)

  constructor(
    private route: ActivatedRoute,
    private applicationService: ApplicationService,
    private aspectConfService: AspectConfigurationService,
    private metricFinderService: MetricFinderService) {

    this.application$ = applicationService.selectedApplication$;
    this.page$ = aspectConfService.selectedPage$;
    this.performanceAspects$ = aspectConfService.extendedAspects$;
    combineLatest(this.application$, this.page$, this.browserId$).subscribe(([app, page, browserId]: [Application, Page, number]) => {
      const now = new Date();
      const twoWeeksAgo = new Date(now.getTime() - 1000 * 60 * 60 * 24 * 14);
      this.metricFinderService.loadData(twoWeeksAgo, now, app.id, page.id, browserId);
    })
  }


  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.browserId$.next(Number(params.get('browserId')));
      this.aspectConfService.loadApplication(params.get('applicationId'));
      this.aspectConfService.loadPage(params.get('pageId'));
      this.aspectConfService.uniqueAspectTypes$.pipe(
        filter((uniqueTypes: PerformanceAspectType[]) => {
          return uniqueTypes.some((type: PerformanceAspectType) => type.name == params.get('aspectType'))
        }),
        map((types: PerformanceAspectType[]) => types.find((type: PerformanceAspectType) => type.name == params.get('aspectType')))
      ).subscribe((type: PerformanceAspectType) => this.aspectType$.next(type));
    });
  }

}
