FROM jboss/keycloak:11.0.2

COPY --chown=jboss:root lib/ /opt/lib/

USER jboss

RUN $JBOSS_HOME/bin/jboss-cli.sh --command="module add --name=org.mindrot.jbcrypt --resources=/opt/lib/jbcrypt-0.4.jar"

ADD cli/JDBC_PING.cli /opt/jboss/tools/cli/jgroups/discovery/

COPY --chown=jboss:root lib/ /opt/lib/

USER root

EXPOSE 8080

