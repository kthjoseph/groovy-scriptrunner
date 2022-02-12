import com.atlassian.jira.component.ComponentAccessor

// import org.apache.log4j.Level
// import org.apache.log4j.Logger
// def mylog = Logger.getLogger("com.adaptavist.test")
// mylog.setLevel(Level.DEBUG)
// mylog.debug("Start calculate total scripted field")

def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()

def issueObject = issueManager.getIssueObject(issue.getKey())
// mylog.debug("Issue Key = " + issue.getKey())

// mylog.debug("Get all customfields")
def customFieldObjects = customFieldManager.getCustomFieldObjects(issueObject)

// mylog.debug("Start to sum all the Number Field value")
def sum = 0
customFieldObjects.each {
 	// mylog.debug(it.customFieldType.descriptor.completeKey)
    // mylog.debug("Field object = " + it.name + ", type = " + it.getCustomFieldType().getName())
    def typeName = it.getCustomFieldType().getName()
    if ("Number Field".equals(typeName)) {
        def value = getCustomFieldValue(it.name) as Integer
        // mylog.debug("Field value = " + value)
        value = value == null ? 0 : value
        sum += value
    }
}

return sum