# daily-reward-parse-util

Parses the Hypixel daily reward website into a basic Java API

## Examples

### Use the library to take the ID, get the options and claim the first option
```java
try (var reward = HypixelReward.fromId("abcd1234")) {
    reward.retrieveRewardOptions()
            .thenApply(response -> response.getOptions().get(0))
            .thenCompose(RewardOption::claim)
            .thenAccept(unused -> System.out.println("Claimed"))
}
```

### Parse the HTML
```java
var parseResult = RewardParser.parse(html);
System.out.println(parseResult.getSecurityToken());
```
The parser is an afterthought, so its more of just a 1:1 to the data JSON.
You are able to convert the parse result if you have the CSRF cookie
which comes from the cookie named `_csrf` from an HTTP response of the
main page. An example of this:
```java
var response = new RewardResponse(HypixelReward.fromId(parseResult.getId()), csrfCookie, parseResult);
```