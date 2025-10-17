# dge-api-paef

Projeto Spring Boot (Java 17) no padrão dos teus outros serviços (camadas `domain`/`infrastructure`/`interfaces`), com:
- JPA + PostgreSQL
- Flyway
- MapStruct + DTOs
- OpenAPI/Swagger
- Actuator
- Dockerfile e docker-compose

## Como rodar rápido (Dev)
1. Subir Postgres via docker-compose (usa porta 5433 local):
   ```bash
   docker compose up -d postgres
   ```
2. Build & run:
   ```bash
   ./mvnw spring-boot:run
   ```
   ou
   ```bash
   mvn spring-boot:run
   ```

A app sobe em `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui/index.html`

## Produção (compose com API)
```bash
docker compose up --build
```

## Estrutura de pacotes
```
cv.gov.dge.paef
├─ config
├─ domain
│  ├─ entidade
│  └─ qualificacao
├─ infrastructure
│  └─ repository
└─ interfaces
   ├─ dto
   ├─ mapper
   ├─ rest
   └─ error
```

## Rotas de exemplo
- `POST /api/entidades` `{ nif, nome }`
- `GET /api/entidades`
- `GET /api/entidades/{id}`
- `PUT /api/entidades/{id}`
- `DELETE /api/entidades/{id}`

- `POST /api/qualificacoes` `{ codigo, designacao, versao }`
- `GET /api/qualificacoes` ...
```

