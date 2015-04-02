package de.iteratec.osm.persistence

import de.iteratec.osm.ConfigService
import org.joda.time.DateTime


class DbCleanupOldMeasuredValuesWithDependenciesJob {

    DbCleanupService dbCleanupService
    ConfigService configService

    static triggers = {
        /**
         * Each Day at 3:00 am.
         */
        cron(name: 'DailyOldMeasuredValuesWithDependenciesCleanup', cronExpression: '0 0 3 ? * *')
    }

    def execute() {
        if(configService.isDatabaseCleanupEnabled() > 0 && configService.areMeasurementsGenerallyEnabled()) {
            Date toDeleteResultsBefore = new DateTime().minusMonths(configService.getMaxDataStorageTimeInMonths()).toDate()
            dbCleanupService.deleteMeasuredValuesAndMeasuredValueUpdateEventsBefore(toDeleteResultsBefore)
        }
    }
}