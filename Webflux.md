We have a lot of contents on Web flux right but here we are going to discuss about unusual scenarios and related handling.

Scenario1:
Lets say SAVE-C is dependent on SAVE-A and SAVE-B is also dependent on SAVE-A viz. 
CRUD save should appear in this order: 
```
         SAVE-A
        /       \
      SAVE-B   SAVE-C
```

> /*
   Below code represents the scenario:
   ```
   Requirement: 3 save operation. Each dependent on the first save operation response.
                Response of the first save operation needs to be propagated to all the other save operations since corresponding model to be created from the first res.
                At last, first response viz. modelAResponse to be sent back to User.
```
> */

```java

return modelAR2Repository.save(modelA);
        }).flatMap(modelAResponse ->{
            Mono<modelBResponse> modelBResponse = modelBR2Repository.save(createModelBFromModelA(modelAResponse));
            return modelBResponse.flatMap(modelBResponse -> Mono.just(modelAResponse));
        }).flatMap(modelAResponse ->{
            Mono<modelCResponse> modelCResponse = modelCR2Repository.save(createModelCFromModelA(modelAResponse));
            return modelCResponse.flatMap(modelCResponse -> Mono.just(modelAResponse));
        });

```
Scenario2: 
Lets say SAVE-C is dependent on SAVE-B and SAVE-B is also dependent on SAVE-A viz. 
         SAVE-A   --> SAVE-B   ---> SAVE-C