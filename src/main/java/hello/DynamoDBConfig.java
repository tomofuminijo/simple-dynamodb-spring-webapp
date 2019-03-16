package hello;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;


@Configuration
@EnableDynamoDBRepositories(basePackages = "hello.repositories")
public class DynamoDBConfig {
    @Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;
	
    @Value("${amazon.dynamodb.region}")
	private String amazonDynamoDBRegion;

    @Value("${amazon.credential.profile}")
    private String profile;
    
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {

		System.out.println("---- DynamoDB Endpoint: " + amazonDynamoDBEndpoint);
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(amazonAWSCredentials())
			.withEndpointConfiguration(
			new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonDynamoDBRegion))
			.withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()))
			.build();

/*		
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
*/

		return amazonDynamoDB;
	}

	@Bean
	public AWSCredentialsProvider amazonAWSCredentials() {
		return DefaultAWSCredentialsProviderChain.getInstance();
//		return new ProfileCredentialsProvider(profile);
	}
}