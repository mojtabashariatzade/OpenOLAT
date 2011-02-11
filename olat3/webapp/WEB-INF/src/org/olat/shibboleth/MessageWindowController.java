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


package org.olat.shibboleth;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.Windows;
import org.olat.core.gui.components.Component;
import org.olat.core.gui.components.Window;
import org.olat.core.gui.components.velocity.VelocityContainer;
import org.olat.core.gui.control.ChiefController;
import org.olat.core.gui.control.DefaultChiefController;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowBackOffice;
import org.olat.core.gui.translator.PackageTranslator;
import org.olat.core.gui.translator.Translator;
import org.olat.core.helpers.Settings;
import org.olat.core.logging.Tracing;
import org.olat.core.util.Util;

/**
 * 
 * Displays a simple message to the user
 * TODO: Lavinia Dumitrescu Class Description for MessageWindowController
 * 
 * <P>
 * Initial Date:  05.11.2007 <br>
 * @author Lavinia Dumitrescu
 */
public class MessageWindowController extends DefaultChiefController {
	private static final String PACKAGE = Util.getPackageName(MessageWindowController.class);
	private static final String VELOCITY_ROOT = Util.getPackageVelocityRoot(MessageWindowController.class);

	private VelocityContainer msg;

	/**
	 * 
	 * @param ureq
	 * @param th
	 * @param detailedmessage
	 * @param supportEmail
	 */
	public MessageWindowController(UserRequest ureq, Throwable th, String detailedmessage, String supportEmail) {
		Translator trans = new PackageTranslator(PACKAGE, ureq.getLocale());
		//Formatter formatter = Formatter.getInstance(ureq.getLocale());
		msg = new VelocityContainer("olatmain", VELOCITY_ROOT + "/message.html", trans, this);
						
		Tracing.logWarn(th.getMessage() + " *** User info: " + detailedmessage, th.getClass());
		
		msg.contextPut("buildversion", Settings.getVersion());
		msg.contextPut("detailedmessage", detailedmessage);					
		if(supportEmail!=null) {
		  msg.contextPut("supportEmail",supportEmail);
		}

		//Window w = new Window("messagewindow", this, jsadder);
		
		Windows ws = Windows.getWindows(ureq);
		WindowBackOffice wbo = ws.getWindowManager().createWindowBackOffice("messagewindow", this);
		Window w = wbo.getWindow();
		
		msg.put("jsAndCssC", w.getJsCssRawHtmlHeader());
		msg.contextPut("theme", w.getGuiTheme());	
		
		w.setContentPane(msg);
		setWindow(w);
	}

	/**
	 * @see org.olat.core.gui.control.DefaultController#event(org.olat.core.gui.UserRequest,
	 *      org.olat.core.gui.components.Component, org.olat.core.gui.control.Event)
	 */
	public void event(UserRequest ureq, Component source, Event event) {
	//
	}

	/**
	 * @see org.olat.core.gui.control.DefaultController#doDispose(boolean)
	 */
	protected void doDispose() {
	//
	}
	
	/**
	 * Provides a simple <code>MessageWindowController</code> for avoiding the famous REDSCREENs.
	 * @param ureq
	 * @param th
	 * @param message
	 * @param supportEmail
	 * @return
	 */
	public static ChiefController createMessageChiefController(UserRequest ureq, Throwable th, String message, String supportEmail) {
		return new MessageWindowController(ureq, th, message, supportEmail);
	}

}