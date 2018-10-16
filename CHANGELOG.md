# Generic Webhook Plugin Changelog
Changelog of Generic Webhook Plugin.
## Unreleased
### GitHub [#85](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/85) cannot filter based on changed_files and ref    *question*  

**Testing change of specific file in branch #85**


[961bb0c9c5b512a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/961bb0c9c5b512a) Tomas Bjerre *2018-10-16 14:38:46*


## 1.46 (2018-10-04 17:23:18)
### GitHub [#82](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/82) How not to send data in response    *enhancement*  

**Adding option to respond silently #82**


[6e30c524269d25f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6e30c524269d25f) Tomas Bjerre *2018-10-04 17:21:32*


### No issue

**Automatically stepping dependencies**


[001a783571102ed](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/001a783571102ed) Tomas Bjerre *2018-09-23 19:11:15*


## 1.45 (2018-09-11 18:35:23)
### GitHub [#78](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/78) Working Webhook is now returning &quot;Did not find any jobs to trigger&quot;    *question*  

**Making response clearer when no jobs found #78**


[3d1938f91ad1fa0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3d1938f91ad1fa0) Tomas Bjerre *2018-09-11 18:34:11*


### No issue

**Adding multibranch example with credentials**


[a3b0024b222eb9d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a3b0024b222eb9d) Tomas Bjerre *2018-09-07 09:48:43*


## 1.44 (2018-09-06 16:48:37)
### GitHub [#77](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/77) Auth/security: can the GWT end point be reached without authenticating?    *enhancement*  *question*  

**Only impersonating if token supplied #77**


[fe9232eb83b1895](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fe9232eb83b1895) Tomas Bjerre *2018-09-06 16:47:52*


### No issue

**Updating fmt-maven-plugin**

 * Adding another simpler test case on request parameters 
 * Testing github tag push 
 * Adding more testcases on Bitbucket Server and Cloud 

[df9e0129c081fd0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/df9e0129c081fd0) Tomas Bjerre *2018-09-05 06:28:39*


## 1.43 (2018-08-25 14:03:35)
### GitHub [#70](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/70) How to specify the TOKEN using Job DSL?    *question*  

**Documenting token in DSL #70**


[d88bd14417d6922](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d88bd14417d6922) Tomas Bjerre *2018-08-24 09:14:45*


### No issue

**Illegal group reference error if a resolved variable contains unexpected characters**


[76092cf10993f0f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/76092cf10993f0f) Vladimir Tsykun *2018-08-25 13:24:12*


## 1.42 (2018-08-21 05:07:14)
### No issue

**Update JSONPath dependency**


[d6e1eaa7fc7df99](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d6e1eaa7fc7df99) Volodymyr Tsykun *2018-08-20 16:28:19*


## 1.41 (2018-08-09 09:04:59)
### GitHub [#63](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/63) Specifying the authentication token in a scripted pipeline    *question*  

**Token support without Build Token Root Plugin #63**

 * This enables token support when using multibranch pipelines. 
 * Also code cleanup. 

[0fd195da51d7832](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0fd195da51d7832) Tomas Bjerre *2018-08-09 09:04:33*


### GitHub [#65](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/65) Multibranch pipeline trigger specific branch/pr job possibility    *question*  

**Updating doc #65**


[d1f05cfd5300b57](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d1f05cfd5300b57) Tomas Bjerre *2018-08-07 12:16:10*


## 1.40 (2018-07-10 12:21:16)
### GitHub [#62](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/62) NullPointerException when trying to trigger job  

**Avoiding NPE for parameters without value #62**


[3dcdd664bbbb74a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3dcdd664bbbb74a) Tomas Bjerre *2018-07-10 12:20:20*


### No issue

**Adding a jenkinsfile with defaults**


[6e47573b2926fbd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6e47573b2926fbd) Tomas Bjerre *2018-07-09 20:48:34*


## 1.39 (2018-06-25 18:17:02)
### No issue

**Also resolving variables with curly brackets**


