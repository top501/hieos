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
package com.vangent.hieos.empi.impl.base;

import com.vangent.hieos.empi.exception.EMPIException;
import com.vangent.hieos.empi.persistence.PersistenceManager;
import com.vangent.hieos.hl7v3util.model.subject.DeviceInfo;
import com.vangent.hieos.hl7v3util.model.subject.Subject;
import com.vangent.hieos.hl7v3util.model.subject.SubjectIdentifier;
import com.vangent.hieos.hl7v3util.model.subject.SubjectMergeRequest;
import com.vangent.hieos.empi.adapter.EMPINotification;
import com.vangent.hieos.empi.validator.MergeSubjectsValidator;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class MergeSubjectsHandler extends BaseHandler {

    private static final Logger logger = Logger.getLogger(MergeSubjectsHandler.class);

    /**
     *
     * @param configActor
     * @param persistenceManager
     * @param senderDeviceInfo
     */
    public MergeSubjectsHandler(XConfigActor configActor, PersistenceManager persistenceManager, DeviceInfo senderDeviceInfo) {
        super(configActor, persistenceManager, senderDeviceInfo);
    }

    /**
     *
     * @param subjectMergeRequest
     * @return
     * @throws EMPIException
     */
    public EMPINotification mergeSubjects(SubjectMergeRequest subjectMergeRequest) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();

        // First, run validations on input.
        MergeSubjectsValidator validator = new MergeSubjectsValidator(pm, this.getSenderDeviceInfo());
        validator.validate(subjectMergeRequest);

        EMPINotification notification = new EMPINotification();

        Subject survivingSubject = subjectMergeRequest.getSurvivingSubject();
        Subject subsumedSubject = subjectMergeRequest.getSubsumedSubject();

        // Lookup surviving and subsumed subjects.
        Subject baseSurvivingSubject = this.getBaseSubject(survivingSubject, "surviving");
        Subject baseSubsumedSubject = this.getBaseSubject(subsumedSubject, "subsumed");

        if (baseSurvivingSubject.getType().equals(Subject.SubjectType.SYSTEM)
                && baseSubsumedSubject.getType().equals(Subject.SubjectType.SYSTEM)) {
            // Both are system-level subjects.
            String survivingSubjectSystemSubjectId = baseSurvivingSubject.getInternalId();
            String subsumedSubjectSystemSubjectId = baseSubsumedSubject.getInternalId();

            // See if this is referencing the same system-level subject.
            if (survivingSubjectSystemSubjectId.equals(subsumedSubjectSystemSubjectId)) {
                // Get all identifiers for the system-subject.
                List<SubjectIdentifier> subjectIdentifiers = pm.loadSubjectIdentifiers(subsumedSubjectSystemSubjectId);
                if (subjectIdentifiers.size() == 1) {
                    // This case should never happen given prior validation checks.
                    // FIXME????
                    throw new EMPIException("Should never happen - skipping merge");
                }
                // Now, remove the "subsumed" identifier.
                // Find the identifier to remove.
                SubjectIdentifier subsumedSubjectIdentifierToRemove =
                        this.findSubjectIdentifier(subsumedSubject.getSubjectIdentifiers().get(0), subjectIdentifiers);

                // Delete the identifier.
                pm.deleteSubjectIdentifier(subsumedSubjectIdentifierToRemove.getInternalId());

                // FIXME: Should we even allow this case (see above).
                // FIXME: Do update notification.
            } else {

                // Get base enterprise subjects.
                String baseEnterpriseSurvivingSubjectId = pm.getEnterpriseSubjectId(baseSurvivingSubject);
                String baseEnterpriseSubsumedSubjectId = pm.getEnterpriseSubjectId(baseSubsumedSubject);

                // Delete the "subsumed" system-level subject.
                pm.deleteSubject(baseSubsumedSubject);

                // FIXME: MAKE CONFIGURABLE!!!

                // Now move all cross references.
                pm.mergeEnterpriseSubjects(baseEnterpriseSurvivingSubjectId, baseEnterpriseSubsumedSubjectId);
                this.addSubjectToNotification(notification, baseEnterpriseSurvivingSubjectId);
            }
        } else if (baseSurvivingSubject.getType().equals(Subject.SubjectType.ENTERPRISE)
                && baseSubsumedSubject.getType().equals(Subject.SubjectType.ENTERPRISE)) {
            // Both are enterprise-level subjects.
            // Now move all cross references.
            String baseEnterpriseSurvivingSubjectId = baseSurvivingSubject.getInternalId();
            String baseEnterpriseSubsumedSubjectId = baseSubsumedSubject.getInternalId();
            pm.mergeEnterpriseSubjects(baseEnterpriseSurvivingSubjectId, baseEnterpriseSubsumedSubjectId);
            this.addSubjectToNotification(notification, baseEnterpriseSurvivingSubjectId);
        }

        // FIXME: Complete ALL cases.
        return notification;
    }

    /**
     *
     * @param searchSubjectIdentifier
     * @param subjectIdentifiers
     * @return
     */
    private SubjectIdentifier findSubjectIdentifier(SubjectIdentifier searchSubjectIdentifier, List<SubjectIdentifier> subjectIdentifiers) {
        // TBD: Move this method ...
        for (SubjectIdentifier subjectIdentifier : subjectIdentifiers) {
            if (subjectIdentifier.equals(searchSubjectIdentifier)) {
                return subjectIdentifier;
            }
        }
        return null;
    }

    /**
     *
     * @param subject
     * @param subjectType
     * @return
     * @throws EMPIException
     */
    private Subject getBaseSubject(Subject subject, String subjectType) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        List<SubjectIdentifier> subjectIdentifiers = subject.getSubjectIdentifiers();
        SubjectIdentifier subjectIdentifier = subjectIdentifiers.get(0);
        Subject baseMergeSubject = pm.loadBaseSubjectByIdentifier(subjectIdentifier);
        if (baseMergeSubject == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(subjectType).append(" subject not found - skipping merge");
            throw new EMPIException(sb.toString());
        }
        return baseMergeSubject;
    }
}