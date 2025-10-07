# GameHub — How to Use (FIAP Oracle + Azure App Service)

Este guia é **passo a passo** para usar o GameHub: rodar localmente, criar o banco, configurar variáveis e (opcional) publicar na **Azure App Service** com **Application Insights**. O professor permitiu usar **Oracle FIAP** no lugar do Azure SQL.

---

## 1) Pré‑requisitos
- **Java 17+** e **Maven 3.8+**
- Acesso a um **Oracle (FIAP)**: `<HOST>`, `<PORT>`, `<SERVICE_NAME>`, `<USER>`, `<PASS>`
- (Opcional) **Azure CLI** se for publicar: `az --version`

> O driver **Oracle JDBC (ojdbc8)** já está declarado no `pom.xml` do projeto.

---

## 2) Banco de Dados (criar tabelas)
No **SQL Developer**, execute os blocos do arquivo(db/gamehub.sql):
Isso criará as tabelas `GAMES` e `REVIEWS` com FK de `REVIEWS.GAME_ID → GAMES.GAME_ID`.

---

## 3) Variáveis de ambiente (Oracle FIAP)
Defina no seu terminal **antes de rodar**:

```bash
# Exemplo
export SPRING_DATASOURCE_URL="jdbc:oracle:thin:@<HOST>:<PORT>/<SERVICE_NAME>"
export SPRING_DATASOURCE_USERNAME="<USER>"
export SPRING_DATASOURCE_PASSWORD="<PASS>"
export SERVER_PORT=8080
```

> A aplicação lê estas variáveis (veja `application.properties`).

---


## 4) Publicando na **Azure App Service** 

1. **Login na Azure**  
   ```bash
   az login
   az account set --subscription "<SUBSCRIPTION_ID>"
   ```

2. **Build do JAR**  
   ```bash
   mvn -DskipTests package
   ```

3. **Criar/usar App Service** (Java SE 17) e **definir variáveis**  
   No portal, em *Configuration → Application Settings* do seu App Service, adicione:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - (Opcional) `APPLICATIONINSIGHTS_CONNECTION_STRING`
   - **Não** defina `SERVER_PORT` (o padrão 8080 é o correto na Azure).

4. **Deploy do JAR**  
   ```bash
   az webapp deploy -g <RESOURCE_GROUP> -n <APP_NAME>      --type jar --path target/<seu-jar>.jar
   ```

5. **Application Insights** (monitorar)  
   - Vincule um recurso de Application Insights ao App Service (ou só preencha a `APPLICATIONINSIGHTS_CONNECTION_STRING`).  
   - Acesse **Application Insights → Live Metrics / Logs** e gere tráfego usando a aplicação.

---

## 5) Verificando logs (diagnóstico)
Ative logs e veja o *Log Stream* para encontrar a causa de erros de inicialização (ex.: `BeanCreationException`):

```bash
az webapp log config -g <RG> -n <APP_NAME>   --application-logging filesystem --web-server-logging filesystem --level Information
az webapp log tail -g <RG> -n <APP_NAME>
```

**Erros comuns e como resolver:**
- `ClassNotFoundException: oracle.jdbc.OracleDriver` → faltou `ojdbc8` no `pom.xml` (adicione e faça build).
- `ORA-01017` (usuário/senha) → corrija `SPRING_DATASOURCE_USERNAME/PASSWORD`.
- `ORA-12541`/`TNS:no listener` ou timeout → host/porta/serviço inacessíveis da Azure. (Ambientes FIAP podem bloquear rede externa.)  
  **Alternativa para a demo**: ative o profile H2 abaixo para subir na Azure e demonstre FIAP localmente.
- `BindException: Address already in use` → não altere a porta; use 8080 (padrão do App Service).

---

- Eduardo Ferreira Silva de Jesus - rm98410