[603642cf5d3af95](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/603642cf5d3af95) Tomas Bjerre *2018-06-25 18:15:43*


## 1.38 (2018-06-24 16:51:38)
### GitHub [#61](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/61) Credentials Parameter converted to String rather than Credentials    *enhancement*  

**Supporting Credentials Parameter #61**


[7f4ca9deff70a94](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7f4ca9deff70a94) Tomas Bjerre *2018-06-24 16:49:20*


### No issue

**Doc**


[b7e8b47e1d7b4cc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b7e8b47e1d7b4cc) Tomas Bjerre *2018-06-15 14:49:28*


## 1.37 (2018-06-14 15:19:40)
### GitHub [#60](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/60) Value from token Param is not evaluated     *bug*  

**Remvoing searchUrl and searchName from response #60**


[76bb41ae584ca78](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/76bb41ae584ca78) Tomas Bjerre *2018-06-14 15:18:49*


## 1.36 (2018-05-18 18:02:15)
### No issue

**Doc**


[8c5910820cbd9a8](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8c5910820cbd9a8) Tomas Bjerre *2018-05-18 18:01:27*


## 1.35 (2018-05-09 10:04:15)
### GitHub [#58](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/58) Boolean parameter becomes string    *bug*  

**Respect boolean parameter datatype #58**


[5b36c5a8ca9b832](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5b36c5a8ca9b832) Tomas Bjerre *2018-05-08 16:08:55*


## 1.34 (2018-05-05 21:21:30)
### GitHub [#45](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/45) &quot;This project is parameterized&quot; parameters are being ignored if triggered    *bug*  

**Avoiding warning about skipped parameters #45**

 * Got a lot of: WARNING: Skipped parameter &#39;X&#39;... 
 * Changed code to only add parameters that does exist. 

[759fc7070ce7942](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/759fc7070ce7942) Tomas Bjerre *2018-05-05 21:19:45*


### No issue

**Adding declarative example**


[23e3e03ab5e815b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23e3e03ab5e815b) Tomas Bjerre *2018-05-05 20:50:46*


## 1.33 (2018-05-05 19:13:39)
### GitHub [#47](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/47) Declarative Pipeline support    *enhancement*  

**Declarative Pipeline support #47**


[9d29aa90ffc2996](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9d29aa90ffc2996) Tomas Bjerre *2018-05-05 19:11:38*


## 1.32 (2018-04-18 15:31:31)
### GitHub [#57](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/57) When building using a token, the cause parameter has no effect    *enhancement*  

**Custom cause #57**


[1f0006795ab6941](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/1f0006795ab6941) Tomas Bjerre *2018-04-18 15:30:40*


## 1.31 (2018-04-07 08:59:46)
### GitHub [#55](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/55) When token used, only trigger if token supplied    *enhancement*  *opinions-needed*  

**Only trigger if token matches #55**


[23afc4b6ba81617](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23afc4b6ba81617) Tomas Bjerre *2018-04-07 08:51:48*


### No issue

**BDD**


[fc5306ce0fe9a71](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fc5306ce0fe9a71) Tomas Bjerre *2018-04-03 18:15:38*

**Issue template**


[f6be97728349dfa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/f6be97728349dfa) Tomas Bjerre *2018-03-27 20:15:32*

**improve header variable name description**

 * Version 1.28 introduced RFC 2616 compatibility making all headers lowercase. 
 * This patch adds a note to make this fact more obvious. 
 * Example situation where this matters: a CI systems that checks for X-GitHub-Event header to make further decisions. Since 1.28 the resulting variable changes from X_GitHub_Event to x_github_event effectively breaking this process. 

[0fd81969349a1d0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0fd81969349a1d0) jekader *2018-03-08 17:28:09*


## 1.29 (2018-03-06 20:26:35)
### GitHub [#45](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/45) &quot;This project is parameterized&quot; parameters are being ignored if triggered    *bug*  

**Keeping default values in parameterized jobs #45**


