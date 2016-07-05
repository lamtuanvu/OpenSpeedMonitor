package geb.pages.de.iteratec.osm.result

import geb.Page

/**
 * Created by marcus on 16.06.16.
 */
class EventResultDashboardPage extends Page{
    static url = "/eventResultDashboard/showAll"

    static at = { title == "Dashboard" }

    static content = {
        showButton (to: EventResultDashboardPage) {$("#graphButtonHtmlId")}
        jobGroupList{$("#folderSelectHtmlId").find("option").contextElements}
        pageList{$("#pageSelectHtmlId").find("option").contextElements}
        timeFrameSelect{$("#timeframeSelect").find("option").contextElements[0]}
        fromDatepicker{$("#fromDatepicker")}
        toDatepicker{$("#toDatepicker")}
        firstViewDiv{$("#selectAggregatorUncachedHtmlId")}
        firstViewList{$("#selectAggregatorUncachedHtmlId").find("option").contextElements}
        filterJobsAccordionContent { $("#collapseTwo") }
        filterJobsAccordion { $(".accordion-toggle")[1] }
        chooseMeasuredVariablesAccordionContent { $("#collapseThree") }
        clickChooseMeasuredVariablesAccordion { $(".accordion-toggle")[2].click() }
        adjustChartAccordion { $(".accordion-toggle")[3] }
        chartTitleInputField{$("#dia-title")}
        chartTitle{$("#rickshaw_chart_title").attr("innerHTML")}
        chartWidthInputField{$("#dia-width")}
        chartheightInputField{$("#dia-height")}
        diaChangeChartsizeButton{$("#dia-change-chartsize")}
        diaYAxisMinInputField{$("#dia-y-axis-min")}
        diaYAxisMaxInputField{$("#dia-y-axis-max")}
        diaChangeYAxisButton{$("#dia-change-yaxis")}
        showDataMarkersCheckBox{$("#to-enable-marker")}
        showDataLabelsCheckBox{$("#to-enable-label")}
        optimizeForWideScreenCheckBox{$("#wide-screen-diagram-montage")}
        addAliasButton{$("#addAliasButton")}
        aliasInputField{$(".input-xxlarge")[2]}
        graphName{$("span.label").attr("innerHTML")}
        colorPicker{$("#color")}
        graphColorField{$(".swatch").attr("style")}
        graphLineDiv{$(".path")}
        graphLine{$(".path").attr("d")}
        dataMarker{$(".pointMarker")}
        dataLabel{$(".dataLabel")}
        saveAsDashboardButton(required:false){$("a",href:"#CreateUserspecifiedDashboardModal")}
        dashboardNameFromModalTextField(required: false){$("#dashboardNameFromModal")}
        saveDashboardButtonButton{$("#saveDashboardButton")}
        saveDashboardSuccessMessage(required: false ){$("#saveDashboardSuccessDiv")}
        customDashboardSelectionDropdown{$("#customDashBoardSelection").parent()}
        customDashboardSelectionList{$("#customDashBoardSelection")}
        appendedInputBelowLoadTimesTextField{$("#appendedInputBelowLoadTimes")}
        appendedInputAboveLoadTimesTextField{$("#appendedInputAboveLoadTimes")}
        appendedInputBelowRequestCountsTextField{$("#appendedInputBelowRequestCounts")}
        appendedInputAboveRequestCountsTextField{$("#appendedInputAboveRequestCounts")}
        appendedInputBelowRequestSizesTimesTextField{$("#appendedInputBelowRequestSizes")}
        appendedInputAboveRequestSizesTextField{$("#appendedInputAboveRequestSizes")}


    }
}
