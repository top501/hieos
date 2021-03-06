/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2011 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.vangent.hieos.services.xds.bridge.client;

import com.vangent.hieos.subjectmodel.DeviceInfo;
import com.vangent.hieos.services.xds.bridge.message.XDSPnRMessage;
import com.vangent.hieos.services.xds.bridge.mock.MockXConfigTransaction;
import com.vangent.hieos.xutil.exception.SOAPFaultException;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import com.vangent.hieos.xutil.xconfig.XConfigTransaction;

import org.apache.axiom.om.OMElement;


/**
 * Class description
 *
 *
 * @version        v1.0, 2011-06-22
 * @author         Jim Horner
 */
public class MockRepositoryClient extends XDSDocumentRepositoryClient {

    /** Field description */
    public int count;

    /** Field description */
    public final OMElement response;

    /** Field description */
    public final boolean throwsExceptionAlways;

    /** Field description */
    public final boolean throwsExceptionOnEven;

    /**
     * Constructs ...
     *
     *
     * @param throwsAlways
     * @param throwsEven
     * @param response
     */
    public MockRepositoryClient(boolean throwsAlways, boolean throwsEven,
                                OMElement response) {

        super(null, createConfigActor());
        this.throwsExceptionAlways = throwsAlways;
        this.throwsExceptionOnEven = throwsEven;
        this.response = response;
        this.count = 0;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    private static XConfigActor createConfigActor() {

        XConfigActor result = new XConfigActor();
        XConfigTransaction trans = new MockXConfigTransaction(PNR_TRANS);

        result.getTransactions().add(trans);

        return result;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public DeviceInfo createReceiverDeviceInfo() {

        DeviceInfo result = new DeviceInfo();

        result.setId("TEST");
        result.setName("TEST-Receiver");

        return result;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public DeviceInfo createSenderDeviceInfo() {

        DeviceInfo result = new DeviceInfo();

        result.setId("TEST");
        result.setName("TEST-Sender");

        return result;
    }

    /**
     * Method description
     *
     *
     * @param request
     *
     * @return
     *
     */
    @Override
    public OMElement submitProvideAndRegisterDocumentSet(XDSPnRMessage request)
            throws SOAPFaultException {

        if (this.throwsExceptionAlways) {

            throw new SOAPFaultException(
                "MockRepositoryClient thows exception always.");

        } else if (this.throwsExceptionOnEven) {

            if ((this.count % 2) == 0) {

                String msg =
                    String.format(
                        "Document %d: MockRepositoryClient throws even exception.",
                        this.count);

                this.count++;

                throw new SOAPFaultException(msg);
            }

            this.count++;
        }

        return this.response;
    }
}
