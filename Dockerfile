ARG VERSION=19.0.0.Final
FROM jboss/wildfly:${VERSION}

USER jboss:root

ARG CONFIGURATION="$JBOSS_HOME/standalone/configuration"
COPY --chown=jboss:root ./config/standalone.xml $CONFIGURATION

ARG PROPERTIES="$JBOSS_HOME/properties"
RUN mkdir -p $PROPERTIES && chown -R $USER:$GROUP $PROPERTIES
COPY --chown=jboss:root ./config/template_processor.properties $PROPERTIES

ARG DEPLOYMENTS="$JBOSS_HOME/standalone/deployments"
COPY --chown=jboss:root ./template_processor/build/libs/*.war $DEPLOYMENTS

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-P", "/opt/jboss/wildfly/properties/template_processor.properties", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