[602ea1fd613abd2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/602ea1fd613abd2) Tomas Bjerre *2018-03-06 20:25:41*


## 1.28 (2018-02-24 13:41:42)
### GitHub [#43](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/43) Add suport for Authorization Bearer  

**Authorization Bearer #43**

 * Also transforming headers to lower case to make them case insensitive (RFC 2616 and 7230). 

[c2620541b8c48d3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c2620541b8c48d3) Tomas Bjerre *2018-02-24 13:39:01*


## 1.27 (2018-02-22 17:53:45)
### No issue

**Avoid resolve variables in random order**


[d602b6bd7a77d87](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d602b6bd7a77d87) Tomas Bjerre *2018-02-22 17:52:47*

**Doc**


[911aca914fcfb79](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/911aca914fcfb79) Tomas Bjerre *2018-02-22 17:05:26*

**Helpful node about underscores for HTTP Header vars**


[52b9b5258c9a2e3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/52b9b5258c9a2e3) Gmanfunky *2018-02-17 02:22:25*


## 1.26 (2018-02-07 15:23:32)
### GitHub [#37](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/37) NullPointerException at GenericTriggerResults (version 1.25)    *bug*  

**Avoiding NPE #37**


[02c4d68a5871f2d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/02c4d68a5871f2d) Tomas Bjerre *2018-02-07 15:21:47*


## 1.25 (2018-02-06 19:45:30)
### GitHub [#35](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/35) Add defaultValue attribute to GenericVariables    *enhancement*  

**Adding defaultValue #35**

 * Also making regexpFilter optional. 
 * Making expressionType optional and defaulting to JSONPath. 
 * Found stacktrace in log when JSONPath does not match, avoiding that now. 

[429a3e253966b59](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/429a3e253966b59) Tomas Bjerre *2018-02-06 19:44:40*


### No issue

**Doc**


[a87bb57241904a5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a87bb57241904a5) Tomas Bjerre *2018-02-06 18:35:46*


## 1.24 (2018-02-05 20:06:59)
### GitHub [#32](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/32) Do not show received payload in Jenkins job    *enhancement*  

**Optional logging in job log #32 #33**

 * Also: 
 * More details in trigger response. Including queue ID. 
 * Avoiding exposure of entire exception stacktrace in response on crash. 

[8580bf8aaf52b2d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8580bf8aaf52b2d) Tomas Bjerre *2018-02-05 19:52:22*


### GitHub [#33](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/33) Receive additional info after job trigger    *enhancement*  

**Optional logging in job log #32 #33**

 * Also: 
 * More details in trigger response. Including queue ID. 
 * Avoiding exposure of entire exception stacktrace in response on crash. 

[8580bf8aaf52b2d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8580bf8aaf52b2d) Tomas Bjerre *2018-02-05 19:52:22*


### No issue

**Cleaning pom**


[4ac53820dbca918](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4ac53820dbca918) Tomas Bjerre *2018-02-05 16:59:30*


## 1.23 (2018-01-27 13:10:21)
### GitHub [#31](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/31) Wanna get all request body that payload body including null value  

**Including null attributes #31**


[632b93761a17e48](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/632b93761a17e48) Tomas Bjerre *2018-01-27 13:09:28*


### No issue

**Removing newlines from assert in test**


[92ced8a3dc25a34](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/92ced8a3dc25a34) Tomas Bjerre *2018-01-03 12:33:36*


## 1.22 (2018-01-03 12:22:47)
### GitHub [#30](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/30) Request: Supporting of JSON header without flattening   

**Contributing the actual JSON/XML #30**

 * Will now contribute the actual JSON, or XML, in the configured variable, if expression does not resolve to a string. So that it is possible to contribute the, or a part of, the JSON/XML that was read. 
 * Correctly readning streams with UTF-8, not default encodings. 
 * Adding Violations Maven Plugin to monitor Findbugs findings. 

[4a60c3cd9350e49](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4a60c3cd9350e49) Tomas Bjerre *2018-01-03 12:18:32*


