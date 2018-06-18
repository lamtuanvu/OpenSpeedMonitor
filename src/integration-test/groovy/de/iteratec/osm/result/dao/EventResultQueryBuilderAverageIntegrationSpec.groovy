package de.iteratec.osm.result.dao

import de.iteratec.osm.csi.NonTransactionalIntegrationSpec
import de.iteratec.osm.csi.Page
import de.iteratec.osm.measurement.schedule.JobGroup
import de.iteratec.osm.result.*
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback

@Integration
@Rollback
class EventResultQueryBuilderAverageIntegrationSpec extends NonTransactionalIntegrationSpec {

    JobGroup jobGroup1, jobGroup2, jobGroup3
    Page page1, page2, page3

    void "check average for measurands with page"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            jobGroup1 = JobGroup.build()
            jobGroup2 = JobGroup.build()
            jobGroup3 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 600,
                    medianValue: true,
            )

            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                )
            }
            session.flush()
        }

        when: "the builder is configured for measurand and page"
        SelectedMeasurand selectedMeasurand = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withPageIdsIn([page1.id])
                .withSelectedMeasurands([selectedMeasurand])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.fullyLoadedTimeInMillisecs == 300 &&
                    it.pageId == page1.id
        }
    }

    void "check average for userTimings with page"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            jobGroup1 = JobGroup.build()
            jobGroup2 = JobGroup.build()
            jobGroup3 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(600), type: UserTimingType.MEASURE)]
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                        userTimings: [UserTiming.build(name: "mark1", duration: new Double(500), type: UserTimingType.MEASURE)]
                )
            }
            session.flush()
        }

        when: "the builder is configured for usertiming and page"
        SelectedMeasurand selectedMeasurand = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withPageIdsIn([page1.id])
                .withSelectedMeasurands([selectedMeasurand])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.mark1 == 300 &&
                    it.pageId == page1.id
        }
    }

    void "check average for measurands and userTimings with page"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            jobGroup1 = JobGroup.build()
            jobGroup2 = JobGroup.build()
            jobGroup3 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 600,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(600), type: UserTimingType.MEASURE)]
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                        userTimings: [UserTiming.build(name: "mark1", duration: new Double(500), type: UserTimingType.MEASURE)]
                )
            }
            session.flush()
        }

        when: "the builder is configured for usertiming and measurand with page"
        SelectedMeasurand selectedMeasurand1 = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        SelectedMeasurand selectedMeasurand2 = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withPageIdsIn([page1.id])
                .withSelectedMeasurands([selectedMeasurand1, selectedMeasurand2])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.mark1 == 300 &&
                    it.fullyLoadedTimeInMillisecs == 300 &&
                    it.pageId == page1.id
        }
    }

    void "check average for measurands with jobGroup"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            page2 = Page.build()
            page3 = Page.build()
            jobGroup1 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
            )
            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
            )
            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 600,
                    medianValue: true,
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                )
            }
            session.flush()
        }

        when: "the builder is configured for measurand and jobGroup"
        SelectedMeasurand selectedMeasurand = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withJobGroupIdsIn([jobGroup1.id])
                .withSelectedMeasurands([selectedMeasurand])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.fullyLoadedTimeInMillisecs == 300 &&
                    it.jobGroupId == jobGroup1.id
        }
    }

    void "check average for userTimings with jobGroup"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            page2 = Page.build()
            page3 = Page.build()
            jobGroup1 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )

            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 500,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(600), type: UserTimingType.MEASURE)]
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                        userTimings: [UserTiming.build(name: "mark1", duration: new Double(500), type: UserTimingType.MEASURE)]
                )
            }
            session.flush()
        }

        when: "the builder is configured for usertiming and jobGroup"
        SelectedMeasurand selectedMeasurand = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withJobGroupIdsIn([jobGroup1.id])
                .withSelectedMeasurands([selectedMeasurand])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.mark1 == 300 &&
                    it.jobGroupId == jobGroup1.id
        }
    }

    void "check average for measurands and userTimings with jobGroup"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            page2 = Page.build()
            page3 = Page.build()
            jobGroup1 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 600,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(600), type: UserTimingType.MEASURE)]
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                        userTimings: [UserTiming.build(name: "mark1", duration: new Double(500), type: UserTimingType.MEASURE)]
                )
            }
            session.flush()
        }

        when: "the builder is configured for usertiming and measurand with jobGroup"
        SelectedMeasurand selectedMeasurand1 = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        SelectedMeasurand selectedMeasurand2 = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withJobGroupIdsIn([jobGroup1.id])
                .withSelectedMeasurands([selectedMeasurand1, selectedMeasurand2])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.mark1 == 300 &&
                    it.fullyLoadedTimeInMillisecs == 300 &&
                    it.jobGroupId == jobGroup1.id
        }
    }

    void "check average for measurand and usertiming with page and jobGroup"() {
        given: "three matching and two other Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            jobGroup1 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 600,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(600), type: UserTimingType.MEASURE)]
            )
            2.times {
                EventResult.build(
                        fullyLoadedTimeInMillisecs: 500,
                        medianValue: true,
                        userTimings: [UserTiming.build(name: "mark1", duration: new Double(500), type: UserTimingType.MEASURE)]
                )
            }
            session.flush()
        }

        when: "the builder is configured for usertiming and measurand with page and jobGroup"
        SelectedMeasurand selectedMeasurand1 = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        SelectedMeasurand selectedMeasurand2 = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withPageIdsIn([page1.id])
                .withJobGroupIdsIn([jobGroup1.id])
                .withSelectedMeasurands([selectedMeasurand1, selectedMeasurand2])
                .getAverageData()

        then: "only one aggregation is returned"
        result.size() == 1
        result.every {
            it.mark1 == 300 &&
                    it.fullyLoadedTimeInMillisecs == 300 &&
                    it.jobGroupId == jobGroup1.id &&
                    it.pageId == page1.id
        }
    }

    void "check average for measurand and usertiming with page and jobGroup without aggregation"() {
        given: "nine different but matching Eventresults"
        EventResult.withNewSession { session ->
            page1 = Page.build()
            page2 = Page.build()
            page3 = Page.build()
            jobGroup1 = JobGroup.build()
            jobGroup2 = JobGroup.build()
            jobGroup3 = JobGroup.build()

            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page1,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 300,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(300), type: UserTimingType.MEASURE)]
            )

            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page2,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 300,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(300), type: UserTimingType.MEASURE)]
            )

            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup1,
                    fullyLoadedTimeInMillisecs: 100,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(100), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup2,
                    fullyLoadedTimeInMillisecs: 200,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(200), type: UserTimingType.MEASURE)]
            )
            EventResult.build(
                    page: page3,
                    jobGroup: jobGroup3,
                    fullyLoadedTimeInMillisecs: 300,
                    medianValue: true,
                    userTimings: [UserTiming.build(name: "mark1", duration: new Double(300), type: UserTimingType.MEASURE)]
            )
            session.flush()
        }


        when: "the builder is configured for usertiming and measurand with all pages and jobGroups"
        SelectedMeasurand selectedMeasurand1 = new SelectedMeasurand("_UTME_mark1", CachedView.UNCACHED)
        SelectedMeasurand selectedMeasurand2 = new SelectedMeasurand("FULLY_LOADED_TIME", CachedView.UNCACHED)
        def result = new EventResultQueryBuilder(0, 1000)
                .withPageIdsIn([page1.id, page2.id, page3.id])
                .withJobGroupIdsIn([jobGroup1.id, jobGroup2.id, jobGroup3.id])
                .withSelectedMeasurands([selectedMeasurand1, selectedMeasurand2])
                .getAverageData()

        then: "nine aggregations are returned"
        result.size() == 9
    }
}
