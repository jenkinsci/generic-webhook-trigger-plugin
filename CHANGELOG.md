# Generic Webhook Plugin Changelog
Changelog of Generic Webhook Plugin.
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


