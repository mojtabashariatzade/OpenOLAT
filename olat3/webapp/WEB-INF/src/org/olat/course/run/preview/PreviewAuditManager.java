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

package org.olat.course.run.preview;

import org.olat.core.id.Identity;
import org.olat.course.auditing.UserNodeAuditManager;
import org.olat.course.nodes.CourseNode;

/**
 * Initial Date: 08.02.2005
 * 
 * @author Mike Stock
 */
final class PreviewAuditManager extends UserNodeAuditManager {

	/**
	 * @see org.olat.course.auditing.AuditManager#hasUserNodeLogs(org.olat.course.nodes.CourseNode)
	 */
	public boolean hasUserNodeLogs(CourseNode node) {
		// no logging in preview
		return false;
	}

	/**
	 * @see org.olat.course.auditing.AuditManager#getUserNodeLog(org.olat.course.nodes.CourseNode,
	 *      org.olat.core.id.Identity)
	 */
	public String getUserNodeLog(CourseNode courseNode, Identity identity) {
		// no logging in preview
		return null;
	}

	/**
	 * @see org.olat.course.auditing.AuditManager#appendToUserNodeLog(org.olat.course.nodes.CourseNode,
	 *      org.olat.core.id.Identity, org.olat.core.id.Identity,
	 *      java.lang.String)
	 */
	public void appendToUserNodeLog(CourseNode courseNode, Identity identity, Identity assessedIdentity, String logText) {
	// no logging in preview
	}

}