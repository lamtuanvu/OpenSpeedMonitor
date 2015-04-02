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

package de.iteratec.osm.batch

import grails.converters.JSON

/**
 * BatchActivityController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class BatchActivityController {

    BatchActivityService batchActivityService

    def list(){
        params.order = params.order?:"desc"
        params.sort = params.sort?:"startDate"
        [batchActivities:BatchActivity.list(params), batchActivityCount:BatchActivity.count()]
    }

    def index(){
        redirect(action: 'list',params: [max:10])
    }

    def edit(){
        [batchActivityInstance:  BatchActivity.get(params.id)]
    }

    def show(){
        [batchActivityInstance: BatchActivity.get(params.id)]
    }

    def delete(){
        BatchActivity activity = BatchActivity.get(params.id)
        activity.delete(flush: true)
        redirect(action: 'list')
    }

    /**
     *
     * @return new Content from BatchActivityTable
     */
    def updateTable(){
        params.order = "desc"
        params.sort = "startDate"
        params.max = 10
        render(view: '_batchActivityTable',model: [batchActivities:  BatchActivity.list(params),batchActivityCount:BatchActivity.count()])
    }
    /**
     *
     * @param id id of the BatchActivity to collect
     * @param evenOdd even or odd to get the right row highlighting
     * @return BatchActivity row of the given id
     */
    def getUpdate(int id, String evenOdd){
        render(view: '_batchActivityRow', model:[batchActivityInstance: BatchActivity.get(id), evenOdd:evenOdd])
    }

    def checkForUpdate(){
        if(BatchActivity.findByStatus(Status.ACTIVE)){
            render("true")
        } else{
            render("false")
        }
    }
}