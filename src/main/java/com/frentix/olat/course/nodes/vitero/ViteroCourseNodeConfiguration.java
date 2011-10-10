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
 * Copyright (c) frentix GmbH<br>
 * http://www.frentix.com<br>
 * <p>
 */
package com.frentix.olat.course.nodes.vitero;

import java.util.List;
import java.util.Locale;

import org.olat.core.extensions.ExtensionResource;
import org.olat.core.gui.translator.Translator;
import org.olat.core.util.Util;
import org.olat.course.nodes.AbstractCourseNodeConfiguration;
import org.olat.course.nodes.CourseNode;
import org.olat.course.nodes.CourseNodeConfiguration;

import com.frentix.olat.course.nodes.ViteroCourseNode;

/**
 * 
 * Description:<br>
 * 
 * <P>
 * Initial Date:  6 oct. 2011 <br>
 *
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 */
public class ViteroCourseNodeConfiguration extends AbstractCourseNodeConfiguration implements CourseNodeConfiguration {

	public String getAlias() {
		return "vitero";
	}

	public String getIconCSSClass() {
		return "o_vc_icon";
	}

	public CourseNode getInstance() {
		return new ViteroCourseNode();
	}

	public String getLinkCSSClass() {
		return "o_vc_icon";
	}

	public String getLinkText(Locale locale) {
		Translator fallback = Util.createPackageTranslator(CourseNodeConfiguration.class, locale);
		Translator translator = Util.createPackageTranslator(this.getClass(), locale, fallback);
		return translator.translate("title_vc");
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	public ExtensionResource getExtensionCSS() {
		return null;
	}

	public List getExtensionResources() {
		return null;
	}

	public String getName() {
		return getAlias();
	}

	public void setup() {
		// no special setup necessary
	}

	public void tearDown() {
		// no special tear down necessary
	}

}