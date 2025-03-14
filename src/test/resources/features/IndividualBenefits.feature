@local
Feature:  Local - As Individual-Benefits api consumer I should be able to view all IABD's

  Scenario Outline: validate success responses from individual-benefits api call
    Given I have generated a test user for individual-benefits with the UTR: <utr> for: <taxYear> with scenario: <scenario json>
    And I have generated a bearer token Valid for the UTR '<utr>'
    Then I should get the <statusCode> success status when calling individual-benefits with UTR: <utr> and tax year: <taxYear> for scenario: <scenario json>

    Examples:
      | utr        | taxYear | statusCode | scenario json |
      | 1097172564 | 2017-18 | 200        | HAPPY_PATH_1  |
      | 1097172564 | 2018-19 | 200        | HAPPY_PATH_2  |

  Scenario Outline: validate error responses from individual-benefits api call
    Given I have generated a test user for individual-benefits with the UTR: <utr> for: <taxYear> with scenario: <scenario json>
    And I have generated a bearer token <bearer-token> for the UTR '<utr>'
    Then I should get the statusCode: <statusCode> responseCode: <error code> responseMessage: <message> when calling individual-benefits with UTR: <utr> and tax year: <taxYear>

    Examples:
      | utr        | taxYear | statusCode | error code            | message                                   | bearer-token | scenario json |
      | 109717256- | 2017-18 | 400        | SA_UTR_INVALID        | The provided SA UTR is invalid            | Valid        | HAPPY_PATH_1  |
      |            | 2017-18 | 404        | NOT_FOUND             | Resource was not found                    | Valid        | HAPPY_PATH_1  |
      | 1097172564 | 2017-19 | 400        | TAX_YEAR_INVALID      | The provided Tax Year is invalid          | Valid        | HAPPY_PATH_1  |
      | 1097172564 | 2017-18 | 401        | UNAUTHORIZED          | Bearer token is missing or not authorized | Missing      | HAPPY_PATH_1  |
      | 1097172564 | 2017-18 | 401        | UNAUTHORIZED          | Bearer token is missing or not authorized | Invalid      | HAPPY_PATH_1  |
      | 1097172564 | 2017-18 | 406        | ACCEPT_HEADER_INVALID | The accept header is missing or invalid   | Valid        | HAPPY_PATH_1  |

  Scenario Outline: validate error response from individual-benefits api call - new/different user
    Given I have generated a test user for individual-benefits with the UTR: <utr> for: <taxYear> with scenario: <scenario json>
    And I have generated a bearer token Valid for the UTR '1097172555'
    Then I should get the statusCode: <statusCode> responseCode: <error code> responseMessage: <message> when calling individual-benefits with UTR: <utr> and tax year: <taxYear>

    Examples:
      | utr        | taxYear | statusCode | error code   | message                                   | scenario json |
      | 1097172564 | 2017-18 | 401        | UNAUTHORIZED | Bearer token is missing or not authorized | HAPPY_PATH_1  |
