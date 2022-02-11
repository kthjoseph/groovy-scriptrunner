def sum = 0
issue.getSubTaskObjects()?.each { 
    subtask -> sum += 1 
}
return sum