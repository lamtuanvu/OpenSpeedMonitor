import { Component, OnInit, Input } from '@angular/core';
import {Threshold} from '../../service/model/threshold.model';
import { ThresholdRestService } from '../../service/rest/threshold-rest.service';
import {Measurand} from "../../service/model/measurand.model";

@Component({
  selector: 'osm-threshold',
  templateUrl: './threshold.component.html',
  styleUrls: ['./threshold.component.css']
})
export class ThresholdComponent implements OnInit {
  @Input() threshold: Threshold;
  @Input() allowthresholdAdd: boolean;
  measurandList: Measurand[];
  selectedOption: string;

  allowInput = false;
  leftButtonLabel= "Editieren";

  constructor(private thresholdRestService: ThresholdRestService) {
    this.thresholdRestService.measurands$.subscribe((next: Measurand[]) => {
      this.measurandList = next;
    } );
  }

  ngOnInit() {
    console.log("this.allowthresholdAdd: " + this.allowthresholdAdd);
    console.log("Threshold.component ngOninit this.threshold: " + JSON.stringify(this.threshold));
    /*if (this.threshold.state == "new") {
      this.leftButtonLabel = "Speichern";
    }*/
  }

  delete(thresholdID) {
    console.log("DELETE");
    this.thresholdRestService.deleteThreshold(thresholdID);
  }

  edit() {
    console.log("EDIT");
    this.allowInput = !this.allowInput;
    this.allowInput
      ? this.leftButtonLabel = "Übernehmen"
      : (
      this.leftButtonLabel = "Editieren",
        this.thresholdRestService.editThreshold(this.threshold)
    ) ;
  }

  save() {
    console.log("SAVE");
    this.threshold.measurand.name = this.selectedOption;
    this.thresholdRestService.addThreshold(this.threshold)
  }

  remove() {
    console.log("REMOVE");
  }


}