## 1.21 (2017-12-20 18:52:26)
### GitHub [#26](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/26) JSONPath processing seems to not handle &quot;,&quot; operator  

**Test case with comma operator #26**


[ad58a6aadbdaf32](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ad58a6aadbdaf32) Tomas Bjerre *2017-11-15 18:37:55*


### GitHub [#29](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/29) Unable to resolve variable   

**More information in log if variable cannot be resolved #29**


[19443b4e072acc4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/19443b4e072acc4) Tomas Bjerre *2017-12-20 18:51:29*


### No issue

**Doc**


[98e25326d3f645b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/98e25326d3f645b) Tomas Bjerre *2017-09-20 19:27:55*


## 1.20 (2017-09-20 19:25:18)
### GitHub [#25](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/25) XmlFlattener doesn&#39;t work with Attribute nodes    *bug*  

**Enabling attributes with XPath #25**


[53f924177b85314](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/53f924177b85314) Tomas Bjerre *2017-09-20 19:21:03*


### No issue

**doc**


[34da1337c863b15](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/34da1337c863b15) Tomas Bjerre *2017-08-23 16:16:10*

**Correcting how first value is determined**


[9c1d3999b933e12](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9c1d3999b933e12) Tomas Bjerre *2017-08-17 16:08:10*


## 1.19 (2017-08-16 14:41:37)
### GitHub [#20](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/20) Header Variables cannot be reused through several webhook triggered jobs  

**fix #20: Header Variables cannot be reused through several webhook triggered jobs**


[5c74d73d14093a5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5c74d73d14093a5) Juan Pablo Santos Rodríguez *2017-08-16 10:53:02*


### No issue

**doc**


[a54279dd885a976](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a54279dd885a976) Tomas Bjerre *2017-08-16 14:39:30*

**fix failing tests, should ran them before; seems they were checking for incorrect number of parameters, regardless(?) the previous Enumeration -> List change**


[10d8f40afee5693](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/10d8f40afee5693) Juan Pablo Santos Rodríguez *2017-08-16 11:17:34*

**Refactoring**


[05ac39f8cb39e8d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/05ac39f8cb39e8d) Tomas Bjerre *2017-08-13 06:35:14*

**doc**


[dba825f8f189251](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/dba825f8f189251) Tomas Bjerre *2017-08-11 08:54:20*

**Adding sandbox jenkinsfile**


[7468387fbc3791e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7468387fbc3791e) Tomas Bjerre *2017-08-10 18:52:28*

**doc**


[934742625d102f2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/934742625d102f2) Tomas Bjerre *2017-07-17 16:08:00*


## 1.18 (2017-08-10 18:13:06)
### GitHub [#18](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/18) How to access request header variables?  

**Correcting name/descriptions #18**


[2643526ce361183](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/2643526ce361183) Tomas Bjerre *2017-08-10 18:12:12*


### No issue

**doc**


[8eacab3e02d3680](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8eacab3e02d3680) Tomas Bjerre *2017-07-28 20:27:43*


## 1.17 (2017-07-26 14:58:32)
### No issue

**Helpful response if no jobs found**


[ff3514909f3363d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ff3514909f3363d) Tomas Bjerre *2017-07-26 14:57:36*

**doc**


[b0fad3a4ff5f110](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b0fad3a4ff5f110) Tomas Bjerre *2017-07-26 04:15:15*


## 1.16 (2017-07-25 18:10:46)
### GitHub [#13](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/13) Generic Webhook hook stops working on  v1.15    *bug*  

**Simplifying job finding algorithm #13**


[90984b4b8d06cdd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/90984b4b8d06cdd) Tomas Bjerre *2017-07-25 18:08:42*


### No issue

**Removing trigger config from invocation response**


[c665aefd504ca82](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c665aefd504ca82) Tomas Bjerre *2017-07-25 17:11:44*

**doc**


[fec2f6e88d5e4f4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fec2f6e88d5e4f4) Tomas Bjerre *2017-07-21 12:42:34*


