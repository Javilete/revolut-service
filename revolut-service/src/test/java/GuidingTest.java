import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.revolut.RevolutApplication;
import com.revolut.config.RevolutServiceConfiguration;
import com.revolut.domain.Account;
import com.revolut.domain.CreateAccountRequest;
import com.revolut.domain.CreateAccountResponse;
import com.revolut.domain.TransferRequest;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.hamcrest.core.Is.is;

public class GuidingTest {

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
        CreateAccountRequest originAccount = new CreateAccountRequest("100.00");
        ValidatableResponse originAcc = RestAssured.given().contentType(ContentType.JSON)
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(originAccount)
                .post("/accounts")
                .then()
                .assertThat()
                .statusCode(201);

        CreateAccountRequest destinationAccount = new CreateAccountRequest("50.00");
        ValidatableResponse destAcc = RestAssured.given().contentType(ContentType.JSON.withCharset("UTF-8"))
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(destinationAccount)
                .post("/accounts")
                .then()
                .assertThat()
                .statusCode(201);

        String originId = originAcc.extract().as(CreateAccountResponse.class).getAccId();
        String destinationId = destAcc.extract().as(CreateAccountResponse.class).getAccId();

        TransferRequest transferRequest = new TransferRequest(
                originId,
                destinationId,
                "20.00");

        ValidatableResponse transferResponse = RestAssured.given().contentType(ContentType.JSON.withCharset("UTF-8"))
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .body(transferRequest)
                .post("/accounts/transfer")
                .then()
                .assertThat()
                .statusCode(204);

        ValidatableResponse originAccResponse = RestAssured.given().contentType(ContentType.JSON.withCharset("UTF-8"))
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .get("/accounts/{id}", originId)
                .then()
                .assertThat()
                .statusCode(200);

        ValidatableResponse destAccResponse = RestAssured.given().contentType(ContentType.JSON.withCharset("UTF-8"))
                .log()
                .all()
                .spec(apiReqSpecBuilder.build())
                .get("/accounts/{id}", destinationId)
                .then()
                .assertThat()
                .statusCode(200);

        Assert.assertThat(originAccResponse.extract().as(Account.class).getBalance(), is(new BigDecimal("120.00")));
        Assert.assertThat(destAccResponse.extract().as(Account.class).getBalance(), is(new BigDecimal("30.00")));
    }
}
