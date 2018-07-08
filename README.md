# Java Lambda - context evaluation

This Lambda code shows how AWS reuses the same environment context between several Java Lambda executions[^1].
Especially, this demonstrates how the function handler is called from a Singleton instance.
 

My definition of a '_Lambda environment context_' is the following:

- the container running the Lambda
- the JVM running the Lambda
- the instance of the class where the handler is attached (a singleton)


In the [AWS Lambda documentation](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html), one of the best practice recommends to take advantage of this execution context:

>>>
    Take advantage of Execution Context reuse to improve the performance of your function. Make sure any externalized configuration or dependencies that your code retrieves are stored and referenced locally after initial execution. Limit the re-initialization of variables/objects on every invocation. Instead use static initialization/constructor, global/static variables and singletons. Keep alive and reuse connections (HTTP, database, etc.) that were established during a previous invocation.


Unfortunately, the `use static initialization/constructor, global/static variables and singletons` part is confusing.

I don't think they should have mentioned the use of `static initialization` as a best practice, but instead it should be emphasized that the class where the handler method is running is itself a singleton. 
Using static initializers decrease the testability of the code while not providing any benefit.




## Requirements

* AWS CLI already configured
* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker installed](https://www.docker.com/community-edition)
* [Maven](https://maven.apache.org/install.html)
* [AWS SAM CLI installed](https://docs.aws.amazon.com/lambda/latest/dg/sam-cli-requirements.html)


## Deploy


We use `maven` to install our dependencies and package our application into a JAR file:
```bash
mvn package
```

Firstly, we need a `S3 bucket` where we can upload our Lambda functions packaged as ZIP before we deploy anything - If you don't have a S3 bucket to store code artifacts then this is a good time to create one:

```bash
aws s3 mb s3://BUCKET_NAME
```

Next, run the following command to package our Lambda function to S3:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket REPLACE_THIS_WITH_YOUR_S3_BUCKET_NAME
```

Next, the following command will create a Cloudformation Stack and deploy your SAM resources.

```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name lambda-app \
    --capabilities CAPABILITY_IAM
```

After deployment is complete you can run the following command to retrieve the API Gateway Endpoint URL:

```bash
aws cloudformation describe-stacks \
    --stack-name lambda-app \
    --query 'Stacks[].Outputs[1].OutputValue'
```

## Conclusion:

Navigate to the url a few times.
You will notice that not all the random number change after each invocation:
- The static number is evaluated only once for this Lambda instance
- The number initialized in the constructor is _also_ evaluated only _once_
- The number generated in the handler is generated every time.

This is because AWS reuses the environment context to run the same instance of a Lambda (_most of the time_). The class holding the handler function is a Singleton and its instance is reused between invocation.

If this Lambda api was called multiple times and _in parallel_ you would noticed that AWS starts other environment contexts to run the invocations in parallel.




[^1]: AWS does not provide any guarantee to reuse the same execution context between invocations, but it is generally the case. If the invocation