![deciphurl Logo](src/icon/deciphurl-small.png)deciphURL
=========

> deciphurl enables a user to highlight a text, press win+c, and open the referred URL in the browser.

deciphurl connects human with URL again. In a high bandwidth communication organization, often people are 
having a conversation by email, IM, or a dedicated chat room. Some of these programs doesn't allow hyperlinks,
and some people just forgot to put them or find long URL annoying. deciphurl was developed to tackle this problem
in mind.

## Getting started
1. Requirement: JRE 1.5+, as of 1.1.1 only works for Windows.
1. Download the latest release (1.1.1) at https://bintray.com/ceilfors/generic/deciphurl
1. Extract the zip
1. Run deciphurld.exe (or if you have AutoHotkey installed, just run the deciphurld.ahk)
1. Highlight any text and _google define_ will be opened!

## The rules definition
**conf/config.groovy**

This files contains the deciphurl's rules definition. deciphurl interpolates a URL variable with 
a regex group extracted from the input. {1} refers to the first group, {2} the
second group and so on. The URL **must** contain a protocol e.g. `http://`.

Let's say if you have the following rule:

    jenkins {
        pattern = /^(j#)(\d+)$\
        url = "https://ci.jenkins-ci.org/view/All/job/jenkins_main_trunk/{2}/"
    }

When your input is `j#2638`, deciphurl will return `https://ci.jenkins-ci.org/view/All/job/jenkins_main_trunk/2638/`.
{2} in the URL refers to the second group in the regex pattern, which is the digits.

You can have as many rules as you want in this file. deciphurl will try to find a matching rule
based on the pattern, sequentially from top to bottom of the file. By default, this file only
contains the following rule:

    google_define {
        pattern = /([\W\w]+)/
        url = "http://www.google.com/search?q=define:{0.1}"
    }
This rules takes any inputs and `google define` them.

## Configuration
**conf/deciphurl.ini**
- port: deciphurl uses nailgun to speed up the input processing, hence it requires a port to be specified
- key: by default Win+C. Read [this](http://www.autohotkey.com/docs/Hotkeys.htm#Symbols) to configure.

## Building
`gradle distZip`

## Advance rules
deciphurl also supports group multi match. By default when the variable in the input is a single digit,
it will refer to the group from the `first match`. To use the multi match support, use a dot: `{match.group}`.
`match` starts from 0. So when a variable is {1}, it is actually equivalent with {0.1}.

See the following rule:

    multiMatchRule {
        pattern = /(\d)(\d)/
        url = "http://multi.com/{2.2}{2.1}{1.2}{1.1}{0.2}{0.1}{2}{1}"
    }

When the input is `123456`, the interpolation result is `http://multi.com/65432121`.
