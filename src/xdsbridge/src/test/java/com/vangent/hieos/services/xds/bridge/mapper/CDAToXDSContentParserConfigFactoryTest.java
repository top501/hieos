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

package com.vangent.hieos.services.xds.bridge.mapper;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Class description
 *
 *
 * @version        v1.0, 2011-06-10
 * @author         Jim Horner
 */
public class CDAToXDSContentParserConfigFactoryTest {

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void createExpressionMapTest() throws Exception {

        ContentParserConfig config =
            CDAToXDSContentParserConfigFactory.createConfig();

        Map<String, String> result = config.getExpressionMap();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        assertEquals(
            "/ns:ClinicalDocument/ns:author/ns:assignedAuthor/ns:representedOrganization/ext:asEntityIdentifier/ext:id/@root",
            result.get(ContentVariableName.AuthorInstitutionRoot.toString()));

        Map<String, String> ns = config.getNamespaces();

        assertEquals("urn:hl7-org:v3", ns.get("ns"));
        assertEquals("http://ns.electronichealth.net.au/Ci/Cda/Extensions/3.0",
                     ns.get("ext"));
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void toPrefixURIArrayTest() throws Exception {

        ContentParserConfig config =
            CDAToXDSContentParserConfigFactory.createConfig();

        Map<String, String> result = config.getExpressionMap();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        String[][] prefixURIArray = config.toPrefixURIArray();

        assertNotNull(prefixURIArray);
        assertEquals(2, prefixURIArray.length);

        String[] prefixes = prefixURIArray[0];

        assertNotNull(prefixes);
        assertTrue(prefixes.length > 1);

        // find the ns prefix
        int nsidx = -1;

        for (int i = 0; i < prefixes.length; ++i) {

            if ("ns".equals(prefixes[i])) {

                nsidx = i;

                break;
            }
        }

        assertFalse("Unable to find ns prefix.", nsidx == -1);

        String[] uris = prefixURIArray[1];

        assertNotNull(uris);
        assertTrue(uris.length > 1);

        assertEquals("urn:hl7-org:v3", uris[nsidx]);
    }
}