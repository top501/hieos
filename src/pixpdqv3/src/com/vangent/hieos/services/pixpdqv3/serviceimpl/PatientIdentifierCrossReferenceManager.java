/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2010 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.services.pixpdqv3.serviceimpl;

import com.vangent.hieos.services.pixpdqv3.transactions.PIXRequestHandler;
import com.vangent.hieos.xutil.atna.ATNAAuditEvent;
import com.vangent.hieos.xutil.exception.SOAPFaultException;
import com.vangent.hieos.xutil.services.framework.XAbstractService;
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import com.vangent.hieos.xutil.xconfig.XConfigObject;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class PatientIdentifierCrossReferenceManager extends XAbstractService {

    private final static Logger logger = Logger.getLogger(PatientIdentifierCrossReferenceManager.class);
    private static XConfigActor config = null;  // Singleton.

    @Override
    protected XConfigActor getConfigActor() {
        return config;
    }

    /**
     *
     * @param request
     * @return
     * @throws AxisFault
     */
    public OMElement PatientRegistryGetIdentifiersQuery(OMElement request) throws AxisFault {
        OMElement response = null;
        try {
            long start = System.currentTimeMillis();
            beginTransaction("GetIdentifiersQuery (PIXV3)", request);
            validateWS();
            validateNoMTOM();
            PIXRequestHandler handler = new PIXRequestHandler(this.log_message);
            handler.setConfigActor(config);
            response = handler.run(request, PIXRequestHandler.MessageType.PatientRegistryGetIdentifiersQuery);
            endTransaction(handler.getStatus());
            if (logger.isDebugEnabled()) {
                logger.debug("PIXv3 Query TOTAL TIME - " + (System.currentTimeMillis() - start) + "ms.");
            }
        } catch (SOAPFaultException ex) {
            throwAxisFault(ex);
        }
        return response;

    }

    /**
     * 
     * @param PRPA_IN201301UV02_Message
     * @return
     * @throws AxisFault
     */
    public OMElement PatientRegistryRecordAdded(OMElement PRPA_IN201301UV02_Message) throws AxisFault {
        OMElement response = null;
        try {
            long start = System.currentTimeMillis();
            beginTransaction("PIDFEED.Add (PIXV3)", PRPA_IN201301UV02_Message);
            validateWS();
            validateNoMTOM();
            PIXRequestHandler handler = new PIXRequestHandler(this.log_message);
            handler.setConfigActor(config);
            response = handler.run(PRPA_IN201301UV02_Message, PIXRequestHandler.MessageType.PatientRegistryRecordAdded);
            endTransaction(handler.getStatus());
            if (logger.isDebugEnabled()) {
                logger.debug("PIXv3 Add TOTAL TIME - " + (System.currentTimeMillis() - start) + "ms.");
            }
        } catch (SOAPFaultException ex) {
            throwAxisFault(ex);
        }
        return response;
    }

    /**
     *
     * @param PRPA_IN201302UV02_Message
     * @return
     * @throws AxisFault
     */
    public OMElement PatientRegistryRecordRevised(OMElement PRPA_IN201302UV02_Message) throws AxisFault {
        OMElement response = null;
        try {
            long start = System.currentTimeMillis();
            beginTransaction("PIDFEED.Update (PIXV3)", PRPA_IN201302UV02_Message);
            validateWS();
            validateNoMTOM();
            PIXRequestHandler handler = new PIXRequestHandler(this.log_message);
            handler.setConfigActor(config);
            response = handler.run(PRPA_IN201302UV02_Message, PIXRequestHandler.MessageType.PatientRegistryRecordRevised);
            endTransaction(handler.getStatus());
            if (logger.isDebugEnabled()) {
                logger.debug("PIXv3 Update TOTAL TIME - " + (System.currentTimeMillis() - start) + "ms.");
            }
        } catch (SOAPFaultException ex) {
            throwAxisFault(ex);
        }
        return response;
    }

    /**
     *
     * @param PRPA_IN201304UV02_Message
     * @return
     * @throws AxisFault
     */
    public OMElement PatientRegistryDuplicatesResolved(OMElement PRPA_IN201304UV02_Message) throws AxisFault {
        OMElement response = null;
        try {
            long start = System.currentTimeMillis();
            beginTransaction("PIDFEED.Merge (PIXV3)", PRPA_IN201304UV02_Message);
            validateWS();
            validateNoMTOM();
            PIXRequestHandler handler = new PIXRequestHandler(this.log_message);
            handler.setConfigActor(config);
            response = handler.run(PRPA_IN201304UV02_Message, PIXRequestHandler.MessageType.PatientRegistryDuplicatesResolved);
            endTransaction(handler.getStatus());
            if (logger.isDebugEnabled()) {
                logger.debug("PIXv3 Merge TOTAL TIME - " + (System.currentTimeMillis() - start) + "ms.");
            }
        } catch (SOAPFaultException ex) {
            throwAxisFault(ex);
        }
        return response;
    }

    // BHT (ADDED Axis2 LifeCycle methods):
    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     */
    @Override
    public void startup() {
        logger.info("PatientIdentifierCrossReferenceManager::startup()");
        try {
            XConfig xconf;
            xconf = XConfig.getInstance();
            XConfigObject homeCommunity = xconf.getHomeCommunityConfig();
            config = (XConfigActor) homeCommunity.getXConfigObjectWithName("pix", XConfig.PIX_MANAGER_TYPE);
        } catch (Exception ex) {
            logger.fatal("Unable to get configuration for service", ex);
        }
        this.ATNAlogStart(ATNAAuditEvent.ActorType.PIX_MANAGER);
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     */
    @Override
    public void shutdown() {
        logger.info("PatientIdentifierCrossReferenceManager::shutdown()");
        this.ATNAlogStop(ATNAAuditEvent.ActorType.PIX_MANAGER);
    }
}
