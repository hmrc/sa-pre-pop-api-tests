
# sa-pre-pop-api-tests

This is the acceptance test repository for Individual Benefits/Employments/Income/Tax. These acceptance tests are actually API tests; our microservice has no front-end. The tests are to verify when we make a call through our service via the API platform, the data is retrieved from a DES/stub, **paye-des-stub**, and can be found at: https://github.com/hmrc/paye-des-stub, and then filtered according to the initial request.

## How to run a normal suite in local:

First you need the services running:
```shell
sm2 --start API_SA_PREPOP_ALL
```

You can run the tests using the shell script:
```shell
./run_tests_local.sh
```

## How to run zap tests
First the services need to be started by running:
```shell
sm2 --start API_SA_PREPOP_ALL
```

The `run_zap_tests_local.sh` file now uses [dast-config-manager](https://github.com/hmrc/dast-config-manager) to run ZAP tests locally using the DAST Docker image.
