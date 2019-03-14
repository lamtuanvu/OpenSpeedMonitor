import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EMPTY, Observable, OperatorFunction, ReplaySubject, combineLatest} from "rxjs";
import { SelectableMeasurand} from "../models/measurand.model";
import {ResponseWithLoadingState} from "../models/response-with-loading-state.model";
import {catchError, switchMap, map} from "rxjs/operators";
import {Application} from "../models/application.model";
import {Page} from "../models/page.model";

@Injectable({
  providedIn: 'root'
})
export class ResultSelectionService {
  measurands$: ReplaySubject<ResponseWithLoadingState<SelectableMeasurand[]>> = new ReplaySubject(1);
  userTimings$: ReplaySubject<ResponseWithLoadingState<SelectableMeasurand[]>> = new ReplaySubject(1);
  heroTimings$: ReplaySubject<ResponseWithLoadingState<SelectableMeasurand[]>> = new ReplaySubject(1);

  selectedApplication$: ReplaySubject<Application[]> = new ReplaySubject<Application[]>(1);
  selectedPage$: ReplaySubject<Page[]> = new ReplaySubject<Page[]>(1);

  constructor(private http: HttpClient) {
    this.getMeasurands();

    combineLatest(this.selectedApplication$, this.selectedPage$,  (applications: Application[], pages:Page[]) =>this.generateParams(applications, pages)).pipe(
      switchMap(params => this.getUserTimings(params))
    ).subscribe(this.userTimings$);

    combineLatest(this.selectedApplication$, this.selectedPage$, (applications: Application[], pages:Page[]) =>this.generateParams(applications, pages)).pipe(
      switchMap(params => this.getHeroTimings(params))
    ).subscribe(this.heroTimings$)
  }

  updateApplications(applications: Application[]){
    this.selectedApplication$.next(applications)
  }
  updatePages(pages: Page[]){
    this.selectedPage$.next(pages);
  }

  private getUserTimings(params): Observable<ResponseWithLoadingState<SelectableMeasurand[]>>{
    const userTimingsUrl: string = '/resultSelection/getUserTimings';
    return this.getSelectableMeasurands(userTimingsUrl, this.userTimings$, params);
  }
  private getHeroTimings(params): Observable<ResponseWithLoadingState<SelectableMeasurand[]>>{
    const heroTimingsUrl: string = '/resultSelection/getHeroTimings';
    return this.getSelectableMeasurands(heroTimingsUrl, this.heroTimings$, params);
  }

  private getMeasurands(){
    const measurandUrl: string = '/resultSelection/getMeasurands';
    this.getSelectableMeasurands(measurandUrl,this.measurands$).subscribe(next => this.measurands$.next(next));
  }

  private getSelectableMeasurands(url: string, subject: ReplaySubject<ResponseWithLoadingState<SelectableMeasurand[]>>, params?: any): Observable<ResponseWithLoadingState<SelectableMeasurand[]>>{
    subject.next({isLoading: true, data:[]});
    return this.http.get<SelectableMeasurand[]>(url, {params}).pipe(
      handleError(),
      map(dtos => ({isLoading: false, data: dtos})),
    )
  }

  private generateParams(applications: Application[], pages: Page[]){
    let now: Date = new Date();
    let threeDaysAgo: Date = new Date();
    threeDaysAgo.setDate(threeDaysAgo.getDate() - 3);
    return {
      jobGroupIds: applications.map(app => app.id),
      pageIds: pages.map(page => page.id),
      from: threeDaysAgo.toISOString(),
      to: now.toISOString()
    }
  }
}

function handleError(): OperatorFunction<any, any> {
  return catchError((error) => {
    console.log(error);
    return EMPTY;
  });
}
