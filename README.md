## Spring-Cloud-Netflix-Feign STUPS AccessTokens Support

###Dependency

```
    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>spring-cloud-netflix-feign-tokens</artifactId>
        <version>${version}</version>
    </dependency>
```

###Usage

Define your client as usual:

```
@FeignClient("kio")
public interface KioFeignClient {

    @RequestMapping(value = "/somewhere/{term}", method = RequestMethod.GET)
    Map<String, String> getSomewhat(String term);
}
```

**BUT** instead of using ```@EnableFeignClients``` use the following this:

```
@Configuration
@EnableOAuth2FeignClients(basePackages = {"org.zalando.stups.clients.feign"})
public class FeignConfig { }
```



## License

Copyright © 2015 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
