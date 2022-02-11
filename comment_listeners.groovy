import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.util.ImportUtils
import com.atlassian.jira.issue.index.IssueIndexingService

import org.apache.log4j.Level
import org.apache.log4j.Logger

def mylog = Logger.getLogger("com.adaptavist.test")
mylog.setLevel(Level.DEBUG)
mylog.debug("Check comment event start")

def issue = event.getIssue() as MutableIssue;
def commentManager = ComponentAccessor.getCommentManager()

mylog.debug("Find the last comment")
// def comments = commentManager.getComments(issue)
def comment = commentManager.getLastComment(issue)

mylog.debug("Build new description text if not null")
def newDesc = "";
if (comment != null) {
    newDesc = new StringBuilder().append("Author: ")
				.append(comment.authorFullName)
				.append("\r\n")
				.append(comment.body).toString()
    mylog.debug("New Description = " + newDesc)
}

mylog.debug("Start update the description")
def authContext = ComponentAccessor.getJiraAuthenticationContext()
def appUser = authContext.getLoggedInUser()

def issueManager = ComponentAccessor.getIssueManager();
def issueIndexingService = ComponentAccessor.getComponent(IssueIndexingService);

issue.setDescription(newDesc)
issueManager.updateIssue(appUser, issue, EventDispatchOption.ISSUE_UPDATED, false);

mylog.debug("Updated and reindexing to ensure issue correct")
boolean wasIndexing = ImportUtils.isIndexIssues();
ImportUtils.setIndexIssues(true);
issueIndexingService.reIndex(issueManager.getIssueObject(issue.id));
ImportUtils.setIndexIssues(wasIndexing);