## 1.15 (2017-07-21 12:38:27)
### No issue

**doc**


[8ac068c484022d7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8ac068c484022d7) Tomas Bjerre *2017-07-21 12:33:57*

**Add impersonalization to execute Webhook as anonymous user using only Authentication Token**


[8a98d57c06952fa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8a98d57c06952fa) Pedro Juarez *2017-07-21 02:34:50*

**doc**


[40eb716a1bbe7ac](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/40eb716a1bbe7ac) Tomas Bjerre *2017-07-20 18:58:21*


## 1.14 (2017-07-17 16:06:52)
### GitHub [#10](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/10) replace &quot;-&quot; characters on header variable names  

**#10: replace "-" characters on header variable names**


[13802b842bc50d2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/13802b842bc50d2) Juan Pablo Santos Rodríguez *2017-07-17 11:42:55*


### GitHub [#11](https://github.com/jenkinsci/generic-webhook-trigger-plugin/pull/11) fix #10: replace &quot;-&quot; characters on header variable names  

**follow-up on #11, suggested description no longer makes sense here, as it applies to all kind of variables**


[c6ffa25abb7236a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c6ffa25abb7236a) Juan Pablo Santos Rodríguez *2017-07-17 14:32:34*

**follow -up on #11, transform noWhitespace(String) from FlattenerUtils into  toVariableName, and use it also on headers and request params**


[895979680dd8c03](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/895979680dd8c03) Juan Pablo Santos Rodríguez *2017-07-17 14:31:39*


### No issue

**Avoiding NPE after upgrade**

 * As a result of headers and request parameters not being read from existing config. 

[ad2317275276a5b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ad2317275276a5b) Tomas Bjerre *2017-07-17 16:02:38*

**small typo: flatternJson -> flattenJson**


[72169e5af5d0451](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/72169e5af5d0451) Juan Pablo Santos Rodríguez *2017-07-17 14:33:19*

**Cleaning**


[bedc35d535ae00f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/bedc35d535ae00f) Tomas Bjerre *2017-07-16 05:09:54*

**Refactoring**


[bbd41960d1fc017](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/bbd41960d1fc017) Tomas Bjerre *2017-07-14 05:52:08*


## 1.13 (2017-07-13 19:46:13)
### No issue

**Both exact and "_0" if only one value**

 * If request, or header, expression matches only one value then both an exact variable and a variable with added _0 is now contributed. Users who expect only one value will probably expect the exact variable name. Users who expects several will probably want to always use the variableName_X varaibles. 

[4cc63cc9fc5be42](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4cc63cc9fc5be42) Tomas Bjerre *2017-07-13 19:45:19*

**doc**


[3d6e791dfedabfe](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3d6e791dfedabfe) Tomas Bjerre *2017-07-13 18:15:51*


## 1.12 (2017-07-13 18:09:55)
### GitHub [#9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/9) Enhancement: allow request headers to be set as variables too    *enhancement*  

**Contribute headers to build #9**


[9ad43d864581af5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9ad43d864581af5) Tomas Bjerre *2017-07-13 18:08:52*


### No issue

**Refactoring**

 * Also replacing space in keys with underscore (_). 

[23077594abea6a5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23077594abea6a5) Tomas Bjerre *2017-07-13 15:23:12*

**doc**


[8de2365d832e07a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8de2365d832e07a) Tomas Bjerre *2017-07-12 18:41:20*


## 1.11 (2017-07-12 18:30:35)
### GitHub [#7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/7) Contribute all leafs in node, if node is selected by expression    *enhancement*  

**fixes #7: JSONPath params are always converted to String**


[e1239f8480fdcdf](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e1239f8480fdcdf) Juan Pablo Santos Rodríguez *2017-07-12 07:08:10*


### No issue

**Doc**


[5a5b8e86caec71e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5a5b8e86caec71e) Tomas Bjerre *2017-07-12 18:28:27*

**doc**


