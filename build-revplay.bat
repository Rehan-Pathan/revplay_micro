@echo off

echo =====================================
echo Building RevPlay Microservices
echo =====================================

cd revplay-eureka-server
call mvn clean package -DskipTests

cd ..\revplay-config-server
call mvn clean package -DskipTests

cd ..\revplay-user-service
call mvn clean package -DskipTests

cd ..\revplay-catalog-service
call mvn clean package -DskipTests

cd ..\revplay-playlist-service
call mvn clean package -DskipTests

cd ..\revplay-playback-service
call mvn clean package -DskipTests

cd ..\revplay-favorites-service
call mvn clean package -DskipTests

cd ..\revplay-analytics-service
call mvn clean package -DskipTests

cd ..\revplay-api-gateway
call mvn clean package -DskipTests

cd ..

echo =====================================
echo All JARs built successfully
echo =====================================

pause