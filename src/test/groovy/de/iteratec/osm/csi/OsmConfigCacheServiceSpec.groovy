/* 
* OpenSpeedMonitor (OSM)
* Copyright 2014 iteratec GmbH
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
* 	http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License.
*/

package de.iteratec.osm.csi

import de.iteratec.osm.ConfigService
import de.iteratec.osm.OsmConfigCacheService
import de.iteratec.osm.OsmConfiguration
import grails.buildtestdata.BuildDataTest
import grails.buildtestdata.mixin.Build
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

import static de.iteratec.osm.OsmConfiguration.DEFAULT_MIN_VALID_LOADTIME
import static de.iteratec.osm.OsmConfiguration.DEFAULT_MAX_VALID_LOADTIME

@Build([OsmConfiguration])
class OsmConfigCacheServiceSpec extends Specification implements BuildDataTest, ServiceUnitTest<OsmConfigCacheService> {

    Closure doWithSpring() {
        return {
            configService(ConfigService)
        }
    }

    void setup() {
        OsmConfiguration.build()
    }

    void setupSpec() {
        mockDomain(OsmConfiguration)
    }

    void "test accessing cached configs min doc complete time"() {
        when: "getting cached min doc complete time"
            Integer time = service.getMinValidLoadtime(24)
        then: "the result should be 10ms"
            time == DEFAULT_MIN_VALID_LOADTIME
    }

    void "test accessing cached configs max doc complete time"() {
        when: "getting cached max doc complete time"
            Integer time = service.getMaxValidLoadtime(24)
        then: "the result should be 3m"
            time == DEFAULT_MAX_VALID_LOADTIME
    }
}
