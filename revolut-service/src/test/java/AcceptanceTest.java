import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.revolut.RevolutApplication;
import com.revolut.config.RevolutServiceConfiguration;
import com.revolut.domain.model.ErrorResponse;
import com.revolut.rest.CreateAccountRequest;
import com.revolut.rest.CreateAccountResponse;
import com.revolut.rest.TransferRequest;
import com.revolut.rest.ViewAccontResponse;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTest {

    private static final int UNPROCESSABLE_ENTITY_STATUS_CODE = 422;
    private static final BigDecimal AMOUNT_TO_TRANSFER = new BigDecimal(20.00);
    private static final String ACCOUNTS_PATH = "/accounts";
    private static final String NULL_ACCOUNT_ID = null;
    private static final String EMPTY_ACC_ID = "";

    private RequestSpecBuilder apiReqSpecBuilder;

    @ClassRule
    public static final DropwizardAppRule<RevolutServiceConfiguration> RULE =
            new DropwizardAppRule<RevolutServiceConfiguration>(RevolutApplication.class,
                    resourceFilePath("configuration.yml"));

    @Before
    public void setUp() throws Exception {
        apiReqSpecBuilder = new RequestSpecBuilder();
        apiReqSpecBuilder.setBaseUri(String.format("http://localhost:%d/", RULE.getLocalPort()));
        apiReqSpecBuilder.setBasePath("api/v1/");
    }

    @Test
    public void should_transfer_money_from_one_account_to_another_when_enough_balance_in_origin_account() {
        String originAccId = createAnAccount(new CreateAccountRequest(100.00));
        String destinationAccId = createAnAccount(new CreateAccountRequest(50.00));
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                AMOUNT_TO_TRANSFER);

        transferMoney(transferRequest, Response.Status.NO_CONTENT.getStatusCode());

        assertThat(fetchAnAccountBy(originAccId)
                .extract()
                .as(ViewAccontResponse.class)
                .getBalance(), is(80.00));
        assertThat(fetchAnAccountBy(destinationAccId)
                .extract()
                .as(ViewAccontResponse.class)
                .getBalance(), is(70.00));
    }

    @Test
    public void should_throw_precodintion_failed_error_when_origin_account_id_is_null() {
        String destinationAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                NULL_ACCOUNT_ID,
                destinationAccId,
                AMOUNT_TO_TRANSFER);

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_destination_account_id_is_null() {
        String originAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                NULL_ACCOUNT_ID,
                AMOUNT_TO_TRANSFER);

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_origin_account_id_is_empty() {
        String destinationAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                EMPTY_ACC_ID,
                destinationAccId,
                AMOUNT_TO_TRANSFER);

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_destination_account_id_is_empty() {
        String originAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                EMPTY_ACC_ID,
                AMOUNT_TO_TRANSFER);

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_amount_is_null() {
        String originAccId = createAnAccount(new CreateAccountRequest());
        String destinationAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                null);

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_amount_is_not_greater_than_zero() {
        String originAccId = createAnAccount(new CreateAccountRequest());
        String destinationAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                new BigDecimal(-10.00));

        transferMoney(transferRequest, UNPROCESSABLE_ENTITY_STATUS_CODE);
    }

    @Test
    public void should_throw_precodintion_failed_error_when_origin_account_does_not_have_enough_balance() {
        String originAccId = createAnAccount(new CreateAccountRequest());
        String destinationAccId = createAnAccount(new CreateAccountRequest());
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                AMOUNT_TO_TRANSFER);

        ValidatableResponse response = transferMoney(transferRequest,
                Response.Status.PRECONDITION_FAILED.getStatusCode());

        assertThat(response.extract().as(ErrorResponse.class).getMessage(),
                is("Account with id: " + originAccId + " does not have enough balance"));
    }

    @Test
    public void should_throw_not_found_exception_when_id_does_not_match_any_account() {
        RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .get(ACCOUNTS_PATH + "/{id}", "non-existing-id")
                .then()
                .assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    private String createAnAccount(CreateAccountRequest account) {
        return RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(account)
                .post(ACCOUNTS_PATH)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract()
                .as(CreateAccountResponse.class)
                .getAccId();
    }

    private ValidatableResponse fetchAnAccountBy(String id) {
        return RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .get(ACCOUNTS_PATH + "/{id}", id)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    private ValidatableResponse transferMoney(TransferRequest transferRequest, int statusCode) {
        return RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(transferRequest)
                .post(ACCOUNTS_PATH + "/transfer")
                .then()
                .assertThat()
                .statusCode(statusCode);
    }
}
