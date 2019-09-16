/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.course.run.scoring;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.olat.core.CoreSpringFactory;
import org.olat.core.id.Identity;
import org.olat.core.logging.Tracing;
import org.olat.core.util.nodes.INode;
import org.olat.course.assessment.AssessmentManager;
import org.olat.course.assessment.CourseAssessmentService;
import org.olat.course.nodeaccess.NodeAccessType;
import org.olat.course.nodes.CourseNode;
import org.olat.course.run.scoring.LastModificationsEvaluator.LastModifications;
import org.olat.course.run.userview.UserCourseEnvironment;
import org.olat.modules.assessment.AssessmentEntry;
import org.olat.modules.assessment.model.AssessmentEntryStatus;
import org.olat.modules.assessment.model.AssessmentObligation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Initial date: 16 Sep 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class AssessmentAccounting implements ScoreAccounting {

	private static final Logger log = Tracing.createLoggerFor(AssessmentAccounting.class);
	
	private final UserCourseEnvironment userCourseEnvironment;
	private final NodeAccessType nodeAccessType;
	private Map<String, AssessmentEntry> identToEntry = new HashMap<>();
	private final Map<CourseNode, AssessmentEvaluation> courseNodeToEval = new HashMap<>();
	private AssessmentEvaluation previosEvaluation;
	
	@Autowired
	private CourseAssessmentService courseAssessmentService;

	public AssessmentAccounting(UserCourseEnvironment userCourseEnvironment) {
		this.userCourseEnvironment = userCourseEnvironment;
		this.nodeAccessType = NodeAccessType.of(userCourseEnvironment);
		CoreSpringFactory.autowireObject(this);
	}

	@Override
	public AssessmentEvaluation getScoreEvaluation(CourseNode courseNode) {
		return courseAssessmentService.getAssessmentEvaluation(courseNode, userCourseEnvironment);
	}

	@Override
	public AssessmentEvaluation evalCourseNode(CourseNode courseNode) {
		AssessmentEvaluation se = courseNodeToEval.get(courseNode);
		if (se == null) {
			se = getScoreEvaluation(courseNode);
			courseNodeToEval.put(courseNode, se);
		}
		return se;
	}

	@Override
	public void evaluateAll() {
		evaluateAll(false);
	}
	
	@Override
	public boolean evaluateAll(boolean update) {
		previosEvaluation = null;
		courseNodeToEval.clear();
		
		identToEntry = loadAssessmentEntries(getIdentity());
		
		CourseNode root = userCourseEnvironment.getCourseEnvironment().getRunStructure().getRootNode();
		fillCacheRecursiv(root);
		
		if (update) {
			updateEntryRecursiv(root);
		}
		
		return false;
	}

	private Map<String, AssessmentEntry> loadAssessmentEntries(Identity identity) {
		return getAssessmentManager()
				.getAssessmentEntries(identity)
				.stream()
				.collect(Collectors.toMap(AssessmentEntry::getSubIdent, Function.identity()));
	}

	private void fillCacheRecursiv(CourseNode courseNode) {
		int childCount = courseNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			INode child = courseNode.getChildAt(i);
			if (child instanceof CourseNode) {
				CourseNode childCourseNode = (CourseNode) child;
				fillCacheRecursiv(childCourseNode);
			}
		}
		
		AssessmentEvaluation assessmentEvaluation = getAssessmentEvaluation(courseNode);
		courseNodeToEval.put(courseNode, assessmentEvaluation);
	}

	private AssessmentEvaluation getAssessmentEvaluation(CourseNode courseNode) {
		AssessmentEntry entry = getOrCreateAssessmentEntry(courseNode);
		return courseAssessmentService.toAssessmentEvaluation(entry, courseNode);
	}

	private AssessmentEntry getOrCreateAssessmentEntry(CourseNode courseNode) {
		AssessmentEntry entry = identToEntry.get(courseNode.getIdent());
		if (entry == null) {
			entry = getAssessmentManager().createAssessmentEntry(courseNode, getIdentity(), null);
		}
		return entry;
	}
	
	private AccountingResult updateEntryRecursiv(CourseNode courseNode) {
		log.debug("Evaluate course node: type '{}', ident: '{}'", courseNode.getType(), courseNode.getIdent());
		
		AssessmentEvaluation currentEvaluation = courseNodeToEval.get(courseNode);
		AccountingResult result = new AccountingResult(currentEvaluation);
		
		AccountingEvaluators evaluators = courseAssessmentService.getEvaluators(courseNode, nodeAccessType);
		
		ObligationEvaluator obligationEvaluator = evaluators.getObligationEvaluator();
		AssessmentObligation obligation = obligationEvaluator.getObligation(courseNode);
		result.setObligation(obligation);
		
		DurationEvaluator durationEvaluator = evaluators.getDurationEvaluator();
		if (durationEvaluator.isDependingOnCurrentNode()) {
			Integer duration = durationEvaluator.getDuration(courseNode);
			result.setDuration(duration);
		}
		
		//TODO uh score 1:1 übernehmen (con / lp)
		//TODO uh passed 1:1 übernehmen (con / lp)
		//TODO uh status. con = passed. lp = childen fully assessed
		ScoreEvaluator scoreEvaluator = evaluators.getScoreEvaluator();
		Float score = scoreEvaluator.getScore(result, courseNode, userCourseEnvironment.getConditionInterpreter());
		result.setScore(score);
		
		PassedEvaluator passedEvaluator = evaluators.getPassedEvaluator();
		Boolean passed = passedEvaluator.getPassed(result, courseNode,
				userCourseEnvironment.getCourseEnvironment().getCourseGroupManager().getCourseEntry(),
				userCourseEnvironment.getConditionInterpreter());
		result.setPassed(passed);
		
		StatusEvaluator statusEvaluator = evaluators.getStatusEvaluator();
		AssessmentEntryStatus status = statusEvaluator.getStatus(previosEvaluation, result);
		result.setStatus(status);
		
		previosEvaluation = result;
		int childCount = courseNode.getChildCount();
		List<AssessmentEvaluation> children = new ArrayList<>(childCount);
		for (int i = 0; i < childCount; i++) {
			INode child = courseNode.getChildAt(i);
			if (child instanceof CourseNode) {
				CourseNode childCourseNode = (CourseNode) child;
				AccountingResult childResult = updateEntryRecursiv(childCourseNode);
				children.add(childResult);
			}
		}
		
		if (durationEvaluator.isdependingOnChildNodes()) {
			Integer duration = durationEvaluator.getDuration(children);
			result.setDuration(duration);
		}
		
		LastModificationsEvaluator lastModificationsEvaluator = evaluators.getLastModificationsEvaluator();
		LastModifications lastModifications = lastModificationsEvaluator.getLastModifications(result, children);
		result.setLastUserModified(lastModifications.getLastUserModified());
		result.setLastCoachModified(lastModifications.getLastCoachModified());
		
		status = statusEvaluator.getStatus(result, children);
		result.setStatus(status);
		
		FullyAssessedEvaluator fullyAssessedEvaluator = evaluators.getFullyAssessedEvaluator();
		Boolean fullyAssessed = fullyAssessedEvaluator.getFullyAssessed(result, children);
		result.setFullyAssessed(fullyAssessed);
		
		if (result.hasChanges()) {
			update(courseNode, result);
		}
		
		return result;
	}

	private void update(CourseNode courseNode, AccountingResult result) {
		AssessmentEntry entry = getOrCreateAssessmentEntry(courseNode);
		
		entry.setObligation(result.getObligation());
		BigDecimal score = result.getScore() != null? new BigDecimal(result.getScore()): null;
		entry.setScore(score);
		entry.setPassed(result.getPassed());
		entry.setDuration(result.getDuration());
		entry.setLastUserModified(result.getLastUserModified());
		entry.setLastCoachModified(result.getLastCoachModified());
		entry.setAssessmentStatus(result.getAssessmentStatus());
		entry.setFullyAssessed(result.getFullyAssessed());
		
		entry = getAssessmentManager().updateAssessmentEntry(entry);
		
		identToEntry.put(courseNode.getIdent(), entry);
		courseNodeToEval.put(courseNode, result);
	}

	private Identity getIdentity() {
		return userCourseEnvironment.getIdentityEnvironment().getIdentity();
	}

	private AssessmentManager getAssessmentManager() {
		return userCourseEnvironment.getCourseEnvironment().getAssessmentManager();
	}
	
}