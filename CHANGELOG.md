# generic-webhook-trigger-plugin changelog

Changelog of generic-webhook-trigger-plugin.

## 1.86.4 (2023-07-09)

### Bug Fixes

-  correct spelling of flatten in API ([3794d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3794dabb04d5eaf) Tomas Bjerre)  [#268](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/268)  
-  avoid flattening branches was broken ([3fedc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3fedc55573f5748) Tomas Bjerre)  [#268](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/268)  

## 1.86.3 (2023-04-22)

### Bug Fixes

-  excluding guava ([08a20](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/08a204c2d1ca1a3) Tomas Bjerre)  [#262](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/262)  

## 1.86.2 (2023-01-04)

### Bug Fixes

-  stepping owasp-java-html-sanitizer ([7a926](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7a926bc802946a2) Tomas Bjerre)  

### Other changes

**allow links and formatting in causeString**


[4340e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4340ecc84e1d172) Benjamin, Max (mb388a) *2023-01-04 01:19:03*


## 1.86.1 (2022-12-19)

### Bug Fixes

-  release-script ([d9ba4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d9ba4b6b289e960) Tomas Bjerre)  

## 1.86.0 (2022-12-19)

### Features

-  adding allowSeveralTriggersPerBuild configuration ([aa637](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/aa6370b2930ac94) Tomas Bjerre)  [#64](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/64)  [#116](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/116)  [#126](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/126)  [#162](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/162)  [#171](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/171)  [#252](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/252)  

## 1.85.2 (2022-10-17)

### Bug Fixes

-  stepping GSON to 2.9.1 ([7bcdc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7bcdcec1e623bf6) Tomas Bjerre)  

## 1.85.1 (2022-10-08)

### Bug Fixes

-  release-script ([ebfaa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ebfaa8f58c0949e) Tomas Bjerre)  

## 1.85.0 (2022-10-08)

### Features

-  optionally avoid flatterning JSON when selecting a branch ([19a1f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/19a1fca904df922) Tomas Bjerre)  [#237](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/237)  

### Bug Fixes

-  revert avoiding flatterning JSON when resolving entire payload with ([a9582](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a958270f2a24a16) Tomas Bjerre)  [#237](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/237)  

## 1.84.2 (2022-09-24)

### Bug Fixes

-  minor refactoring ([95dfa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/95dfa833c67c95f) Tomas Bjerre)  
-  updating changelog ([b4813](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b48133a999159ac) Tomas Bjerre)  

## 1.84.1 (2022-09-10)

### Bug Fixes

-  avoiding flatterning JSON when resolving entire payload with `$` ([d2488](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d24880327a71b0d) Tomas Bjerre)  [#237](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/237)  

### Other changes

**Jobs triggered with a token run with system privileges (#241)**


[fa097](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fa0974147f7e7c7) DanInst84 *2022-05-16 07:50:34*


## 1.84 (2022-04-19)

### Other changes

**Replace jenkins with example.org in public facing error message. (#236)**


[93b29](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/93b296142f4a1f9) rene *2022-04-19 16:08:37*


## 1.83 (2022-01-24)

### Bug Fixes

-  removing accidentally hardcoded text in cause ([16340](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/16340bcdeb49f50) Tomas Bjerre)  

## 1.82 (2022-01-19)

### Bug Fixes

-  escaping text in cause ([b289c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b289c32aa74439f) Tomas Bjerre)  [#228](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/228)  

### Other changes

**Merge branch 'feature/issue-228'**


[aa904](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/aa904f159b816ad) Tomas Bjerre *2022-01-19 14:57:20*

**revert faulty merge**


[2ce3a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/2ce3aaa88349de5) Tomas Bjerre *2022-01-19 14:56:32*


## 1.80 (2022-01-15)

### Bug Fixes

-  ignore OPTIONS ([14fa1](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/14fa17df8ad7e38) Tomas Bjerre)  [#229](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/229)  

### Other changes

**Update logging path for Render (#227)**

* Logging for Render is not in the plugin&#x27;s namespace, just shows up as 
* Render. Which could cause conflicts. 

[8066e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8066e337b15db8e) Mike Terzo *2022-01-07 17:40:16*


## 1.79 (2021-12-23)

### Bug Fixes

-  optnially specify contentType and charset ([d219c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d219ce3384c7566) Tomas Bjerre)  [#226](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/226)  [#227](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/227)  

## 1.78 (2021-12-22)

### Other changes

**minor cleanup in pom**


[27a7e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/27a7e079a67efec) Tomas Bjerre *2021-12-22 05:51:20*

**Accept x-www-form-urlencoded #225**


[381c9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/381c993b309dd00) Tomas Bjerre *2021-12-22 05:20:48*


## 1.77 (2021-09-17)

### Features

-  json-path 2.6.0 ([90a0d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/90a0dcc3251f6e7) Tomas Bjerre)  
-  plugin-parent 4.25 and jenkins 2.222.4 ([62797](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/62797e5f3c9ed4a) Tomas Bjerre)  

### Other changes

**Update issue templates**


[fa908](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fa908eb7bb0ffc8) Tomas Bjerre *2021-09-09 02:33:07*

**Update issue templates**


[cc5be](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/cc5be4e6323f4fd) Tomas Bjerre *2021-09-09 02:28:52*


## 1.76 (2021-08-28)

### Bug Fixes

-  bug in hmac verification ([14f62](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/14f62f8bdd4d39c) Tomas Bjerre)  [#216](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/216)  

## 1.75 (2021-08-20)

### Bug Fixes

-  find credentials configured in folders ([8366b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8366bf58bbde1b4) Tomas Bjerre)  [#214](https://github.com/jenkinsci/generic-webhook-trigger-plugin/issues/214)  

## 1.73 (2021-06-16)

### Bug Fixes

-  disallow-doctype-decl ([da434](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/da434dfca1b82f5) Tomas Bjerre)  

### Other changes

**bdd test for pull-request and issue-comment #200**


[75de9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/75de929d6ed34bf) Tomas Bjerre *2021-02-20 07:38:10*


## 1.72 (2020-11-06)

### Other changes

**Supporting Base64 encoded HMAC header #187**


[17c8f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/17c8f3495ee0cd2) Tomas Bjerre *2020-11-06 17:33:51*

**Documenting**


[99821](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/99821d521ee09b8) Tomas Bjerre *2020-10-28 15:56:51*


## 1.70 (2020-10-28)

### Other changes

**Correcting X-Gitlab-Token #186**


[aeec7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/aeec75fecff3b14) Tomas Bjerre *2020-10-28 15:33:17*


## 1.69 (2020-10-28)

### Other changes

**Adding support for X-Gitlab-Token #186**


[f0679](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/f067906be0a2223) Tomas Bjerre *2020-10-28 14:34:06*


## 1.68 (2020-09-28)

### Other changes

**Adding token credential support #183 #165**


[e923e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e923e8d9f15bb8d) Tomas Bjerre *2020-09-28 17:13:03*

**documenting batched builds #171**


[c0cfc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c0cfc1314988ab5) Tomas Bjerre *2020-08-05 04:57:42*


## 1.67 (2020-03-13)

### Other changes

**Refactoring after #160**


[21f3b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/21f3b54169998e7) Tomas Bjerre *2020-03-13 15:49:38*

**Add ability to over-ride a jobs quiet period #159**


[2f372](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/2f3722f0a6c4c3d) Thomas Winderweedle *2020-03-13 15:28:59*

**Ignore if list contains item with attribute value #154**


[39a37](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/39a37c5649790d7) Tomas Bjerre *2020-01-23 16:58:17*

**Stepping dependencies**


[3cb4d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3cb4d794819bae2) Tomas Bjerre *2020-01-19 18:15:51*

**Fix minor typo (#150)**


[513a5](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/513a54530219b67) Tristan McPherson *2019-12-06 18:49:39*


## 1.66 (2019-11-22)

### Other changes

**Using HTTP error codes #146**

* New datastructure in JSON responses. 
* Lowering log-level i Jenkins server log. 

[89877](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/898775bd4a05c3e) Tomas Bjerre *2019-11-21 20:22:46*


## 1.65 (2019-11-17)

### Other changes

**Allow whitelist host to be empty, and rely on only HMAC**


[17cb3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/17cb38ac9c8b82b) Tomas Bjerre *2019-11-17 15:50:28*

**More test #145**


[935fa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/935fab2291e9663) Tomas Bjerre *2019-11-17 13:22:16*


## 1.64 (2019-11-15)

### Other changes

**Refactoring after merging #145**


[04963](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/04963fa91455837) Tomas Bjerre *2019-11-15 18:47:07*

**Fix whitelist host validation (#145)**

* Whitelist host validation 

[09101](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0910146b517c742) AbsoluteOther *2019-11-15 15:46:53*


## 1.63 (2019-11-10)

### Other changes

**Refactoring and cleanup of #143 and solving #141**


[dcc87](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/dcc87c074e5f212) Tomas Bjerre *2019-11-10 18:53:58*

**Merge pull request #143 from AbsoluteOther/cidr**

* Support CIDR Notation 

[c0903](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c0903458a9e63c7) Tomas Bjerre *2019-11-10 17:17:45*

**Cleanup.**


[7b6bd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7b6bdb7be8d66aa) Terrance Wood *2019-11-10 14:49:10*

**Cleanup.**


[922fb](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/922fb3a258583f8) Terrance Wood *2019-11-10 14:42:51*

**Removed problematic guava dependency.**


[57884](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/57884ebd47638f8) Terrance Wood *2019-11-10 14:33:15*

**Fixed exact whitelist matches broken after introducing CIDR notation.**


[0ab3b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0ab3bc62d64f490) Terrance Wood *2019-11-10 09:10:49*

**Fixed field form validation for whitelist host.**


[48e30](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/48e3063c6cd04b4) Terrance Wood *2019-11-10 09:05:17*

**Added support for ipv4/ipv6 CIDR notation to WhitelistVerifer; includes methods for form field validation.**


[db13a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/db13a2b33594b0a) Terrance Wood *2019-11-10 08:54:50*

**Added google guava and commons-ip-math packages; bumped java.level due to package requirements.**


[e6daa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e6daab9b3ff40e7) Terrance Wood *2019-11-10 08:53:44*

**Added tests for CIDR notation.**


[daecd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/daecd55fcced3a1) Terrance Wood *2019-11-10 08:52:38*


## 1.62 (2019-10-21)

### Other changes

**Referencing Github as URL in pom**


[721da](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/721da84ea72506e) Tomas Bjerre *2019-10-21 15:09:07*


## 1.61 (2019-10-21)

### Other changes

**Incoming HMAC header in error message #139**


[8f491](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8f491b592bc0661) Tomas Bjerre *2019-10-21 04:04:54*


## 1.60 (2019-10-21)

### Other changes

**Clearer responses with whitelist #139**


[118ef](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/118efcd549630f1) Tomas Bjerre *2019-10-21 03:53:30*


## 1.59 (2019-10-20)

### Other changes

**Correcting whitelist with multiple whitelisted items #139**


[e77e0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e77e0329c4793b1) Tomas Bjerre *2019-10-20 20:27:41*


## 1.58 (2019-10-20)

### Other changes

**Clearer error from whitelist filter #139**


[7f585](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7f585f8f230d238) Tomas Bjerre *2019-10-20 19:44:59*

**Merge pull request #140 from jenkinsci/feature/whitelist-hmac**

* Whitelist and HMAC #139 

[d2dd7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d2dd767341a4131) Tomas Bjerre *2019-10-20 19:22:31*

**Whitelist and HMAC #139**


[d39f8](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d39f81c3ccefaa6) Tomas Bjerre *2019-10-20 19:20:49*

**Merge pull request #138 from jenkinsci/feature/taking-care-of-some-sca**

* Taking care of some sca 

[80a11](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/80a1176f0d639f0) Tomas Bjerre *2019-10-14 16:40:30*

**Taking care of some sca**


[a1066](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a1066f64da803cc) Tomas Bjerre *2019-10-14 16:39:13*

**doc**


[722aa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/722aaf227e3dd56) Tomas Bjerre *2019-10-03 13:28:49*

**Testing combination of variables #136**


[e02cb](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e02cb6ccfd88325) Tomas Bjerre *2019-09-29 09:18:16*

**Create FUNDING.yml**


[95280](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/952805a24a1e7b6) Tomas Bjerre *2019-09-28 06:52:11*

**Merge pull request #135 from daniel-beck-bot/https-urls-pom**

* Use HTTPS URLs in pom.xml 

[b1676](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b1676943ae0fab3) Tomas Bjerre *2019-09-24 12:22:37*

**Use HTTPS URLs in pom.xml**


[352b0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/352b0124fbca71f) Daniel Beck *2019-09-24 10:36:48*

**Test case with newlines #132**


[23bbe](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23bbefcc5b0dd39) Tomas Bjerre *2019-09-02 15:36:33*

**Documenting replay pipeline #130**


[8f69e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8f69e3b659a5ee3) Tomas Bjerre *2019-08-27 17:07:58*


## 1.57 (2019-08-20)

### Other changes

**Updating description**


[904c7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/904c76d1b9319c7) Tomas Bjerre *2019-08-20 18:09:11*

**doc**


[f8446](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/f84463c9065eaa0) Tomas Bjerre *2019-08-19 13:30:25*


## 1.56 (2019-08-01)

### Other changes

**Avoiding NPE #127**


[7a665](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7a6654973359749) Tomas Bjerre *2019-08-01 15:36:14*


## 1.55 (2019-07-31)

### Other changes

**Avoid IndexOutOfBounds when no stacktrace is in thrown exception #127**


[68aea](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/68aeade50f90079) Tomas Bjerre *2019-07-31 15:00:25*

**Documenting default values #125**


[bd73f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/bd73fe693fe549f) Tomas Bjerre *2019-07-02 14:57:39*

**More test #123**


[f08ea](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/f08ea0c26128b14) Tomas Bjerre *2019-06-28 12:39:49*

**Testing exact matching of single variable #122**


[13bce](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/13bce9c546f3c92) Tomas Bjerre *2019-06-19 19:03:38*


## 1.54 (2019-05-06)

### Other changes

**Avoid triggering when text or regexp is empty #115**

* This was originally implemented to avoid checking the regexp when no filtering was configured. 
* Changing to triggering when text **and** regexp is empty. 

[9c78d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9c78d75222ca3fe) Tomas Bjerre *2019-05-06 13:26:40*


## 1.53 (2019-05-02)

### Other changes

**Allowing empty default values #35**

* This may break the API for some users. But the intention is that it will make pipelines easier to write, without having to check for null. 

[2bf0a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/2bf0a3f15ec2af6) Tomas Bjerre *2019-05-02 15:35:03*

**Update README.md**


[c33a1](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c33a171c0b7960f) Tomas Bjerre *2019-04-09 10:36:43*

**Doc #107**


[adda8](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/adda82b3c27d146) Tomas Bjerre *2019-03-27 17:21:39*

**Doc**


[cb04c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/cb04c670d9177f5) Tomas Bjerre *2019-02-16 18:17:44*

**Doc**


[82d48](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/82d483a2f33fa7b) Tomas Bjerre *2019-01-23 20:17:15*


## 1.52 (2019-01-02)

### Other changes

**Avoid calling unsupported API getLogFile #97**


[febc1](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/febc1ba1afa9ae0) Tomas Bjerre *2019-01-02 09:28:04*

**Fixing test case**


[3d591](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3d59136e2515a0a) Tomas Bjerre *2018-12-12 18:56:45*


## 1.51 (2018-12-12)

### Other changes

**Contributing result of JSONPath $ exactly as received**


[94a88](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/94a88fa42fe6ad3) Tomas Bjerre *2018-12-12 16:44:19*

**Example of GitLab triggering on comment**


[3bdf8](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3bdf8281aff00f3) Tomas Bjerre *2018-12-01 10:16:58*

**Test case for changed files in folders #91**


[731e4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/731e44cc8b1fea7) Tomas Bjerre *2018-11-27 20:46:42*


## 1.50 (2018-11-27)

### Other changes

**Linking to BDD tests from GUI**


[fc99c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fc99cc4d1335531) Tomas Bjerre *2018-11-27 07:26:46*


## 1.49 (2018-11-26)

### Other changes

**Tweaking docs #92**


[4f4d3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4f4d3c00403eff2) Tomas Bjerre *2018-11-26 20:01:49*

**Adding example of ignoring push event with zero commits**


[8ba5b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8ba5bcd28356fba) Tomas Bjerre *2018-11-16 19:51:45*

**Example of ignoring removal or creation of branches**


[10872](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/108723a32c7c5ec) Tomas Bjerre *2018-11-16 19:28:24*

**Documenting trigger on changed folder GitLab**


[a4713](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a4713a8b6532496) Tomas Bjerre *2018-11-15 18:13:36*


## 1.48 (2018-11-08)

### Other changes

**Refactoring after merge of #88**


[62b53](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/62b53f7f2f6f7e1) Tomas Bjerre *2018-11-08 17:46:31*

**Merge pull request #88 from robert-shade/fix_quiet**

* Use the job&#x27;s configured quiet period 

[a680f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a680f84720a73b5) Tomas Bjerre *2018-11-08 17:39:26*

**Use the job's configured quiet period**


[08e27](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/08e27215e450252) Robert Shade *2018-11-08 16:36:28*


## 1.47 (2018-11-07)

### Other changes

**Printing information when no jobs found #47 #84 #81**


[800de](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/800def977df4232) Tomas Bjerre *2018-11-07 13:56:56*

**Testing change of specific file in branch #85**


[d40b2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d40b2be422907bc) Tomas Bjerre *2018-10-16 14:39:55*


## 1.46 (2018-10-04)

### Other changes

**Adding option to respond silently #82**


[6e30c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6e30c524269d25f) Tomas Bjerre *2018-10-04 17:21:32*

**Automatically stepping dependencies**


[001a7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/001a783571102ed) Tomas Bjerre *2018-09-23 19:11:15*


## 1.45 (2018-09-11)

### Other changes

**Making response clearer when no jobs found #78**


[3d193](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3d1938f91ad1fa0) Tomas Bjerre *2018-09-11 18:34:11*

**Adding multibranch example with credentials**


[a3b00](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a3b0024b222eb9d) Tomas Bjerre *2018-09-07 09:48:43*


## 1.44 (2018-09-06)

### Other changes

**Only impersonating if token supplied #77**


[fe923](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fe9232eb83b1895) Tomas Bjerre *2018-09-06 16:47:52*

**Updating fmt-maven-plugin**

* Adding another simpler test case on request parameters 
* Testing github tag push 
* Adding more testcases on Bitbucket Server and Cloud 

[df9e0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/df9e0129c081fd0) Tomas Bjerre *2018-09-05 06:28:39*


## 1.43 (2018-08-25)

### Other changes

**Merge pull request #72 from vtsykun/fix/regex-exception**

* Fixed an error if a resolved variable contains unexpected characters 

[d5f1e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d5f1e4b456b2630) Tomas Bjerre *2018-08-25 14:02:09*

**Illegal group reference error if a resolved variable contains unexpected characters**


[76092](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/76092cf10993f0f) Vladimir Tsykun *2018-08-25 13:24:12*

**Documenting token in DSL #70**


[d88bd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d88bd14417d6922) Tomas Bjerre *2018-08-24 09:14:45*


## 1.42 (2018-08-21)

### Other changes

**Merge pull request #69 from vtsykun/master**

* Update JSONPath 2.2.0 -&gt; 2.3.0 

[41148](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/41148f0e15ca0a3) Tomas Bjerre *2018-08-21 04:58:50*

**Update JSONPath dependency**


[d6e1e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d6e1eaa7fc7df99) Volodymyr Tsykun *2018-08-20 16:28:19*


## 1.41 (2018-08-09)

### Other changes

**Token support without Build Token Root Plugin #63**

* This enables token support when using multibranch pipelines. 
* Also code cleanup. 

[0fd19](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0fd195da51d7832) Tomas Bjerre *2018-08-09 09:04:33*

**Updating doc #65**


[d1f05](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d1f05cfd5300b57) Tomas Bjerre *2018-08-07 12:16:10*


## 1.40 (2018-07-10)

### Other changes

**Avoiding NPE for parameters without value #62**


[3dcdd](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3dcdd664bbbb74a) Tomas Bjerre *2018-07-10 12:20:20*

**Adding a jenkinsfile with defaults**


[6e475](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6e47573b2926fbd) Tomas Bjerre *2018-07-09 20:48:34*


## 1.39 (2018-06-25)

### Other changes

**Also resolving variables with curly brackets**


[60364](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/603642cf5d3af95) Tomas Bjerre *2018-06-25 18:15:43*


## 1.38 (2018-06-24)

### Other changes

**Supporting Credentials Parameter #61**


[7f4ca](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7f4ca9deff70a94) Tomas Bjerre *2018-06-24 16:49:20*

**Doc**


[b7e8b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b7e8b47e1d7b4cc) Tomas Bjerre *2018-06-15 14:49:28*


## 1.37 (2018-06-14)

### Other changes

**Remvoing searchUrl and searchName from response #60**


[76bb4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/76bb41ae584ca78) Tomas Bjerre *2018-06-14 15:18:49*


## 1.36 (2018-05-18)

### Other changes

**Doc**


[8c591](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8c5910820cbd9a8) Tomas Bjerre *2018-05-18 18:01:27*


## 1.35 (2018-05-09)

### Other changes

**Respect boolean parameter datatype #58**


[5b36c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5b36c5a8ca9b832) Tomas Bjerre *2018-05-08 16:08:55*


## 1.34 (2018-05-05)

### Other changes

**Avoiding warning about skipped parameters #45**

* Got a lot of: WARNING: Skipped parameter &#x27;X&#x27;... 
* Changed code to only add parameters that does exist. 

[759fc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/759fc7070ce7942) Tomas Bjerre *2018-05-05 21:19:45*

**Adding declarative example**


[23e3e](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23e3e03ab5e815b) Tomas Bjerre *2018-05-05 20:50:46*


## 1.33 (2018-05-05)

### Other changes

**Declarative Pipeline support #47**


[9d29a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9d29aa90ffc2996) Tomas Bjerre *2018-05-05 19:11:38*


## 1.32 (2018-04-18)

### Other changes

**Custom cause #57**


[1f000](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/1f0006795ab6941) Tomas Bjerre *2018-04-18 15:30:40*


## 1.31 (2018-04-07)

### Other changes

**Only trigger if token matches #55**


[23afc](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23afc4b6ba81617) Tomas Bjerre *2018-04-07 08:51:48*

**BDD**


[fc530](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fc5306ce0fe9a71) Tomas Bjerre *2018-04-03 18:15:38*

**Issue template**


[f6be9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/f6be97728349dfa) Tomas Bjerre *2018-03-27 20:15:32*

**Merge pull request #46 from jekader/master**

* improve header variable name description 

[0b864](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0b864e180d271a9) Tomas Bjerre *2018-03-08 17:58:07*

**improve header variable name description**

* Version 1.28 introduced RFC 2616 compatibility making all headers lowercase. 
* This patch adds a note to make this fact more obvious. 
* Example situation where this matters: a CI systems that checks for X-GitHub-Event header to make further decisions. Since 1.28 the resulting variable changes from X_GitHub_Event to x_github_event effectively breaking this process. 

[0fd81](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0fd81969349a1d0) jekader *2018-03-08 17:28:09*


## 1.29 (2018-03-06)

### Other changes

**Keeping default values in parameterized jobs #45**


[602ea](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/602ea1fd613abd2) Tomas Bjerre *2018-03-06 20:25:41*


## 1.28 (2018-02-24)

### Other changes

**Authorization Bearer #43**

* Also transforming headers to lower case to make them case insensitive (RFC 2616 and 7230). 

[c2620](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c2620541b8c48d3) Tomas Bjerre *2018-02-24 13:39:01*


## 1.27 (2018-02-22)

### Other changes

**Avoid resolve variables in random order**


[d602b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d602b6bd7a77d87) Tomas Bjerre *2018-02-22 17:52:47*

**Doc**


[911ac](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/911aca914fcfb79) Tomas Bjerre *2018-02-22 17:05:26*

**Merge pull request #41 from gmanfunky/underscores**

* Helpful node about underscores for HTTP Header vars 

[64b76](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/64b76446bca982a) Tomas Bjerre *2018-02-17 06:24:32*

**Helpful node about underscores for HTTP Header vars**


[52b9b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/52b9b5258c9a2e3) Gmanfunky *2018-02-17 02:22:25*


## 1.26 (2018-02-07)

### Other changes

**Avoiding NPE #37**


[02c4d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/02c4d68a5871f2d) Tomas Bjerre *2018-02-07 15:21:47*


## 1.25 (2018-02-06)

### Other changes

**Adding defaultValue #35**

* Also making regexpFilter optional. 
* Making expressionType optional and defaulting to JSONPath. 
* Found stacktrace in log when JSONPath does not match, avoiding that now. 

[429a3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/429a3e253966b59) Tomas Bjerre *2018-02-06 19:44:40*

**Doc**


[a87bb](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a87bb57241904a5) Tomas Bjerre *2018-02-06 18:35:46*


## 1.24 (2018-02-05)

### Other changes

**Optional logging in job log #32 #33**

* Also: 
* More details in trigger response. Including queue ID. 
* Avoiding exposure of entire exception stacktrace in response on crash. 

[8580b](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8580bf8aaf52b2d) Tomas Bjerre *2018-02-05 19:52:22*

**Cleaning pom**


[4ac53](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4ac53820dbca918) Tomas Bjerre *2018-02-05 16:59:30*


## 1.23 (2018-01-27)

### Other changes

**Including null attributes #31**


[632b9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/632b93761a17e48) Tomas Bjerre *2018-01-27 13:09:28*

**Removing newlines from assert in test**


[92ced](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/92ced8a3dc25a34) Tomas Bjerre *2018-01-03 12:33:36*


## 1.22 (2018-01-03)

### Other changes

**Contributing the actual JSON/XML #30**

* Will now contribute the actual JSON, or XML, in the configured variable, if expression does not resolve to a string. So that it is possible to contribute the, or a part of, the JSON/XML that was read. 
* Correctly readning streams with UTF-8, not default encodings. 
* Adding Violations Maven Plugin to monitor Findbugs findings. 

[4a60c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4a60c3cd9350e49) Tomas Bjerre *2018-01-03 12:18:32*


## 1.21 (2017-12-20)

### Other changes

**More information in log if variable cannot be resolved #29**


[19443](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/19443b4e072acc4) Tomas Bjerre *2017-12-20 18:51:29*

**Test case with comma operator #26**


[ad58a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ad58a6aadbdaf32) Tomas Bjerre *2017-11-15 18:37:55*

**Doc**


[98e25](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/98e25326d3f645b) Tomas Bjerre *2017-09-20 19:27:55*


## 1.20 (2017-09-20)

### Other changes

**Enabling attributes with XPath #25**


[53f92](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/53f924177b85314) Tomas Bjerre *2017-09-20 19:21:03*

**doc**


[34da1](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/34da1337c863b15) Tomas Bjerre *2017-08-23 16:16:10*

**Correcting how first value is determined**


[9c1d3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9c1d3999b933e12) Tomas Bjerre *2017-08-17 16:08:10*


## 1.19 (2017-08-16)

### Other changes

**doc**


[a5427](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/a54279dd885a976) Tomas Bjerre *2017-08-16 14:39:30*

**Merge pull request #21 from juanpablo-santos/master**

* fixes #20: Header Variables cannot be reused through several webhook triggered jobs 

[66ec0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/66ec0ca839e5eea) Tomas Bjerre *2017-08-16 14:35:14*

**fix failing tests, should ran them before; seems they were checking for incorrect number of parameters, regardless(?) the previous Enumeration -> List change**


[10d8f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/10d8f40afee5693) Juan Pablo Santos Rodríguez *2017-08-16 11:17:34*

**fix #20: Header Variables cannot be reused through several webhook triggered jobs**


[5c74d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5c74d73d14093a5) Juan Pablo Santos Rodríguez *2017-08-16 10:53:02*

**Merge branch 'master' of github.com:jenkinsci/generic-webhook-trigger-plugin**


[e6384](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e63842a11e3f22a) Juan Pablo Santos Rodríguez *2017-08-16 09:39:08*

**Refactoring**


[05ac3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/05ac39f8cb39e8d) Tomas Bjerre *2017-08-13 06:35:14*

**doc**


[dba82](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/dba825f8f189251) Tomas Bjerre *2017-08-11 08:54:20*

**Adding sandbox jenkinsfile**


[74683](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7468387fbc3791e) Tomas Bjerre *2017-08-10 18:52:28*

**doc**


[93474](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/934742625d102f2) Tomas Bjerre *2017-07-17 16:08:00*


## 1.18 (2017-08-10)

### Other changes

**Correcting name/descriptions #18**


[26435](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/2643526ce361183) Tomas Bjerre *2017-08-10 18:12:12*

**doc**


[8eaca](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8eacab3e02d3680) Tomas Bjerre *2017-07-28 20:27:43*


## 1.17 (2017-07-26)

### Other changes

**Helpful response if no jobs found**


[ff351](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ff3514909f3363d) Tomas Bjerre *2017-07-26 14:57:36*

**doc**


[b0fad](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b0fad3a4ff5f110) Tomas Bjerre *2017-07-26 04:15:15*


## 1.16 (2017-07-25)

### Other changes

**Simplifying job finding algorithm #13**


[90984](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/90984b4b8d06cdd) Tomas Bjerre *2017-07-25 18:08:42*

**Removing trigger config from invocation response**


[c665a](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c665aefd504ca82) Tomas Bjerre *2017-07-25 17:11:44*

**doc**


[fec2f](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fec2f6e88d5e4f4) Tomas Bjerre *2017-07-21 12:42:34*


## 1.15 (2017-07-21)

### Other changes

**doc**


[8ac06](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8ac068c484022d7) Tomas Bjerre *2017-07-21 12:33:57*

**Merge pull request #12 from pjuarezd/feature/auth_token_impersonalization**

* Add impersonalization to execute Webhook as anonymous user using token 

[e72b2](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e72b21964df231a) Tomas Bjerre *2017-07-21 12:20:58*

**Add impersonalization to execute Webhook as anonymous user using only Authentication Token**


[8a98d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8a98d57c06952fa) Pedro Juarez *2017-07-21 02:34:50*

**doc**


[40eb7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/40eb716a1bbe7ac) Tomas Bjerre *2017-07-20 18:58:21*


## 1.14 (2017-07-17)

### Other changes

**Avoiding NPE after upgrade**

* As a result of headers and request parameters not being read from existing config. 

[ad231](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ad2317275276a5b) Tomas Bjerre *2017-07-17 16:02:38*

**Merge pull request #11 from juanpablo-santos/underscore_headers_names**

* fix #10: replace &quot;-&quot; characters on header variable names 

[7cb84](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7cb847ee9034c90) Tomas Bjerre *2017-07-17 15:23:02*

**small typo: flatternJson -> flattenJson**


[72169](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/72169e5af5d0451) Juan Pablo Santos Rodríguez *2017-07-17 14:33:19*

**follow-up on #11, suggested description no longer makes sense here, as it applies to all kind of variables**


[c6ffa](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/c6ffa25abb7236a) Juan Pablo Santos Rodríguez *2017-07-17 14:32:34*

**follow -up on #11, transform noWhitespace(String) from FlattenerUtils into  toVariableName, and use it also on headers and request params**


[89597](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/895979680dd8c03) Juan Pablo Santos Rodríguez *2017-07-17 14:31:39*

**#10: replace "-" characters on header variable names**


[13802](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/13802b842bc50d2) Juan Pablo Santos Rodríguez *2017-07-17 11:42:55*

**Cleaning**


[bedc3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/bedc35d535ae00f) Tomas Bjerre *2017-07-16 05:09:54*

**Refactoring**


[bbd41](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/bbd41960d1fc017) Tomas Bjerre *2017-07-14 05:52:08*


## 1.13 (2017-07-13)

### Other changes

**Both exact and "_0" if only one value**

* If request, or header, expression matches only one value then both an exact variable and a variable with added _0 is now contributed. Users who expect only one value will probably expect the exact variable name. Users who expects several will probably want to always use the variableName_X varaibles. 

[4cc63](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/4cc63cc9fc5be42) Tomas Bjerre *2017-07-13 19:45:19*

**doc**


[3d6e7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3d6e791dfedabfe) Tomas Bjerre *2017-07-13 18:15:51*


## 1.12 (2017-07-13)

### Other changes

**Contribute headers to build #9**


[9ad43](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/9ad43d864581af5) Tomas Bjerre *2017-07-13 18:08:52*

**Refactoring**

* Also replacing space in keys with underscore (_). 

[23077](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/23077594abea6a5) Tomas Bjerre *2017-07-13 15:23:12*

**doc**


[8de23](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/8de2365d832e07a) Tomas Bjerre *2017-07-12 18:41:20*


## 1.11 (2017-07-12)

### Other changes

**Doc**


[5a5b8](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/5a5b8e86caec71e) Tomas Bjerre *2017-07-12 18:28:27*

**Merge pull request #8 from juanpablo-santos/expanded_variables**

* fixes #7: Contribute all leafs in node, if node is selected by expression 

[85589](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/85589239d40fbb9) Tomas Bjerre *2017-07-12 17:38:46*

**fixes #7: JSONPath params are always converted to String**


[e1239](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e1239f8480fdcdf) Juan Pablo Santos Rodríguez *2017-07-12 07:08:10*

**doc**


[81361](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/81361f4bdd73d7b) Tomas Bjerre *2017-05-09 19:30:40*


## 1.10 (2017-05-09)

### Other changes

**Avoid showing trigger for unsupported projects #6**


[fd6d4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/fd6d4e647ec939d) Tomas Bjerre *2017-05-09 19:26:31*

**doc**


[cbf75](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/cbf75eb9d75192a) Tomas Bjerre *2017-05-03 19:31:19*


## 1.9 (2017-05-02)

### Other changes

**Enabling pipeline multibranch to be triggered #5**


[21af9](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/21af9ce2e996f49) Tomas Bjerre *2017-05-02 18:54:05*

**Adding troubleshooting section to readme #4**


[d1288](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/d1288153a6ae2ff) Tomas Bjerre *2017-04-29 06:47:22*


## 1.8 (2017-04-10)

### Other changes

**Only printing variables/post content once in the job log**


[00bf4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/00bf4c75de19007) Tomas Bjerre *2017-04-10 17:28:55*


## 1.7 (2017-04-07)

### Other changes

**Correcting invoke URL in docs**


[417e7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/417e712357349ff) Tomas Bjerre *2017-04-07 18:27:58*

**doc**


[7ca0c](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/7ca0ca4e98a6747) Tomas Bjerre *2017-04-05 16:59:50*


## 1.6 (2017-03-29)

### Other changes

**White list request parameters #3**

* To make it more secure. Also adding regexp filter for them. 

[0c4b7](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/0c4b799015166cc) Tomas Bjerre *2017-03-29 19:57:13*

**doc**


[25d64](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/25d6448d957ca4f) Tomas Bjerre *2017-03-28 18:32:38*


## 1.5 (2017-03-28)

### Other changes

**Include request parameters #3**


[3b162](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/3b1626c816d563e) Tomas Bjerre *2017-03-28 18:07:35*

**doc**


[dad0d](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/dad0d8f3a2d25a3) Tomas Bjerre *2017-03-14 18:14:16*


## 1.4 (2017-03-14)

### Other changes

**Correcting wiki link**


[37694](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/37694044b53d9f9) Tomas Bjerre *2017-03-14 18:10:59*


## 1.3 (2017-03-13)

### Other changes

**Adding filtering option #2**


[e6111](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e6111511bfe802d) Tomas Bjerre *2017-03-13 07:19:49*

**doc**


[b80be](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/b80be7df1e2eb66) Tomas Bjerre *2017-03-12 22:07:15*


## 1.2 (2017-03-12)

### Other changes

**Avoiding duplicated logging info**


[e9db0](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/e9db0102aeb4563) Tomas Bjerre *2017-03-12 21:46:38*

**doc**


[6b2c4](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/6b2c43524f6f5b6) Tomas Bjerre *2017-03-12 20:21:51*


## 1.1 (2017-03-12)

### Other changes

**Defaulting expression type to JSONPath**


[80bc1](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/80bc126c7229ac9) Tomas Bjerre *2017-03-12 20:17:13*

**doc**


[ec4a3](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/ec4a38b96200c76) Tomas Bjerre *2017-03-09 04:42:06*


## 1.0 (2017-03-09)

### Other changes

**Initial**


[32c89](https://github.com/jenkinsci/generic-webhook-trigger-plugin/commit/32c89bb52ac8a57) Tomas Bjerre *2017-03-08 16:27:37*


