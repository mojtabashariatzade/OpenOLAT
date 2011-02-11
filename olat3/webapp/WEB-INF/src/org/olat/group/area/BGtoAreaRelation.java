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

package org.olat.group.area;

import org.olat.core.id.CreateInfo;
import org.olat.core.id.Persistable;
import org.olat.group.BusinessGroup;

/**
 * Description:<BR/> Internal helper class, used to build the relation between
 * business groups and business group areas. <P/> Initial Date: Aug 23, 2004
 * 
 * @author gnaegi
 */
interface BGtoAreaRelation extends Persistable, CreateInfo {
	/**
	 * @return The business group from this relation
	 */
	abstract BusinessGroup getBusinessGroup();

	/**
	 * @param businessGroup The business group form this relation
	 */
	abstract void setBusinessGroup(BusinessGroup businessGroup);

	/**
	 * @return The business group area from this relation
	 */
	abstract BGArea getGroupArea();

	/**
	 * @param groupArea The business group area from this relation
	 */
	abstract void setGroupArea(BGArea groupArea);
}