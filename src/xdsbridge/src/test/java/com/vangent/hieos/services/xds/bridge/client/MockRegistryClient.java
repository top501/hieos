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

import com.vangent.hieos.services.xds.bridge.model.PatientIdentityFeed;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

/**
 * Class description
 *
 *
 * @version        v1.0, 2011-06-22
 * @author         Jim Horner
 */
public class MockRegistryClient extends XDSDocumentRegistryClient {

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
    public MockRegistryClient(boolean throwsAlways, boolean throwsEven,
                              OMElement response) {

        super(null);
        this.throwsExceptionAlways = throwsAlways;
        this.throwsExceptionOnEven = throwsEven;
        this.response = response;
        this.count = 0;
    }

    /**
     * Method description
     *
     *
     * @param pif
     *
     * @return
     *
     * @throws AxisFault
     */
    @Override
    public OMElement sendPatientIdentity(PatientIdentityFeed pif)
            throws AxisFault {

        if (this.throwsExceptionAlways) {

            throw new AxisFault("MockRegistryClient thows exception always.");

        } else if (this.throwsExceptionOnEven) {

            if ((this.count % 2) == 0) {

                String msg =
                    String.format(
                        "Document %d: MockRegistryClient throws even exception.",
                        this.count);

                this.count++;

                throw new AxisFault(msg);
            }

            this.count++;
        }

        return this.response;
    }
}
