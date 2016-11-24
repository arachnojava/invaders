@echo off

echo Calling MHFramework.bat...
call MHFramework.bat

rem copy ..\bin\*.class .
copy .\Scripts\mainClass.txt .\bin

cd bin

c:\Progra~1\Java\jdk1.6.0_12\bin\jar cvfm SI.jar mainClass.txt *.class mhframework
rem pause
