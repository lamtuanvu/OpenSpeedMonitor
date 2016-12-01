package de.iteratec.osm.result

import de.iteratec.osm.csi.Page
import de.iteratec.osm.dimple.BarchartDTO
import de.iteratec.osm.dimple.BarchartDatum
import de.iteratec.osm.dimple.BarchartSeries
import de.iteratec.osm.dimple.GetBarchartCommand
import de.iteratec.osm.measurement.environment.Browser
import de.iteratec.osm.measurement.environment.Location
import de.iteratec.osm.measurement.environment.dao.BrowserDaoService
import de.iteratec.osm.measurement.environment.dao.LocationDaoService
import de.iteratec.osm.measurement.schedule.JobGroup
import de.iteratec.osm.measurement.schedule.dao.JobGroupDaoService
import de.iteratec.osm.measurement.schedule.dao.PageDaoService
import de.iteratec.osm.report.chart.MeasurandGroup
import de.iteratec.osm.util.ControllerUtils
import de.iteratec.osm.util.I18nService
import org.springframework.http.HttpStatus

class PageAggregationController {
    public final
    static Map<CachedView, Map<String, List<String>>> AGGREGATOR_GROUP_VALUES = ResultCsiAggregationService.getAggregatorMapForOptGroupSelect()

    public final static String DATE_FORMAT_STRING_FOR_HIGH_CHART = 'dd.mm.yyyy';
    public final static String DATE_FORMAT_STRING = 'dd.MM.yyyy';
    public final static int MONDAY_WEEKSTART = 1

    def intervals = ['not', 'hourly', 'daily', 'weekly']


    PageDaoService pageDaoService
    BrowserDaoService browserDaoService
    LocationDaoService locationDaoService
    JobGroupDaoService jobGroupDaoService
    EventResultDashboardService eventResultDashboardService
    I18nService i18nService

    def show() {
        Map<String, Object> modelToRender = [:]

        // AggregatorTypes
        modelToRender.put('aggrGroupValuesUnCached', AGGREGATOR_GROUP_VALUES.get(CachedView.UNCACHED))

        // JobGroups
        List<JobGroup> jobGroups = eventResultDashboardService.getAllJobGroups()
        modelToRender.put('folders', jobGroups)

        // Pages
        List<Page> pages = eventResultDashboardService.getAllPages()
        modelToRender.put('pages', pages)

        // MeasuredEvents
        List<MeasuredEvent> measuredEvents = eventResultDashboardService.getAllMeasuredEvents()
        modelToRender.put('measuredEvents', measuredEvents)

        // Browsers
        List<Browser> browsers = eventResultDashboardService.getAllBrowser()
        modelToRender.put('browsers', browsers)

        // Locations
        List<Location> locations = eventResultDashboardService.getAllLocations()
        modelToRender.put('locations', locations)

        // ConnectivityProfiles
        modelToRender['connectivityProfiles'] = eventResultDashboardService.getAllConnectivityProfiles()

        // JavaScript-Utility-Stuff:
        modelToRender.put("dateFormat", DATE_FORMAT_STRING_FOR_HIGH_CHART)
        modelToRender.put("weekStart", MONDAY_WEEKSTART)

        // --- Map<PageID, Set<MeasuredEventID>> for fast view filtering:
        Map<Long, Set<Long>> eventsOfPages = new HashMap<Long, Set<Long>>()
        for (Page eachPage : pages) {
            Set<Long> eventIds = new HashSet<Long>();

            Collection<Long> ids = measuredEvents.findResults {
                it.testedPage.getId() == eachPage.getId() ? it.getId() : null
            }
            if (!ids.isEmpty()) {
                eventIds.addAll(ids);
            }

            eventsOfPages.put(eachPage.getId(), eventIds);
        }
        modelToRender.put('eventsOfPages', eventsOfPages);

        // --- Map<BrowserID, Set<LocationID>> for fast view filtering:
        Map<Long, Set<Long>> locationsOfBrowsers = new HashMap<Long, Set<Long>>()
        for (Browser eachBrowser : browsers) {
            Set<Long> locationIds = new HashSet<Long>();

            Collection<Long> ids = locations.findResults {
                it.browser.getId() == eachBrowser.getId() ? it.getId() : null
            }
            if (!ids.isEmpty()) {
                locationIds.addAll(ids);
            }

            locationsOfBrowsers.put(eachBrowser.getId(), locationIds);
        }
        modelToRender.put('locationsOfBrowsers', locationsOfBrowsers);

        modelToRender.put('selectedAllMeasuredEvents', true)
        modelToRender.put('selectedAllBrowsers', true)
        modelToRender.put('selectedAllLocations', true)
        modelToRender.put('selectedAllConnectivityProfiles', true)
        modelToRender.put('selectedAggrGroupValuesUnCached', [])

        modelToRender.put("tagToJobGroupNameMap", jobGroupDaoService.getTagToJobGroupNameMap())

        // Done! :)
        return modelToRender;
    }

