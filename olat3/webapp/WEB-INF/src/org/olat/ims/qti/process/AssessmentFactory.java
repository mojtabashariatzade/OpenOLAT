/**
* OLAT - Online Learning and Training<br>
* http://www.olat.org
* <p>
* Licensed under the Apache License, Version 2.0 (the "License"); <br>
* you may not use this file except in compliance with the License.<br>
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing,<br>
* software distributed under the License is distributed on an "AS IS" BASIS, <br>
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
* See the License for the specific language governing permissions and <br>
* limitations under the License.
* <p>
* Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
* University of Zurich, Switzerland.
* <p>
*/ 

package org.olat.ims.qti.process;

import org.olat.core.id.Identity;
import org.olat.core.logging.Tracing;
import org.olat.core.util.CodeHelper;
import org.olat.course.nodes.iq.IQEditController;
import org.olat.modules.ModuleConfiguration;
import org.olat.repository.RepositoryEntry;
import org.olat.repository.RepositoryManager;

/**
 * @author Felix Jost
 */
public class AssessmentFactory {

	/**
	 * Create an assessment instance from a Repository resource referenced by repoPointer.
	 * 
	 * @param subj
	 * @param resourcePathInfo
	 * @return
	 */
	public static AssessmentInstance createAssessmentInstance(Identity subj, ModuleConfiguration modConfig, boolean preview, String resourcePathInfo) {
		AssessmentInstance ai = null;
		Persister persister = null;

		String repositorySoftkey = (String)modConfig.get(IQEditController.CONFIG_KEY_REPOSITORY_SOFTKEY);
		RepositoryEntry re = RepositoryManager.getInstance().lookupRepositoryEntryBySoftkey(repositorySoftkey, true);
		if (re == null) return null;
		
		if (!preview) {
			// try to resume the assessment instance 
			persister = new FilePersister(subj, resourcePathInfo);
			ai = (AssessmentInstance) persister.toRAM();
			if (ai == null) {
				// nothing found => try with older V5.0 (shorter Repo-ID) as key 
				FilePersister oldPersister = new FilePersister(subj, re.getKey().toString());
				ai = (AssessmentInstance) oldPersister.toRAM();
				if (ai != null) {
					Tracing.logAudit("Read assessment instance from old path version,",subj + "," + re.getKey().toString(), AssessmentFactory.class);
				}
			}
		}

		if (ai == null) {
			// no assessment/survey... to resume, launch a new one
			Resolver resolver = new ImsRepositoryResolver(re.getKey());
			long aiID = CodeHelper.getForeverUniqueID();
			try {
				ai = new AssessmentInstance(re.getKey().longValue(), aiID, resolver, persister, modConfig);
			} catch (Exception e) { return null; }
		}
		else {
			// continue with the latest non-finished test, mark it as resumed
			ai.setResuming(true);
			Resolver resolver = new ImsRepositoryResolver(new Long(ai.getRepositoryEntryKey()));
			ai.setResolver(resolver);
			ai.setPersister(persister);
		}
		return ai; 
		
	}
	
	/**
	 * Create an assessment instance from a document passed by the session.
	 * 
	 * @param subj
	 * @param doc
	 * @return
	 */
	public static AssessmentInstance createAssessmentInstance(Resolver resolver, Persister persister, ModuleConfiguration modConfig) {
		long aiID = CodeHelper.getForeverUniqueID();
		return new AssessmentInstance(0, aiID, resolver, persister, modConfig);
	}

}
