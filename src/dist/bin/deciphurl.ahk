; Reads deciphurl.ini to extracts out the port
IniRead, port, %A_ScriptDir%\..\conf\deciphurl.ini, ngserver, port
if port = ERROR
{
    MsgBox, port must be set in the conf\deciphurl.ini
    ExitApp
}
if port not between 1 and 65535
{
    MsgBox, Port '%port%' is invalid, valid port is from 1 to 65535
    ExitApp
}

; Starts ngserver.
FileCreateDir %A_ScriptDir%\..\logs
Run, ngserver.bat %port% > %A_ScriptDir%\..\logs\ngserver.out.txt, , Hide UseErrorLevel, ngserverpid
Sleep 2000
Process, Exist, %ngserverpid%
if ErrorLevel = 0
{
    MsgBox, deciphurl can't start. Is the port %port% in use?. Configure port in conf\deciphurl.ini
    ExitApp
}

#Persistent
OnExit, ExitSub
return

ExitSub:
RunWait, %A_ScriptDir%\ng.exe --nailgun-port %port% ng-stop, , Hide
if ErrorLevel = ERROR
{
    Run, taskkill /pid %ngserverpid% /T /F, , Hide ; if ng-stop fail, force with taskkill
}
ExitApp

#C::
clipboard = ; Empty the clipboard for ClipWait to work
Send, ^c
ClipWait, 5
if ErrorLevel
{
    MsgBox, deciphurl attempt to copy text onto the clipboard failed
    return
}

RunWait, %A_ScriptDir%\ng.exe --nailgun-port %port% com.ceilfors.deciphurl.Main "%clipboard%", , Hide
if ErrorLevel = ERROR
{
    MsgBox, deciphurl failed to interpolate, please see the log file in the logs directory
    return
}
return