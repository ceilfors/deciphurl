![deciphurl Logo](src/icon/deciphurl-small.png)deciphURL
=========

> deciphurl enables a user to highlight a text, press win+c, and open the referred URL in the browser.

deciphurl connects human with URL again. In a high bandwidth communication organization, often people are 
having a conversation by email, IM, or a dedicated chat room. Some of these programs doesn't allow hyperlinks,
and some people just forgot to put them or find long URL annoying. deciphurl was developed to tackle this problem
in mind.

## Getting started
1. Requirement: JRE 1.5+, as of 1.1.0 only works for Windows.
1. Download the latest release (1.1.0) at https://bintray.com/ceilfors/generic/deciphurl
1. Extract the zip
1. Run deciphurld.exe (or if you have AutoHotkey installed, just run the deciphurld.ahk)
1. Highlight any text and _google define_ will be opened!

## The rules definition
**conf/config.groovy**

[pending]

## Configuration
**conf/deciphurl.ini**
- port: deciphurl uses nailgun to speed up the input processing, hence it requires a port to be specified
- key: by default Win+C. Read [this](http://www.autohotkey.com/docs/Hotkeys.htm#Symbols) to configure.