    /**
     * Rest Method for ajax call.
     * @param cmd The requested data.
     * @return BarchartDTO as JSON or string message if an error occurred
     */
    def getBarchartData(GetBarchartCommand cmd) {
        String errorMessages = getErrorMessages(cmd)
        if (errorMessages) {
            ControllerUtils.sendSimpleResponseAsStream(response, HttpStatus.BAD_REQUEST, errorMessages)
            return
        }

        List<JobGroup> allJobGroups = JobGroup.findAllByNameInList(cmd.selectedJobGroups)
        List<Page> allPages = Page.findAllByNameInList(cmd.selectedPages)

        def data = []

        List<String> allMeasurands = cmd.selectedSeries*.measurands.flatten()*.replace("Uncached", "")

        allJobGroups.each { currentJobGroup ->
            allPages.each { currentPage ->
                List<EventResult> eventResults = EventResult.findAllByDateCreatedBetweenAndJobGroupAndPage(cmd.from, cmd.to, currentJobGroup, currentPage)
                if (eventResults) {
                    Map<String, String> datum = ['jobGroup': currentJobGroup.name, 'page': currentPage.name]
                    allMeasurands.each { m ->
                        List allValues = eventResults*.getAt(m).findAll { it != null }
                        Double mAverage = allValues ? (allValues.sum { it } / allValues.size()) : null
                        datum.put(m, mAverage)
                    }
                    data.add(datum)
                }
            }
        }

        List allSeries = cmd.selectedSeries
        BarchartDTO barchartDTO = new BarchartDTO(groupingLabel: "Page / JobGroup")

        allSeries.each { series ->
            BarchartSeries barchartSeries = new BarchartSeries(dimensionalUnit: getDimensionalUnit(series.measurands[0]), stacked: series.stacked)
            series.measurands.each { currentMeasurand ->
                data.each { datum ->
                    barchartSeries.data.add(
                            new BarchartDatum(index: currentMeasurand.replace("Uncached", ""), indexValue: datum[currentMeasurand.replace("Uncached", "")], grouping: "${datum['page']} / ${datum['jobGroup']}"))
                }
            }

            barchartDTO.series.add(barchartSeries)
        }


        def result = data ? barchartDTO : [:]
        ControllerUtils.sendObjectAsJSON(response, result)
    }

    /**
     * Validates the command and creates an error message string if necessary.
     * @param cmd
     * @return a string containing the error messages in html format or an empty string if the command is valid
     */
    private String getErrorMessages(GetBarchartCommand cmd) {
        String result = ""
        if (!cmd.selectedPages) {
            result += i18nService.msg("de.iteratec.osm.gui.selectedPage.error.validator.error.selectedPage", "Please select at least one page")
            result += "<br />"
        }
        if (!cmd.selectedJobGroups) {
            result += i18nService.msg("de.iteratec.osm.gui.selectedFolder.error.validator.error.selectedFolder", "Please select at least one jobGroup")
            result += "<br />"
        }
        return result
    }

    /**
     * Returns the dimensional unit for the given measurand
     * @param measurand the {@link MeasurandGroup}
     * @return the dimensional unit as string
     */
    private String getDimensionalUnit(String measurand) {
        def aggregatorGroup = AGGREGATOR_GROUP_VALUES.get(CachedView.UNCACHED)
        if (aggregatorGroup.get(MeasurandGroup.LOAD_TIMES).contains(measurand)) {
            return i18nService.msg("de.iteratec.osm.measurandGroup.loadTimes.dimensionalUnit", "ms")
        } else if (aggregatorGroup.get(MeasurandGroup.PERCENTAGES).contains(measurand)) {
            return i18nService.msg("de.iteratec.osm.measurandGroup.percentages.dimensionalUnit", "percent %")
        } else if (aggregatorGroup.get(MeasurandGroup.REQUEST_COUNTS)) {
            return i18nService.msg("de.iteratec.osm.measurandGroup.requestCounts.dimensionalUnit", "count")
        } else if (aggregatorGroup.get(MeasurandGroup.REQUEST_SIZES)) {
            return i18nService.msg("de.iteratec.osm.measurandGroup.requestSize.dimensionalUnit", "kb")
        } else {
            return ""
        }

    }
}