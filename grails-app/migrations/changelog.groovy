databaseChangeLog = {
    include file: '2015-11-18-SCHEME-initial-liquibase.groovy'
    include file: '2015-11-18-DATA-set-initial-csi-transformation.groovy'
    include file: '2015-11-18-DATA-multiply-csi-values-by-100.groovy'
    include file: '2015-11-26-SCHEME-optimizing-indices.groovy'
    include file: '2015-12-09-SCHEME-csi-configuration.groovy'
    include file: '2015-12-15-SCHEME-measured-value-and-connectivity-profile.groovy'
    include file: '2015-12-16-DATA-delete-invalid-default-csi-mappings.groovy'
    include file: '2015-12-22-DATA-delete-measured-value-update-events.groovy'
    include file: '2015-12-23-SCHEME-CsiDay-class-with-hoursOfDay.groovy'
    include file: '2015-12-23-SCHEME-replaced-hourOfDays-with-CsiDay-in-CsiConfiguration.groovy'
    include file: '2015-12-23-DATA-convert-hoursOfDay-to-CsiDay.groovy'
    include file: '2015-12-23-SCHEME-added-csiConfiguration-to-jobGroup.groovy'
    include file: '2016-01-04-DATA-create-initial-browser-connectivity-weights.groovy'
    include file: '2016-02-22-SCHEME-v340.groovy'
    include file: '2016-02-22-DATA-v340.groovy'
	include file: '2016-02-24-SCHEME-v341.groovy'
	include file: '2016-02-24-DATA-v341.groovy'
    include file: '2016-03-02-SCHEME-v346.groovy'
    include file: '2016-03-04-SCHEME-v347.groovy'
    include file: '2016-03-30-SCHEME-v348.groovy'
    include file: '2016-06-17-SCHEME-v349.groovy'
    include file: '2016-08-12-SCHEME-v350.groovy'
    include file: '2016-08-12-DATA-v350.groovy'
    include file: '2016-09-08-SCHEME-v352.groovy'
    include file: '2016-09-08-DATA-v352.groovy'
    include file: '2016-09-30-SCHEME-v354.groovy'
    include file: '2016-10-26-SCHEME-v355.groovy'
    include file: '2016-10-26-DATA-v355.groovy'
    include file: '2016-12-12-SCHEME-v400.groovy'
    include file: '2016-12-20-SCHEME-v410.groovy'
    include file: '2017-01-30-SCHEME-v411.groovy'
    include file: '2017-02-23-SCHEME-v412.groovy'
    include file: '2017-03-21-SCHEME-v413.groovy'
    include file: '2017-03-21-DATA-v413.groovy'
    include file: '2017-05-03-DATA-v414.groovy'
    include file: '2017-05-03-SCHEME-v414.groovy'
    include file: '2017-05-12-SCHEME-v414.groovy'
    include file: '2017-05-19-DATA-v414.groovy'
    include file: '2017-05-19-SCHEME-v414.groovy'
    include file: '2017-06-13-DATA-v420_beta.groovy'
    include file: '2017-06-16-SCHEME-v420_beta_2.groovy'
    include file: '2017-06-21-SCHEME-v420_beta_2.groovy'
    include file: '2017-06-21-DATA-v420_beta_2.groovy'
    include file: '2017-06-22-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-04-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-04-DATA-v420_beta_2.groovy'
    include file: '2017-07-05-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-10-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-12-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-18-DATA-v420_beta_2.groovy'
    include file: '2017-07-19-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-25-SCHEME-v420_beta_2.groovy'
    include file: '2017-07-27-SCHEME-v420_beta_2.groovy'
    include file: '2017-08-31-DATA-v430.groovy'
    include file: '2017-09-06-SCHEME-v431.groovy'
    include file: '2017-09-21-SCHEME-v432.groovy'
    include file: '2017-09-26-SCHEME-v433.groovy'
    include file: '2017-10-10-v440-fixGraphitePaths.groovy'
    include file: '2017-12-19-v440-improveTimeSeriesPerformance.groovy'
    include file: '2017-12-19-v440-addGlobalUAConfigColumn.groovy'
    include file: '2018-01-09-v440-addUseGlobalUASuffixBool.groovy'
    include file: '2018-01-17-v440-addThreshold.groovy'
    include file: '2018-02-20-v450-dropMainUrl.groovy'
    include file: '2018-06-20-v460-removeScriptPageRelations.groovy'
    include file: '2018-09-27-SCHEME-v510-remove-csi-by-rank.groovy'
    include file: '2018-10-01-v500-job-label-optional.groovy'
    include file: '2018-10-10-v500-locationHealthCheck-add-index.groovy'
    include file: '2018-10-11-v500-job-result-status.groovy'
    include file: '2018-11-19-v501-job-group-graphite-servers.groovy'
}
