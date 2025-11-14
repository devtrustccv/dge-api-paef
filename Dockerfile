# Etapa 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia os arquivos necessários para o build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final para produção
FROM eclipse-temurin:21-jre-alpine
# Adiciona um usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia apenas o JAR gerado na etapa anterior
COPY --from=builder /app/target/*.jar app.jar
#

# Altera permissões para o usuário não-root
RUN chown -R appuser:appgroup /app

# Define o usuário não-root para executar a aplicação
USER appuser

# Expõe a porta da aplicação
EXPOSE 8080

# Define variáveis de ambiente para produção
ENV SPRING_PROFILES_ACTIVE=prod
# variável JAVA_OPTS com -Xms256m (memória inicial) e -Xmx512m (memória máxima) para limitar o uso de memória.
# Ajustar esses valores com base nas necessidades da sua aplicação.
#Incluído -XX:+UseG1GC para usar o Garbage Collector G1, que é eficiente para aplicações Spring Boot.
ENV JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC -Xlog:gc*"

# Adiciona um healthcheck para monitoramento
##HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
##    CMD curl --fail http://localhost:8080/actuator/health || exit 1

# Define a variável de ambiente para o perfil de produção
ENV SPRING_PROFILES_ACTIVE=prod

# Comando de inicialização
#ENTRYPOINT ["java", "-jar", "app.jar"]
# Comando de inicialização com opções da JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]