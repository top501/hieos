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
package com.vangent.hieos.policyutil.pip.model;

import com.vangent.hieos.xutil.xml.OMElementNodeWrapper;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
public class PIPRequestElement extends OMElementNodeWrapper {

    /**
     * 
     * @param element
     */
    public PIPRequestElement(OMElement element) {
        super(element, "PIPRequest");
    }
}
