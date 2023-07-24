We have a lot of contents on Web flux right but here we are going to discuss about unusual scenarios and related handling.

* Never use subscribe inside flatMap when you are returning a Mono or Flux to the client.
  Reason: subscribe applied to the Mono/Flux will execute code associated with it and this code will again get executed when the Mono/Flux as a response received by the caller. Example
  ```java
  Mono<ModelA> monoModelA =  modelAR2Repository.save(modelA);
  monoModelA.subscribe(mod ->{
           //some other async operation on mod.
        });
  return monoModelA; // this is returned to the caller through Controller method

  ```
In the above case, monoModelA will be saved twice viz. if its Mongo, you can see two documents persisted with same payload but different ids.

**Scenario1**:
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
**Scenario2**: 
Lets say SAVE-C is dependent on SAVE-B and SAVE-B is also dependent on SAVE-A viz. 
         SAVE-A   --> SAVE-B   ---> SAVE-C
In this case, we are returning saved result to be used in the next operation.
```java
return modelAR2Repository.save(modelA);
        }).flatMap(modelAResponse ->{
            Mono<modelBResponse> modelBResponse = modelBR2Repository.save(createModelBFromModelA(modelAResponse));
            return modelBResponse;
        }).flatMap(modelBResponse ->{
            Mono<modelCResponse> modelCResponse = modelCR2Repository.save(createModelCFromModelB(modelBResponse));
            return modelCResponse;
        });
```
