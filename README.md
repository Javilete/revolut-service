##Revolut Service##

Design and implement a RESTful API (including data model and the backing implementation) for money
transfers between accounts.
1. Keep it simple and use common sense
2. Use whatever framework/libraries (no spring)
3. Datastore in-memory
4. Executable as a standalone program
5. Demostrate with tests API works

SOLUTION:
Assumptions to keep it simple and straight to the point:
1. Accounts will be identified by UUID internally
2. Money is in a unique currency
3. If origin account does not have enough balance, throws an exception

Dropwizard framework used which contains an embedded jetty HTTP server, jersey for building the
RESTful application and jackson for json to handle the data.

TDD practice followed using outside-in (double loop red -> green -> refactor) approach where the implementation starts writing our
acceptance test should_transfer_money_from_one_account_to_another_when_enough_balance_in_origin_account.
Once our acceptance test is written without any compilation errors, we move in to the inner loop
to unit test our resource component (AccountResource). First, we need to create account to be able
to transfer money between them (POST: /accounts), secondly we need to expose the /transfer endpoint
to handle the money movement between accounts and thirdly retrieve an account by id to be able to
verify that account's balance has been updated.
From the resource class, we go deeper to create the necessary collaborators AccountService, in-memory repository
that will store the accounts (Map) and account data model to be able to
accomplish and make our acceptance test passed in the end.
Our acceptance test will guide us for the implementation since once the unit testing is done for a
component we will run the acceptance test and the failure will tell us what is the next unit test to be done
and the implementation to satisfy it.
After the main implementation is done, edge cases like validation of account ids and the amount to transfer
has been covered including the corresponding tests under the AcceptanceTest class.

The endpoints created are the following:

Create an account
POST: /accounts
{
	"balance": 12.12
}
(Empty body or no balance would create an account with initial balance of 0.00)

Transfer money between two accounts
POST: /accounts/transfer
{
	"originId": "b31b7ab4-9b4d-487e-8deb-ad682df7d350",
	"destinationId": "9467d099-c65f-43be-88d2-5e72bb9debc9",
	"amount": 10.45
}

Retrieve an account by id
GET: /accounts/{id}

To start the application run the script startApp.sh


