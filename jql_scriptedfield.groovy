import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.IssueInputParametersImpl
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.query.Query;

// import org.apache.log4j.Level
// import org.apache.log4j.Logger
// def mylog = Logger.getLogger("com.adaptavist.test")
// mylog.setLevel(Level.DEBUG)
// mylog.debug("Serach issue start")

def authContext = ComponentAccessor.getJiraAuthenticationContext()
ApplicationUser actor = authContext.getLoggedInUser()
JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
SearchService searchService = ComponentAccessor.getComponent(SearchService)
IssueService issueService = ComponentAccessor.getIssueService()

Query query = jqlQueryParser.parseQuery("status not in ('Closed', 'Done')")
SearchResults results = searchService.search(actor, query,PagerFilter.getUnlimitedFilter())
// mylog.debug("All issues=" + results.getResults())

results.getResults() //gives you array of issues returned by the JQL

def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueObject
def customFieldObjects
StringBuilder sb = new StringBuilder();
def sum = 0
results.getResults().each { issue ->
    //do here what ever you want with each issue.
    mylog.debug("Issue key = " + issue.getKey())
    
    // mylog.debug("Get all customfields")
    issueObject = issueManager.getIssueObject(issue.getKey())
    customFieldObjects = customFieldManager.getCustomFieldObjects(issueObject)
    customFieldObjects.each {
        // mylog.debug("Going to get all my own created field which prefix name start with 'Num'")
        if (it.name.startsWith("Num")) {
            def value = getCustomFieldValue(it.name) as Integer
            value = value == null ? 0 : value
            sum += value
        }
    }
    sb.append(issue.getKey()).append(": sum is ").append(sum).append("<br>");
}

return sb.toString();