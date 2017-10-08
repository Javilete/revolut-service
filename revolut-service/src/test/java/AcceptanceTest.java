import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.revolut.RevolutApplication;
import com.revolut.config.RevolutServiceConfiguration;
import com.revolut.domain.model.Account;
import com.revolut.domain.rest.CreateAccountRequest;
import com.revolut.domain.rest.CreateAccountResponse;
import com.revolut.domain.rest.ErrorResponse;
import com.revolut.domain.rest.TransferRequest;
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

    private static final String ACCOUNTS_PATH = "/accounts";
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
        String originAccId = createAnAccount(new CreateAccountRequest("100.00"))
                .extract()
                .as(CreateAccountResponse.class)
                .getAccId();
        String destinationAccId = createAnAccount(new CreateAccountRequest("50.00"))
                .extract()
                .as(CreateAccountResponse.class)
                .getAccId();
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                "20.00");

        transferMoney(transferRequest, Response.Status.NO_CONTENT.getStatusCode());

        assertThat(fetchAnAccountBy(originAccId)
                .extract()
                .as(Account.class)
                .getBalance(), is(new BigDecimal("80.00")));
        assertThat(fetchAnAccountBy(destinationAccId)
                .extract()
                .as(Account.class)
                .getBalance(), is(new BigDecimal("70.00")));
    }

    @Test
    public void should_throw_precodintion_failed_error_when_origin_account_does_not_have_enough_balance() {
        String originAccId = createAnAccount(new CreateAccountRequest())
                .extract()
                .as(CreateAccountResponse.class)
                .getAccId();
        String destinationAccId = createAnAccount(new CreateAccountRequest())
                .extract()
                .as(CreateAccountResponse.class)
                .getAccId();
        TransferRequest transferRequest = new TransferRequest(
                originAccId,
                destinationAccId,
                "20.00");

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

    private ValidatableResponse createAnAccount(CreateAccountRequest account) {
        return RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(account)
                .post(ACCOUNTS_PATH)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
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
