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
package com.vangent.hieos.services.xcpd.gateway.serviceimpl;

import com.vangent.hieos.xutil.exception.SOAPFaultException;

import org.apache.log4j.Logger;

/**
 * Class to handle all asynchronous web service requests to XCPD Initiating Gateway (IG).
 *
 * @author Bernie Thuman
 */
public class XCPDInitiatingGatewayAsync extends XCPDInitiatingGateway {

    private final static Logger logger = Logger.getLogger(XCPDInitiatingGatewayAsync.class);

    /**
     * This method ensures that an asynchronous request has been sent. It evaluates the message
     * context to dtermine if "ReplyTo" is non-null and is not anonymous. It also ensures that
     * "MessageID" is non-null. It throws an exception if that is not the case.
     * @throws SOAPFaultException
     */
    @Override
    protected void validateWS() throws SOAPFaultException {
        validateAsyncWS();
    }
}
