# [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\AdoptOpenJDK\jdk-8.0.265.01-hotspot")
#[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "D:\GTheler\prog\Eclipse Adoptium\jdk8u332-b09")
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "D:\prog\_JAVA\jdk8u332-b09")

# cd C:\Users\guill\prog\java_mvn\ppbridge
# SET JAVA_HOME=$GCEMPOS_JAVA_HOME

mvn clean install
