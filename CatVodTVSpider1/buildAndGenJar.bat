@echo off
:: 关闭命令回显，使批处理运行时不显示每一条命令

call "%~dp0\gradlew" assembleRelease --no-daemon
:: 调用gradlew脚本，执行assembleRelease任务，并禁用守护进程模式

call "%~dp0\jar\genJar.bat" ec
:: 调用jar目录下的genJar.bat脚本，传入参数ec

pause
:: 暂停批处理执行，等待用户按任意键继续