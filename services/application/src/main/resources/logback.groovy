String log_pattern = "%level | %logger | %msg | %X{id} %X{username} %X{attribute}%n"

appender("FILE", FileAppender) {
    file = "log/application.log"
    encoder(PatternLayoutEncoder) {
        pattern = log_pattern
    }
}
appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = log_pattern
    }
}

logger("by.shimakser", DEBUG, ["FILE"]);
root(INFO, ["CONSOLE"])