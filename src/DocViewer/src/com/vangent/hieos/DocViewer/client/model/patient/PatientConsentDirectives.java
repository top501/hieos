/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2013 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vangent.hieos.DocViewer.client.model.patient;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Bernie Thuman
 *
 */
public class PatientConsentDirectives implements IsSerializable {
	private boolean active;
	private String patientID;
	private List<PatientConsentRule> patientConsentRules = new ArrayList<PatientConsentRule>();

	/**
	 * 
	 * @param active
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * 
	 * @return
	 */
	public String getPatientID() {
		return patientID;
	}

	/**
	 * 
	 * @param patientID
	 */
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PatientConsentRule> getPatientConsentRules()
	{
		return this.patientConsentRules;
	}
	
	/**
	 * 
	 * @param patientConsentRule
	 */
	public void add(PatientConsentRule patientConsentRule)
	{
		this.patientConsentRules.add(patientConsentRule);
	}
}