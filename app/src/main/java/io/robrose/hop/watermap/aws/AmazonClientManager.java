package io.robrose.hop.watermap.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by Robert on 2/6/2016.
 */
public class AmazonClientManager {
    private static final String LOG_TAG = "AmazonClientManager";

    private AmazonDynamoDBClient ddb = null;
    private Context context;

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public void validateCredentials() {
        if(ddb == null) {
            initClients();
        }
    }

    private void initClients() {
        CognitoCachingCredentialsProvider creds = new CognitoCachingCredentialsProvider(
                context,
                Constants.ACCOUNT_ID,
                Constants.IDENTITY_POOL_ID,
                Constants.UNAUTH_ROLE_ARN,
                null,
                Regions.US_EAST_1
        );

        ddb = new AmazonDynamoDBClient(creds);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException e) {
        Log.e(LOG_TAG, "wipeCredentialsOnAuthError called", e);

        String es = e.getErrorCode();
        if(es.equals("IncompleteSignature")
            || es.equals("InternalFailure")
            || es.equals("InvalidClientTokenId")
            || es.equals("OptInRequired")
            || es.equals("RequestExpired")
            || es.equals("ServiceUnavailable")

            // DynamoDB
            || es.equals("AccessDeniedException")
            || es.equals("IncompleteSignatureException")
            || es.equals("MissingAuthenticationTokenException")
            || es.equals("ValidationException")
            || es.equals("InternalFailure")
            || es.equals("InternalServerError")) {
            return true;
        }

        return false;
    }
}
