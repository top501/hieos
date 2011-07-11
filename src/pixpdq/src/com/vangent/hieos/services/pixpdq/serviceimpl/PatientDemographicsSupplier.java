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
package com.vangent.hieos.services.pixpdq.serviceimpl;

import com.vangent.hieos.services.pixpdq.transactions.PDSRequestHandler;
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import com.vangent.hieos.xutil.xconfig.XConfigObject;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class PatientDemographicsSupplier extends PIXPDQServiceBaseImpl {

    private final static Logger logger = Logger.getLogger(PatientDemographicsSupplier.class);
    private static XConfigActor config = null;  // Singleton.

    @Override
    protected XConfigActor getConfigActor() {
        return config;
    }

    /**
     *
     * @param PRPA_IN201305UV02_Message
     * @return
     */
    public OMElement PatientRegistryFindCandidatesQuery(OMElement request) throws AxisFault {
        beginTransaction("FindCandidatesQuery (PDQV3)", request);
        validateWS();
        validateNoMTOM();
        PDSRequestHandler handler = new PDSRequestHandler(this.log_message);
        handler.setConfigActor(config);
        OMElement result = handler.run(request, PDSRequestHandler.MessageType.PatientRegistryFindCandidatesQuery);
        endTransaction(handler.getStatus());
        return result;
    }

    // BHT (ADDED Axis2 LifeCycle methods):
    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     */
    @Override
    public void startUp(ConfigurationContext configctx, AxisService service) {
        logger.info("PatientDemographicsSupplier::startUp()");
        try {
            XConfig xconf;
            xconf = XConfig.getInstance();
            XConfigObject homeCommunity = xconf.getHomeCommunityConfig();
            config = (XConfigActor) homeCommunity.getXConfigObjectWithName("pds", XConfig.PDS_TYPE);
        } catch (Exception ex) {
            logger.fatal("Unable to get configuration for service", ex);
        }
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     */
    @Override
    public void shutDown(ConfigurationContext configctx, AxisService service) {
        logger.info("PatientDemographicsSupplier::shutDown()");
        /*if (service.getParameterValue("ActorName").equals("InitiatingGateway")) {
        this.ATNAlogStop(XATNALogger.ActorType.INITIATING_GATEWAY);
        } else {
        this.ATNAlogStop(XATNALogger.ActorType.RESPONDING_GATEWAY);
        }*/
    }
}
