$ORIGINAL_JAVA_HOME = [System.Environment]::GetEnvironmentVariable("JAVA_HOME")

[System.Environment]::SetEnvironmentVariable("JAVA_HOME", [System.Environment]::GetEnvironmentVariable("JAVA17_HOME"))

mvn clean install

[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $ORIGINAL_JAVA_HOME)