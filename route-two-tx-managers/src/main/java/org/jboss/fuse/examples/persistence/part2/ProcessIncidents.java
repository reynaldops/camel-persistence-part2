/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package org.jboss.fuse.examples.persistence.part2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.jboss.fuse.examples.persistence.part2.dao.IncidentDAO;
import org.jboss.fuse.examples.persistence.part2.model.Incident;

public class ProcessIncidents {

    private IncidentDAO incidentDAO;

    public Incident extract(Exchange exchange) throws ParseException {
        Map<String, Object> model = (Map<String, Object>) exchange.getIn().getBody(Map.class);
        String key = "org.jboss.fuse.examples.persistence.part2.model.Incident";

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = format.format(new Date());
        Date creationDate = format.parse(currentDate);

        Incident incident = (Incident) model.get(key);
        incident.setCreationDate(creationDate);
        incident.setCreationUser("file");

        return incident;
    }

    public void saveReport(@Body Incident incident) {
        incidentDAO.saveIncident(incident);
    }

    public void generateError() throws Exception {
        Thread.sleep(2000);
        // and now generate an exception to rollback TX
        throw new Exception("%%% Database has crashed ....");
    }

    public IncidentDAO getIncidentDAO() {
        return incidentDAO;
    }

    public void setIncidentDAO(IncidentDAO incidentDAO) {
        this.incidentDAO = incidentDAO;
    }

}