[81361f4bdd73d7b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/81361f4bdd73d7b) Tomas Bjerre *2017-05-09 19:30:40*


## 1.10 (2017-05-09 19:28:31)
### GitHub [#6](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/6) After auth still can&#39;t trigger things    *bug*  

**Avoid showing trigger for unsupported projects #6**


[fd6d4e647ec939d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fd6d4e647ec939d) Tomas Bjerre *2017-05-09 19:26:31*


### No issue

**doc**


[cbf75eb9d75192a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/cbf75eb9d75192a) Tomas Bjerre *2017-05-03 19:31:19*


## 1.9 (2017-05-02 18:55:22)
### GitHub [#4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/4) Did not find any jobs to trigger!  

**Adding troubleshooting section to readme #4**


[d1288153a6ae2ff](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d1288153a6ae2ff) Tomas Bjerre *2017-04-29 06:47:22*


### GitHub [#5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/5) Doesn&#39;t work with Pipelines  

**Enabling pipeline multibranch to be triggered #5**


[21af9ce2e996f49](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/21af9ce2e996f49) Tomas Bjerre *2017-05-02 18:54:05*


## 1.8 (2017-04-10 17:29:54)
### No issue

**Only printing variables/post content once in the job log**


[00bf4c75de19007](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/00bf4c75de19007) Tomas Bjerre *2017-04-10 17:28:55*


## 1.7 (2017-04-07 18:31:37)
### No issue

**Correcting invoke URL in docs**


[417e712357349ff](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/417e712357349ff) Tomas Bjerre *2017-04-07 18:27:58*

**doc**


[7ca0ca4e98a6747](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7ca0ca4e98a6747) Tomas Bjerre *2017-04-05 16:59:50*


## 1.6 (2017-03-29 19:58:41)
### GitHub [#3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/3) Lacks job selection    *enhancement*  

**White list request parameters #3**

 * To make it more secure. Also adding regexp filter for them. 

[0c4b799015166cc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0c4b799015166cc) Tomas Bjerre *2017-03-29 19:57:13*


### No issue

**doc**


[25d6448d957ca4f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/25d6448d957ca4f) Tomas Bjerre *2017-03-28 18:32:38*


## 1.5 (2017-03-28 18:08:31)
### GitHub [#3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/3) Lacks job selection    *enhancement*  

**Include request parameters #3**


[3b1626c816d563e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3b1626c816d563e) Tomas Bjerre *2017-03-28 18:07:35*


### No issue

**doc**


[dad0d8f3a2d25a3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/dad0d8f3a2d25a3) Tomas Bjerre *2017-03-14 18:14:16*


## 1.4 (2017-03-14 18:11:55)
### No issue

**Correcting wiki link**


[37694044b53d9f9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/37694044b53d9f9) Tomas Bjerre *2017-03-14 18:10:59*


## 1.3 (2017-03-13 07:20:48)
### GitHub [#2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/2) Conditional trigger    *enhancement*  

**Adding filtering option #2**


[e6111511bfe802d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e6111511bfe802d) Tomas Bjerre *2017-03-13 07:19:49*


### No issue

**doc**


[b80be7df1e2eb66](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b80be7df1e2eb66) Tomas Bjerre *2017-03-12 22:07:15*


## 1.2 (2017-03-12 21:47:37)
### No issue

**Avoiding duplicated logging info**


[e9db0102aeb4563](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e9db0102aeb4563) Tomas Bjerre *2017-03-12 21:46:38*

**doc**


[6b2c43524f6f5b6](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6b2c43524f6f5b6) Tomas Bjerre *2017-03-12 20:21:51*


## 1.1 (2017-03-12 20:18:07)
### No issue

**Defaulting expression type to JSONPath**


[80bc126c7229ac9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/80bc126c7229ac9) Tomas Bjerre *2017-03-12 20:17:13*

**doc**


[ec4a38b96200c76](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ec4a38b96200c76) Tomas Bjerre *2017-03-09 04:42:06*


