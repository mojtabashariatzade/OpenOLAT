package org.olat.restapi.support.vo.elements;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Description:<br>
 * test course node configuration
 * 
 * <P>
 * Initial Date:  27.07.2010 <br>
 * @author skoeber
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testConfigVO")
public class TestConfigVO {
	
	private Boolean showNavigation;
	private Boolean allowNavigation;
	private Boolean showSectionsOnly;
	private String sequencePresentation;
	private Boolean allowCancel;
	private Boolean allowSuspend;
	private Boolean showQuestionTitle;
	private String summeryPresentation;
	private Integer numAttempts;
	private Boolean showQuestionProgress;
	private Boolean showScoreProgress;
	private Boolean showScoreInfo;
	private Boolean showResultsAfterFinish;
	private Boolean showResultsOnHomepage;
	private Boolean showResultsDependendOnDate;
	private Date showResultsStartDate;
	private Date showResultsEndDate;

	public TestConfigVO() {
		//make JAXB happy
	}

	public Boolean getShowNavigation() {
		return showNavigation;
	}

	public void setShowNavigation(Boolean showNavigation) {
		this.showNavigation = showNavigation;
	}

	public Boolean getAllowNavigation() {
		return allowNavigation;
	}

	public void setAllowNavigation(Boolean allowNavigation) {
		this.allowNavigation = allowNavigation;
	}

	public Boolean getShowSectionsOnly() {
		return showSectionsOnly;
	}

	public void setShowSectionsOnly(Boolean showSectionsOnly) {
		this.showSectionsOnly = showSectionsOnly;
	}

	public String getSequencePresentation() {
		return sequencePresentation;
	}

	public void setSequencePresentation(String sequencePresentation) {
		this.sequencePresentation = sequencePresentation;
	}

	public Boolean getAllowCancel() {
		return allowCancel;
	}

	public void setAllowCancel(Boolean allowCancel) {
		this.allowCancel = allowCancel;
	}

	public Boolean getAllowSuspend() {
		return allowSuspend;
	}

	public void setAllowSuspend(Boolean allowSuspend) {
		this.allowSuspend = allowSuspend;
	}

	public Boolean getShowQuestionTitle() {
		return showQuestionTitle;
	}

	public void setShowQuestionTitle(Boolean showQuestionTitle) {
		this.showQuestionTitle = showQuestionTitle;
	}

	public String getSummeryPresentation() {
		return summeryPresentation;
	}

	public void setSummeryPresentation(String summeryPresentation) {
		this.summeryPresentation = summeryPresentation;
	}

	public Integer getNumAttempts() {
		return numAttempts;
	}

	public void setNumAttempts(Integer numAttempts) {
		this.numAttempts = numAttempts;
	}
	
	public Boolean getShowQuestionProgress() {
		return showQuestionProgress;
	}

	public void setShowQuestionProgress(Boolean showQuestionProgress) {
		this.showQuestionProgress = showQuestionProgress;
	}

	public Boolean getShowScoreProgress() {
		return showScoreProgress;
	}

	public void setShowScoreProgress(Boolean showScoreProgress) {
		this.showScoreProgress = showScoreProgress;
	}

	public Boolean getShowScoreInfo() {
		return showScoreInfo;
	}

	public void setShowScoreInfo(Boolean showScoreInfo) {
		this.showScoreInfo = showScoreInfo;
	}

	public Boolean getShowResultsAfterFinish() {
		return showResultsAfterFinish;
	}

	public void setShowResultsAfterFinish(Boolean showResultsAfterFinish) {
		this.showResultsAfterFinish = showResultsAfterFinish;
	}

	public Boolean getShowResultsOnHomepage() {
		return showResultsOnHomepage;
	}

	public void setShowResultsOnHomepage(Boolean showResultsOnHomepage) {
		this.showResultsOnHomepage = showResultsOnHomepage;
	}

	public Boolean getShowResultsDependendOnDate() {
		return showResultsDependendOnDate;
	}

	public void setShowResultsDependendOnDate(Boolean showResultsDependendOnDate) {
		this.showResultsDependendOnDate = showResultsDependendOnDate;
	}

	public Date getShowResultsStartDate() {
		return showResultsStartDate;
	}

	public void setShowResultsStartDate(Date showResultsStartDate) {
		this.showResultsStartDate = showResultsStartDate;
	}

	public Date getShowResultsEndDate() {
		return showResultsEndDate;
	}

	public void setShowResultsEndDate(Date showResultsEndDate) {
		this.showResultsEndDate = showResultsEndDate;
	}
}
