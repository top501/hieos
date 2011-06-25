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

import com.vangent.hieos.hl7v3util.model.subject.CodedValue;
import com.vangent.hieos.services.xds.bridge.mapper.ContentParserConfig.ContentParserConfigName;
import com.vangent.hieos.services.xds.bridge.serviceimpl.XDSBridgeConfig;

/**
 * Class description
 *
 *
 * @version        v1.0, 2011-06-13
 * @author         Jim Horner
 */
public class MapperFactory {

    /** Field description */
    private final ContentParser contentParser;

    /** Field description */
    private final XDSBridgeConfig xdsbridgeConfig;

    /**
     * Constructs ...
     *
     *
     *
     *
     * @param bridgeConfig
     * @param tplGen
     */
    public MapperFactory(XDSBridgeConfig bridgeConfig, ContentParser tplGen) {

        super();
        this.xdsbridgeConfig = bridgeConfig;
        this.contentParser = tplGen;
    }

    /**
     * Method description
     *
     *
     *
     * @param cfg
     * @return
     */
    public CDAToXDSMapper createCDAToXDSMapper(ContentParserConfig cfg) {

        return new CDAToXDSMapper(this.contentParser, cfg);
    }

    /**
     * Method description
     *
     *
     * @param type
     *
     * @return
     */
    private ContentParserConfig findContentParserConfig(CodedValue type) {

        ContentParserConfig result = null;

        DocumentTypeMapping mapping =
            this.xdsbridgeConfig.findDocumentTypeMapping(type);

        if (mapping != null) {
            result = mapping.getContentParserConfig();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param type
     *
     * @return
     */
    public IXDSMapper getMapper(CodedValue type) {

        IXDSMapper result = null;

        ContentParserConfig parserConfig = findContentParserConfig(type);

        if (parserConfig != null) {

            if (ContentParserConfigName.CDAToXDSMapper
                    == parserConfig.getName()) {

                result = createCDAToXDSMapper(parserConfig);
            }
        }

        return result;
    }
}
