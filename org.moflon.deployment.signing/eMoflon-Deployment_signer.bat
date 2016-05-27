@echo off
:: location to store the Certificates
set "key_store=%cd%\resources\certificate\moflonCerts.keystore"

:: Find the Java JRE Home
for /f "tokens=* USEBACKQ" %%i in (`java -jar .\dependencies\homefinder.jar`) do set "java_jre_home=%%i"

:: Find the Java JDK Home
for /d %%d in ("%java_jre_home%\..\jdk*") do set "java_jdk_home=%%d"

:: Set jarsigner variable
set "jarsigner=%java_jdk_home%\bin\jarsigner.exe"

:: the store password
set "store_pass=moflon"

:: the Alias
set "alias=eMoflon"

:: Timestampserver
set "timestamp=http://timestamp.comodoca.com/rfc3161"

:: Generates a Certificate
for /r %%f in (*.jar) do "%jarsigner%" -keystore "%key_store%" -storepass "%store_pass%" -tsa %timestamp%  -verbose %%f %alias%

echo all Jars are signed
pause