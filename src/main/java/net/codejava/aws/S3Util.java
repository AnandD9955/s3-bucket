package net.codejava.aws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public class S3Util {
	private static final String BUCKET = "3ktechnologies-10";
	
	public static void uploadFile(String fileName, InputStream inputStream) 
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		S3Client client = S3Client.builder().build();
		
		PutObjectRequest request = PutObjectRequest.builder()
										.bucket(BUCKET)
										.key(fileName)
										.acl("public-read")
										.build();
		
		client.putObject(request, 
				RequestBody.fromInputStream(inputStream, inputStream.available()));
		
//		S3Waiter waiter = client.waiter();
//		HeadObjectRequest waitRequest = HeadObjectRequest.builder()
//											.bucket(BUCKET)
//											.key(fileName)
//											.build();
//
//		WaiterResponse<HeadObjectResponse> waitResponse = waiter.waitUntilObjectExists(waitRequest);
//
//		waitResponse.matched().response().ifPresent(x -> {
//			// run custom code that should be executed after the upload file exists
//		});

	}

	public static InputStream downloadFile(String fileName) throws
			S3Exception, AwsServiceException, SdkClientException, IOException {

		S3Client client1 = S3Client.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();

		GetObjectRequest request1 = GetObjectRequest.builder()
				.bucket(BUCKET)
				.key(fileName)
				.build();

		ResponseInputStream<?> response = client1.getObject(request1);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = response.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		response.close();
		client1.close();

		return new ByteArrayInputStream(outputStream.toByteArray());
	}

}
