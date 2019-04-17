import {
    Component, Input,
    OnInit,
    ViewEncapsulation
  } from '@angular/core';

import {Caller, ResultSelectionCommand} from "../../models/result-selection-command.model";
import {ResultSelectionService} from "../../services/result-selection.service";
import { SelectableApplication } from 'src/app/models/application.model';
import { ReplaySubject } from 'rxjs';
import { Chart } from '../../models/chart.model';
import { sharedService } from '../../services/sharedService';

@Component({
    selector: 'osm-result-selection-job-group',
    templateUrl: './result-selection-job-group.component.html',
    styleUrls: ['./result-selection-job-group.component.scss'],
    encapsulation: ViewEncapsulation.None
  })
  export class ResultSelectionJobGroupComponent implements OnInit {
    @Input() currentChart: string;
    @Input() jobGroupMappings$: ReplaySubject<SelectableApplication[]>;  
    resultSelectionCommand: ResultSelectionCommand;
    jobGroups = new Array();
    isEmpty = true;
    
    constructor(private resultSelectionService: ResultSelectionService, private sharedService: sharedService) {

    }

    ngOnInit() {
        let defaultFrom = new Date();
        let defaultTo = new Date();
        defaultTo.setHours(23, 59, 59, 999);
        defaultFrom.setDate(defaultTo.getDate() - 28);
        defaultFrom.setHours(0, 0, 0, 0);

        this.resultSelectionCommand = new ResultSelectionCommand({
            from: defaultFrom,
            to: defaultTo,
            caller: Caller.EventResult,
            jobGroupIds: [],
            pageIds: [],
            locationIds: [],
            browserIds: [],
            measuredEventIds: [],
            selectedConnectivities: []
        }); 
        //this.resultSelectionService.loadSelectableData(this.resultSelectionCommand,Chart[this.currentChart]);
        this.sharedService.currentMessage.subscribe(selectedDates => this.registerTimeFrameChangeEvents(selectedDates));
      }

      registerTimeFrameChangeEvents(dates: Date[]):void {
        this.resultSelectionCommand.from = dates[0];
        this.resultSelectionCommand.to = dates[1];
        this.getJobGroups(this.resultSelectionCommand);

      }

      getJobGroups(resultSelectionCommand: ResultSelectionCommand) {
        this.resultSelectionService.loadSelectableApplications(resultSelectionCommand);
        this.jobGroupMappings$ = this.resultSelectionService.applications$;
        this.jobGroupMappings$.subscribe(jobGroups => this.sortJobGroups(jobGroups));
      }

      sortJobGroups(jobGroups: SelectableApplication[]): void{
        this.jobGroups = jobGroups;
        if(this.jobGroups!=null && this.jobGroups.length>0){
          this.isEmpty=false;
          this.jobGroups.sort((a, b) => {
            if(a.name.toLowerCase() > b.name.toLowerCase()){
              return 1;
            }
            if(a.name.toLowerCase() < b.name.toLowerCase()){
              return -1;
            }
            return 0;
          });
        }else{
          this.isEmpty=true;
        }
      }

      
      
  }