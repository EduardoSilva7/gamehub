#!/usr/bin/env bash
set -euo pipefail

SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
SPRING_DATASOURCE_USERNAME=MR98410
SPRING_DATASOURCE_PASSWORD=fiap25...

SUBSCRIPTION="${AZ_SUBSCRIPTION_ID:-}"
LOCATION="brazilsouth"
RG="rg-games-rm98410"
PLAN="asp-games-rm98410"
APP="app-games-rm98410"
AI="ai-games-rm98410"

echo "==> Ensuring Application Insights extension"
az extension add -n application-insights --only-show-errors || true

if [[ -n "${SUBSCRIPTION}" ]]; then
  az account set --subscription "$SUBSCRIPTION"
fi

echo "==> Resource group"
az group create -l "$LOCATION" -n "$RG" --only-show-errors -o table

echo "==> App Service plan (Linux)"
az appservice plan create -g "$RG" -n "$PLAN" --is-linux --sku B1 --only-show-errors -o table

echo "==> Web App Java 17"
if ! az webapp show -g "$RG" -n "$APP" &>/dev/null; then
  az webapp create -g "$RG" -p "$PLAN" -n "$APP" --runtime "JAVA:17-java17" --only-show-errors -o table
fi

echo "==> App Insights"
if ! az monitor app-insights component show -g "$RG" -a "$AI" &>/dev/null; then
  az monitor app-insights component create -g "$RG" -a "$AI" -l "$LOCATION" --only-show-errors -o table
fi
CS=$(az monitor app-insights component show -g "$RG" -a "$AI" --query connectionString -o tsv)
IK=$(az monitor app-insights component show -g "$RG" -a "$AI" --query instrumentationKey -o tsv)

echo "==> App settings (Oracle + Insights)"
az webapp config appsettings set -g "$RG" -n "$APP" --settings \
  SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL:-}" \
  SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME:-}" \
  SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD:-}" \
  SPRING_JPA_HIBERNATE_DDL_AUTO="none" \
  SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT="org.hibernate.dialect.OracleDialect" \
  JAVA_OPTS="-Xms256m -Xmx1024m" \
  APPINSIGHTS_INSTRUMENTATIONKEY="$IK" \
  APPLICATIONINSIGHTS_CONNECTION_STRING="$CS" \
  WEBSITES_PORT="80" \
  SERVER_PORT="80" \
  --only-show-errors -o table

echo "==> Build Maven"
mvn -q -DskipTests clean package

JAR="target/gamehub-0.0.1-SNAPSHOT.jar"
echo "==> Deploying $JAR"
az webapp deploy -g "$RG" -n "$APP" --type jar --src-path "$JAR" --target-path app.jar --only-show-errors -o table
echo "==> Done: https://$APP.azurewebsites.net"
