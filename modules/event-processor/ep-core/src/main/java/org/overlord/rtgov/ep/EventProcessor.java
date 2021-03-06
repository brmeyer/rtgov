/*
 * 2012-3 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.rtgov.ep;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.overlord.rtgov.common.service.Service;

/**
 * This interface defines an event processor responsible
 * for processing events, and where appropriate, forwarding
 * results to other awaiting event processors.
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class EventProcessor {
    
    private java.util.Map<String,Service> _services=
                            new java.util.HashMap<String,Service>();
    
    /**
     * This method returns the map of names to services.
     * 
     * @return The services
     */
    public java.util.Map<String,Service> getServices() {
        return (_services);
    }
    
    /**
     * This method sets the map of names to services.
     * 
     * @param services The services
     */
    public void setServices(java.util.Map<String,Service> services) {
        _services = services;
    }
    
    /**
     * This method initializes the event processor.
     * 
     * @throws Exception Failed to initialize
     */
    public void init() throws Exception {
        
        // Iterate through the services initializing them
        for (Service service : _services.values()) {
            service.init();
        }
    }
    
    /**
     * This method processes the supplied event, and optionally
     * returns a transformed representation to be forwarded to
     * other processors. If multiple objects result from the,
     * transformation, then they should be returned in a collection.
     * <p>
     * If the event cannot be processed at
     * this time, then an exception should be thrown to initiate
     * a retry. The number of remaining retries is supplied,
     * to enable the processor to take appropriate error
     * reporting action.
     *
     * @param source The source event processor name that generated the event
     * @param event The event to process
     * @param retriesLeft The number of retries left
     * @return The optional transformed representation of the event
     * @throws Exception Failed to process event, requesting retry
     */
    public abstract java.io.Serializable process(String source,
                java.io.Serializable event, int retriesLeft) throws Exception;

}
