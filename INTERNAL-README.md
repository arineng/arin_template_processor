ARIN Template Processor
-----------------------

## Docker Run

    docker pull harbor.arin.net/dev/arin-tp:master
    docker run -ti --env-file=<full path of .env file> harbor.arin.net/dev/arin-tp:master

You can configure the TP Docker container by setting its environment variables in a `.env` file (see the
[config/.env](./config/.env) file for a sample) and then using the `--env-file` option. You can also use the `--env`
option to set these variables. The `--env-file` option is recommended over the `--env` option because passing
environment variables on the command line can sometimes be problematic, especially for values with whitespace
characters. See the **Environment Variables** section in the [README](./README.md) for a complete list of available
environment variables and their possible values.

This container will interact with SMTP and Reg-RWS servers.

## Dev

### Build

#### Run Tests

    ./gradlew clean build --info --rerun-tasks

#### Skip Tests

    ./gradlew clean build -PskipTests=true --info --rerun-tasks

#### Build Docker Image

    ./gradlew clean build buildDockerImage [-PimageName=<name>] [-PimageVersion=<version>] [-Pbranch=<branch>] --info --rerun-tasks

By default, image name is `arin-tp` and version `latest`.

If `imageVersion` property is set, image version will be set to its value.

If `imageName` property is set, image name will be set to its value. Otherwise, if `branch` property is set to `master`,
image name will be set to `master`; else to `f_<branch>` where `f` is for feature.

### Run

Bring up SMTP and Reg-RWS using `up -t` in DUDE. Then:

    ./run.sh --env-file=<Full path of .env file>
    docker exec -ti --env COLUMNS=`tput cols` --env LINES=`tput lines` arin-tp /bin/bash
